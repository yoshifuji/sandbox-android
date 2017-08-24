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
    private final static String DB_TABLE = "speech";    // DBのテーブル名
    private final static int DB_VERSION = 1;            // DBのバージョン

    /**
     * DBのカラム名
     */
    public final static String COL_ID = "_id";             // id
    public final static String COL_PRODUCT = "product";    // 品名
    public final static String COL_MADEIN  = "madein";     // 産地
    public final static String COL_NUMBER  = "number";     // 個数
    public final static String COL_PRICE   = "price";      // 単価

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
     * @param product 品名
     * @param madein  産地
     * @param number  個数
     * @param price   単価
     */
    public void saveDB(String product, String madein, int number, int price) {

        db.beginTransaction(); // トランザクション開始

        try {
            ContentValues values = new ContentValues(); // ContentValuesでデータを設定していく
            values.put(COL_PRODUCT, product);
            values.put(COL_MADEIN, madein);
            values.put(COL_NUMBER, number);
            values.put(COL_PRICE, price);

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
     * DBのデータを取得
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE, columns, null, null, null, null, null);
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
                    + COL_PRODUCT + " TEXT NOT NULL,"
                    + COL_MADEIN + " TEXT NOT NULL,"
                    + COL_NUMBER + " INTEGER NOT NULL,"
                    + COL_PRICE + " INTEGER NOT NULL"
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
         * onCreate()
         *
         * @param db SQLiteDatabase
         */
        public void onDrop(SQLiteDatabase db) {
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE);
        }
    }

}
