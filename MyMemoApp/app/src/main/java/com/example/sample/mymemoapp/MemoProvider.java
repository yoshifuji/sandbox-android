package com.example.sample.mymemoapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by yoshitaka.fujisawa on 2017/07/05.
 */

public class MemoProvider extends ContentProvider {
    //<authority>
    private static final String AUTHORITY       = "com.example.sample.mymemoapp.memo";
    private static final String CONTENT_PATH    = "files";
    //MIME prefix
    public static final String MIME_DIR_PREFIX  = "vnd.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
    //set original MIME type
    public static final String MIME_ITEM            = "vnd.memoapp.memo";
    public static final String MIME_TYPE_MULTIPLE   = MIME_DIR_PREFIX + MIME_ITEM;
    public static final String MIME_TYPE_SINGLE     = MIME_ITEM_PREFIX + MIME_ITEM;

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
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
