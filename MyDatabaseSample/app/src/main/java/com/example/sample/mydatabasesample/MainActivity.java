package com.example.sample.mydatabasesample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    static final int DB_VERSION = 1;
    //DB名
    static final String DB = "sqlite_sample.db";
    //TABLE定義
    static final String CREATE_TABLE = "create table mytable (" +
            "_id integer primary key autoincrement, " +
            "user text not null, " +
            "comment text" +
            ");";
    static final String DROP_TABLE = "drop table mytable;";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    Inner class for local database
    [Refer]https://techbooster.org/android/application/567/
     */
    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context c) {
            super(c, DB, null, DB_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}


