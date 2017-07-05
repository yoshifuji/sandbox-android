package com.example.sample.mymemoapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by yoshitaka.fujisawa on 2017/07/05.
 */

public class MemoRepository {
    //file format: prefix-yyyy-mm-dd-HH-MM-SS.txt
    private static final String MEMO_FILE_FORMAT = "%l$s-%2$tF-%2$tH-%2$tM-%2$tS.txt";

    //save as file to database
    public static Uri store(Context context, String memo){
        File outptDir;

        if(Build.VERSION.SDK_INT >= 19){
            //Use shared directory over api level 19
            outptDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            outptDir = new File(context.getExternalFilesDir(null), "Documents");
        }

        if(outptDir == null) return null;

        boolean hasDirectory = true;
        if(!outptDir.exists() || ! outptDir.isDirectory()){
            hasDirectory = outptDir.mkdirs();
        }
        if(!hasDirectory) return null;

        //save file
        File outputFile = saveAsFile(context, outptDir, memo);
        //save failure
        if(outputFile == null) return null;

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

    private static File saveAsFile(Context context, File outptDir, String memo) {
        String fileNamePrefix = SettingPrefUtil.getFileNamePrefix(context);
        Calendar now = Calendar.getInstance();
        String fileName = String.format(MEMO_FILE_FORMAT, fileNamePrefix, now);

        File outputFile = new File(outptDir, fileName);
        FileWriter writer = null;
        try{
            writer = new FileWriter(outputFile);
            writer.write(memo);
            writer.flush();
        } catch(IOException e){
            e.printStackTrace();
            return null;
        } finally {
            if (writer != null){
                try{
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return outputFile;
    }

}
