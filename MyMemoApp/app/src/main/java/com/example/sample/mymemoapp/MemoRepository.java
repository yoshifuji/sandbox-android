package com.example.sample.mymemoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by yoshitaka.fujisawa on 2017/07/05.
 */

public class MemoRepository {
    //file format: prefix-yyyy-mm-dd-HH-MM-SS.txt
    private static final String MEMO_FILE_FORMAT = "%l$s-%2$tF-%2$tH-%2$tM-%2$tS.txt";

    private MemoRepository() {
    }

    ;

    //save as file to database
    public static Uri store(Context context, String memo) {
        File outputDir;

        if (Build.VERSION.SDK_INT >= 19) {
            //Use shared directory over api level 19
            outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            outputDir = new File(context.getExternalFilesDir(null), "Documents");
        }

        if (outputDir == null) return null;

        boolean hasDirectory = true;
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            hasDirectory = outputDir.mkdirs();
        }
        if (!hasDirectory) return null;

        //save file
        File outputFile = saveAsFile(context, outputDir, memo);
        //save failure
        if (outputFile == null) return null;

        //prepare params
        String title = memo.length() > 10 ? memo.substring(0, 10) : memo;
        ContentValues values = new ContentValues();
        values.put(MemoDBHelper.TITLE, title);
        values.put(MemoDBHelper.DATA, outputFile.getAbsolutePath());
        values.put(MemoDBHelper.DATE_ADDED, System.currentTimeMillis());

        //save to ContentProvider
        return context.getContentResolver().insert(
                MemoProvider.CONTENT_URI, values
        );
    }

    //Input memo
    public static String findMemoByUri(Context context, Uri uri) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return context.getString(R.string.error_memo_file_not_found);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return context.getString(R.string.error_memo_file_load_failed);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString();
    }

    //Retrieve memo list
    public static Cursor query(Context context) {
        return context.getContentResolver().query(
                MemoProvider.CONTENT_URI,
                null, null, null, MemoDBHelper.DATE_MODIFIED + " DESC");
    }

    private static File saveAsFile(Context context, File outptDir, String memo) {
        String fileNamePrefix = SettingPrefUtil.getFileNamePrefix(context);
        Calendar now = Calendar.getInstance();
        String fileName = String.format(MEMO_FILE_FORMAT, fileNamePrefix, now);

        File outputFile = new File(outptDir, fileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
            writer.write(memo);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return outputFile;
    }

    private static File getOutputDir(Context context) {
        File outputDir;

        if (Build.VERSION.SDK_INT >= 19) {
            //Use shared directory over api level 19
            outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            outputDir = new File(context.getExternalFilesDir(null), "Documents");
        }
        if (outputDir == null) return null;

        boolean isExist = true;
        if (!outputDir.exists() || !outputDir.isDirectory()) isExist = outputDir.mkdir();
        if (isExist) {
            return outputDir;
        } else {
            return null;
        }
    }

    private static File getFileName(Context context, File outputDir) {
        String fileNamePrefix = SettingPrefUtil.getFileNamePrefix(context);
        Calendar now = Calendar.getInstance();
        String fileName = String.format(MEMO_FILE_FORMAT, fileNamePrefix, now);

        return new File(outputDir, fileName);
    }

    private static boolean writeToFile(File outputFIle, String memo) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(outputFIle);
            writer.write(memo);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
    
    private static Uri create(Context context, String memo) {
        File outputDir = getOutputDir(context);
        if (outputDir == null) return null;

        File outputFile = getFileName(context, outputDir);
        if (outputFile == null || writeToFile(outputFile, memo)) return null;

        //Trim title over 10 chars
        String title = memo.length() > 10 ? memo.substring(0, 10) : memo;

        ContentValues values = new ContentValues();
        values.put(MemoDBHelper.TITLE, title);
        values.put(MemoDBHelper.DATA, outputFile.getAbsolutePath());
        values.put(MemoDBHelper.DATE_ADDED, System.currentTimeMillis());

        return context.getContentResolver().insert(MemoProvider.CONTENT_URI, values);
    }

    private static int update(Context context, Uri uri, String memo) {
        String id = uri.getLastPathSegment();

        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{MemoDBHelper.DATA},
                MemoDBHelper._ID + " = ?",
                new String[]{id}, null);

        if (cursor == null) return 0;

        String filePath = null;
        while (cursor.moveToNext()) {
            filePath = cursor.getString(cursor.getColumnIndex(MemoDBHelper.DATA));
        }
        cursor.close();

        if (TextUtils.isEmpty(filePath)) return 0;

        File outputFile = new File(filePath);
        if (writeToFile(outputFile, memo)) return 0;

        return 1;
    }

}
