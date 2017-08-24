package com.example.sample.voicerecognition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YoshitakaFujisawa on 2017/08/23.
 */

public class DBAdapter {

    private final static String DB_NAME = "rokuon.db";  // DB名
    private final static String DB_TABLE = "rokuon";    // DBのテーブル名
    private final static int DB_VERSION = 1;            // DBのバージョン

    /**
     * DBのカラム名
     */
    public final static String COL_ID       = "_id";      // id
    public final static String COL_GID      = "group_id"; // グループID
    public final static String COL_USER     = "user";     // ユーザー名
    public final static String COL_ROKUON   = "rokuon";   // 録音

    private SQLiteDatabase db = null; // SQLiteDatabase
    private DBHelper dbHelper = null; // DBHepler
    protected Context context;        // Context

    // コンストラクタ
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DB Open
     */
    public DBAdapter openDB() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * DB Close
     */
    public void closeDB() {
        db.close();
        db = null;
    }

    /**
     * DBのレコードへ登録
     *
     * @param gid
     * @param user
     * @param rokuon
     */
    public void saveRokuon(int gid, String user, String rokuon) {

        db.beginTransaction(); // トランザクション開始

        try {
            ContentValues values = new ContentValues(); // ContentValuesでデータを設定していく
            values.put(COL_GID, gid);
            values.put(COL_USER, user);
            values.put(COL_ROKUON, rokuon);

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE, null, values); // レコードへ登録

            db.setTransactionSuccessful(); // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // トランザクションの終了
        }
    }

    /**
     * DBの録音データを取得
     *
     * @return DBデータ
     */
    public Cursor getLatestRokuonGroup() {

        String query = "SELECT orgin.content FROM "+ DB_TABLE +" orgin"
                + " INNER JOIN (SELECT MAX(COL_GID) COL_GID FROM "+ DB_TABLE +") temp"
                + " ON orgin.COL_GID = temp.COL_GID";

        return db.rawQuery(query, null);
    }

    /**
     * DBのグループID最大値を取得
     *
     * @return DBデータ
     */
    public Cursor getMaxGroupID() {

        String query = "SELECT MAX(COL_GID) COL_GID FROM " + DB_TABLE;

        return db.rawQuery(query, null);
    }

    /**
     * データベースの生成やアップグレードを管理するSQLiteOpenHelperを継承したクラス
     */
    private static class DBHelper extends SQLiteOpenHelper {

        // コンストラクタ
        public DBHelper(Context context) {
            //第1引数：コンテキスト
            //第2引数：DB名
            //第3引数：factory nullでよい
            //第4引数：DBのバージョン
            super(context, DB_NAME, null, DB_VERSION);
        }

        /**
         * DB生成時に呼ばれる
         * onCreate()
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            String createTbl = "CREATE TABLE " + DB_TABLE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_GID + " INTEGER NOT NULL,"
                    + COL_USER + " TEXT NOT NULL,"
                    + COL_ROKUON + " TEXT NOT NULL"
                    + ");";

            db.execSQL(createTbl);      //SQL文の実行
        }

        /**
         * DBアップグレード時に呼ばれる
         *
         * @param db         SQLiteDatabase
         * @param oldVersion int 古いバージョン
         * @param newVersion int 新しいバージョン
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE);
            // テーブル生成
            onCreate(db);
        }

        /**
         * DB破棄時に呼ばれる
         * onDrop()
         *
         * @param db SQLiteDatabase
         */
        public void onDrop(SQLiteDatabase db) {
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE);
        }
    }

}
