package com.example.sample.mydatabasesample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String DB = "sqlite_sample.db";
    static final int DB_VERSION = 1;
    static final String CREATE_TABLE = "create table mytable ( _id integer primary key autoincrement, data integer not null );";
    static final String DROP_TABLE = "drop table mytable;";

    static SQLiteDatabase mydb;
    private SimpleCursorAdapter myadapter;

    private ListView listview;
    private Button addbtn, delbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        Cursor cursor = mydb.query("mytable", new String[]{"_id", "data"}, null, null, null, null, "_id DESC");
        String[] from = new String[]{"_id", "data"};
        int[] to = new int[]{R.id._id, R.id.data};
        myadapter = new SimpleCursorAdapter(this, R.layout.list_items, cursor, from, to);

        listview = (ListView) findViewById(R.id.ListView);
        listview.setAdapter(myadapter);

        addbtn = (Button) findViewById(R.id.Add);
        addbtn.setOnClickListener(this);
        delbtn = (Button) findViewById(R.id.Delete);
        delbtn.setOnClickListener(this);
    }

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

    public void onClick(View view) {
        if (view.getId() == R.id.Add) {
            ContentValues values = new ContentValues();
            values.put("data", "data");
            mydb.insert("mytable", null, values);

            Cursor cursor = mydb.query("mytable", new String[]{"_id", "data"}, null, null, null, null, "_id DESC");
            startManagingCursor(cursor);
            myadapter.changeCursor(cursor);
        } else if (view.getId() == R.id.Delete) {
            mydb.delete("mytable", "_id like '%'", null);

            Cursor cursor = mydb.query("mytable", new String[]{"_id", "data"}, null, null, null, null, "_id DESC");
            startManagingCursor(cursor);
            myadapter.changeCursor(cursor);
        }
    }
}


