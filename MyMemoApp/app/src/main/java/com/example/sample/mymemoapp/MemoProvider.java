package com.example.sample.mymemoapp;

import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by yoshitaka.fujisawa on 2017/07/05.
 */

public class MemoProvider extends ContentProvider {
    //<authority>
    private static final String AUTHORITY = "com.example.sample.mymemoapp.memo";
    private static final String CONTENT_PATH = "files";
    //MIME prefix
    public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
    //set original MIME type
    public static final String MIME_ITEM = "vnd.memoapp.memo";
    public static final String MIME_TYPE_MULTIPLE = MIME_DIR_PREFIX + MIME_ITEM;
    public static final String MIME_TYPE_SINGLE = MIME_ITEM_PREFIX + MIME_ITEM;

    //handled uri
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);
    //request for memo list
    private static final int URI_MATCH_MEMO_LIST = 1;
    //request for single memo
    private static final int URI_MATCH_MEMO_ITEM = 2;

    //URI matcher
    //cf: content://<authority>/<path>/<_id>
    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_MATCH_MEMO_LIST); //id with target
        sMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", URI_MATCH_MEMO_ITEM); //id without target
    }

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        MemoDBHelper helper = new MemoDBHelper(getContext());
        mDatabase = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sMatcher.match(uri);

        Cursor cursor;
        switch (match) {
            case URI_MATCH_MEMO_LIST:
                cursor = mDatabase.query(MemoDBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case URI_MATCH_MEMO_ITEM:
                //if including "segment ID"
                String id = uri.getLastPathSegment();

                cursor = mDatabase.query(MemoDBHelper.TABLE_NAME, projection,
                        MemoDBHelper._ID + "=" + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"),
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("invalid uri: " + uri);
        }

        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //check caller signature
        if (!checkSignaturePermission()) throw new SecurityException();
        if (!validateInput(values)) throw new IllegalArgumentException("invalid values");

        int match = sMatcher.match(uri);
        if (match == URI_MATCH_MEMO_LIST) {
            long id = mDatabase.insertOrThrow(MemoDBHelper.TABLE_NAME, null, values);

            if (id >= 0) {
                Uri newUri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
                Context context = getContext();

                if (context != null) {
                    context.getContentResolver().notifyChange(newUri, null);
                }
                return newUri;
            } else {
                return null;
            }
        }
        throw new IllegalArgumentException("invalid uri: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (!checkSignaturePermission()) throw new SecurityException();

        int match = sMatcher.match(uri);
        switch (match) {
            case URI_MATCH_MEMO_LIST:
                return mDatabase.delete(MemoDBHelper.TABLE_NAME, selection, selectionArgs);

            case URI_MATCH_MEMO_ITEM:
                String id = uri.getLastPathSegment();
                int affected = mDatabase.delete(MemoDBHelper.TABLE_NAME,
                        MemoDBHelper._ID + "=" + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"),
                        selectionArgs);

                Context context = getContext();
                if (context != null) context.getContentResolver().notifyChange(uri, null);
                return affected;

            default:
                throw new IllegalArgumentException("invalid uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (!checkSignaturePermission()) throw new SecurityException();
        if (!validateInput(values)) throw new IllegalArgumentException();

        int match = sMatcher.match(uri);
        switch (match) {
            case URI_MATCH_MEMO_LIST:
                return mDatabase.update(MemoDBHelper.TABLE_NAME, values, selection, selectionArgs);

            case URI_MATCH_MEMO_ITEM:
                String id = uri.getLastPathSegment();
                int affected = mDatabase.update(MemoDBHelper.TABLE_NAME,
                        values,
                        MemoDBHelper._ID + "=" + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"),
                        selectionArgs);

                Context context = getContext();
                if (context != null) context.getContentResolver().notifyChange(uri, null);
                return affected;

            default:
                throw new IllegalArgumentException("invalid uri: " + uri);
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (!TextUtils.isEmpty(mode) && mode.contains("w") && !checkSignaturePermission())
            throw new SecurityException();

        int match = sMatcher.match(uri);
        if (match == URI_MATCH_MEMO_ITEM) return openFileHelper(uri, mode);

        throw new IllegalArgumentException("invalid uri: " + uri);
    }

    private boolean checkSignaturePermission() {
        int myPid = android.os.Process.myPid();
        int callingPid = Binder.getCallingPid();

        if (myPid == callingPid) return true;

        Context context = getContext();
        if (context == null) return false;

        PackageManager packagemanager = context.getPackageManager();
        String myPackage = context.getPackageName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String callingPackage = getCallingPackage();
            return packagemanager.checkSignatures(myPackage, callingPackage) == PackageManager.SIGNATURE_MATCH;
        }

        ActivityManager activitymanager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        HashSet<String> callerPackages = new HashSet<>();

        //executing process list
        List<ActivityManager.RunningAppProcessInfo> processes = activitymanager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            if (processInfo.pid == callingPid) {
                Collections.addAll(callerPackages, processInfo.pkgList);
            }
        }

        for (String packageName : callerPackages) {
            if (packagemanager.checkSignatures(myPackage, packageName) == PackageManager.SIGNATURE_MATCH)
                return true;
        }

        return false;
    }

    private boolean validateInput(ContentValues values) {
        return true;
    }
}
