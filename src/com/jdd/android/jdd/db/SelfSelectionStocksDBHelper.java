package com.jdd.android.jdd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * 描述：自选股数据库辅助类
 * </p>
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 15-9-28
 */
public class SelfSelectionStocksDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SelfSelectionStocks.db";// 数据库名称
    private static final int DATABASE_VERSION = 1;// 数据库版本

    public SelfSelectionStocksDBHelper(Context context, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SelfSelectionStocksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /**
     * 该方法只调用一次 就是在数据库被创建的时候<br>
     * 可以完成数据库表的创建<br>
     * 可以通过擦参数SQLiteDatabase的对象 执行sql语句
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE t_zxg(	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "_name VARCHAR(20)," +
                "_market VARCHAR(20)," +
                "_code VARCHAR(20)," +
                "_now DOUBLE," +
                "_change_value DOUBLE," +
                "_change_percent DOUBLE," +
                "_order INTEGER," +
                "_category VARCHAR(20)," +
                "_open DOUBLE," +
                "_high DOUBLE," +
                "_pre_close DOUBLE," +
                "_user_id INTEGER," +
                "_market_value DOUBLE)");// 执行有更新行为的sql,比如创建,修改,删除
    }

    @Override
    /**
     * 更新数据库版本的时候调用 可以对数据表结构调整 基本信息添加等
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS t_zxg");// 真正项目中需要考虑数据的重要性
        // 尽量少对数据库做删除
        onCreate(db);
    }
}
