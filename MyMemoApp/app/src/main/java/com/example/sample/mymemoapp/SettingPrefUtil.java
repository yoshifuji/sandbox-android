package com.example.sample.mymemoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import java.util.Collections;
import java.util.Set;

/**
 * Created by yoshitaka.fujisawa on 2017/07/05.
 */
public class SettingPrefUtil {
    public static final String PREF_FILE_NAME = "settings"; //file name to save
    private static final String KEY_FILE_NAME_PREFIX = "file.name.prefix";
    private static final String KEY_FILE_NAME_PREFIX_DEFAULT = "memo"; //default

    private static final String KEY_TEXT_SIZE    = "text.size";
    private static final String TEXT_SIZE_LARGE  = "text.size.large";
    private static final String TEXT_SIZE_MEDIUM = "text.size.medium";
    private static final String TEXT_SIZE_SMALL  = "text.size.small";

    private static final String KEY_TEXT_STYLE     = "text.style";
    private static final String TEXT_STYLE_BOLD    = "text.style.bold";
    private static final String TEXT_STYLE_ITALIC  = "text.style.italic";
    private static final String KEY_SCREEN_REVERSE = "screen.reverse";

    public static String getFileNamePrefix(Context context){
        //Retrieve SharedPreference
        SharedPreferences sp = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        //Retrieve SharedPreference Settings
        return sp.getString(KEY_FILE_NAME_PREFIX, KEY_FILE_NAME_PREFIX_DEFAULT);
    }

    //Retrieve font size
    public static float getFontSize(Context context){
        //Retrieve SharedPreference
        SharedPreferences sp = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        //current setting
        String storedSize = sp.getString(KEY_TEXT_SIZE, TEXT_SIZE_MEDIUM);

        switch(storedSize){
            case TEXT_SIZE_LARGE:
                return context.getResources().getDimension(R.dimen.settings_text_size_large);
            case TEXT_SIZE_MEDIUM:
                return context.getResources().getDimension(R.dimen.settings_text_size_medium);
            case TEXT_SIZE_SMALL:
                return context.getResources().getDimension(R.dimen.settings_text_size_small);
            default:
                return context.getResources().getDimension(R.dimen.settings_text_size_medium);
        }
    }

    //Retrieve layout settings
    public static int getTypeface(Context context){
        //Retrieve SharedPreference
        SharedPreferences sp = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Set<String> storedTypeface = sp.getStringSet(KEY_TEXT_STYLE, Collections.<String>emptySet());

        //transform param to bit flag
        int typefaceBit = Typeface.NORMAL;
        for(String value : storedTypeface){
            switch (value){
                case TEXT_STYLE_ITALIC:
                    typefaceBit |= Typeface.ITALIC;
                    break;
                case TEXT_STYLE_BOLD:
                    typefaceBit |= Typeface.BOLD;
                    break;
            }
        }

        return typefaceBit;
    }

    //whether or not to reverse window
    public static boolean isScreenReverse(Context context){
        SharedPreferences sp = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        return sp.getBoolean(KEY_SCREEN_REVERSE, false);
    }

    //save file name prefix
    public static void storeFontSize(Context context, String prefix){
        SharedPreferences sp = context.getSharedPreferences("sample", Context.MODE_PRIVATE);

        //retrieve editor
        SharedPreferences.Editor editor = sp.edit();
        //edit SharedPreferences
        editor.putString(KEY_FILE_NAME_PREFIX, prefix);
        //reflect content
        editor.apply();
    }
}
