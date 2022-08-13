package com.jdd.android.jdd.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.jdd.android.jdd.entities.StockEntity;
import com.jdd.android.jdd.utils.DbUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：自选股数据库管理类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-12-14
 * @last_edit 2015-12-26
 * @since 1.0
 */
public class SelfSelectionStocksManager {
    private SelfSelectionStocksDBHelper mDBHelper;
    private static SelfSelectionStocksManager sManager;

    private SelfSelectionStocksManager(Context context) {
        mDBHelper = new SelfSelectionStocksDBHelper(context);
    }

    public static SelfSelectionStocksManager getInstance(Context context) {
        if (null == sManager) {
            sManager = new SelfSelectionStocksManager(context);
        }

        return sManager;
    }

    /**
     * 查询userId用户的所有自选股
     *
     * @param userId 用户id
     * @return userId用户的所有自选股
     */
    public List<StockEntity> queryUserStocks(long userId) {
        List<StockEntity> entities = new ArrayList<>();
        StockEntity entity = null;

        Cursor cursor = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // 开始事务
        db.beginTransaction();
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM t_zxg where _user_id=? ORDER BY  _order ASC",
                    new String[]{String.valueOf(userId)});
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    entity = new StockEntity();

                    entity.setName(DbUtil.getStringByName(cursor, "_name"));
                    entity.setCode(DbUtil.getStringByName(cursor, "_code"));
                    entity.setMarket(DbUtil.getStringByName(cursor, "_market"));
                    entity.setNow(DbUtil.getDoubleByName(cursor, "_now"));
                    entity.setChangePercent((float) DbUtil.getDoubleByName(cursor, "_change_percent"));
                    entity.setChangeValue(DbUtil.getDoubleByName(cursor, "_change_value"));
                    entity.setOrder(DbUtil.getIntByName(cursor, "_order"));
                    entity.setOpen(DbUtil.getDoubleByName(cursor, "_open"));
                    entity.setHigh(DbUtil.getDoubleByName(cursor, "_high"));
                    entity.setUserId(DbUtil.getIntByName(cursor, "_user_id"));
                    entity.setPreClose(DbUtil.getDoubleByName(cursor, "_pre_close"));

                    entities.add(entity);
                } while (cursor.moveToNext());
            }
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            db.endTransaction();
        }
        return entities;
    }

    /**
     * 查询该自选股是否存在
     *
     * @param data 股票对象
     * @return
     */
    public StockEntity query(StockEntity data) {
        StockEntity entity = null;

        Cursor cursor = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        try {
            db.beginTransaction();
            cursor = db.rawQuery(
                    "SELECT * FROM t_zxg WHERE _user_id=? AND _market=? AND _code=?"
                    , new String[]{String.valueOf(data.getUserId()), data.getMarket(), data.getCode()});
            if (null != cursor && cursor.moveToFirst()) {
                entity = new StockEntity();

                entity.setName(DbUtil.getStringByName(cursor, "_name"));
                entity.setCode(DbUtil.getStringByName(cursor, "_code"));
                entity.setMarket(DbUtil.getStringByName(cursor, "_market"));
                entity.setNow(DbUtil.getDoubleByName(cursor, "_now"));
                entity.setChangePercent((float) DbUtil.getDoubleByName(cursor, "_change_percent"));
                entity.setChangeValue(DbUtil.getDoubleByName(cursor, "_change_value"));
                entity.setOrder(DbUtil.getIntByName(cursor, "_order"));
                entity.setOpen(DbUtil.getDoubleByName(cursor, "_open"));
                entity.setHigh(DbUtil.getDoubleByName(cursor, "_high"));
                entity.setUserId(DbUtil.getIntByName(cursor, "_user_id"));
                entity.setPreClose(DbUtil.getDoubleByName(cursor, "_pre_close"));
            }
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            db.endTransaction();
        }
        return entity;
    }

    /**
     * 查询自选股数量
     *
     * @return
     */
    public int count() {
        int count = 0;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // 开始事务
        db.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM t_zxg", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            db.endTransaction();
        }

        return count;
    }

    public void insert(StockEntity entity) {
        if (null == entity) {
            return;
        }
        //如果已经存在，就不添加
        if (null != query(entity)) {
            return;
        }
        //设置排序值为最后
        entity.setOrder(count() + 1);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // 开始事务
        db.beginTransaction();
        try {
            db.execSQL(
                    "INSERT INTO t_zxg(_name, _market, _code, _change_percent, _change_value, _order, _category, _now,  _open, _high, _pre_close, _user_id) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            entity.getName(), entity.getMarket(), entity.getCode(),
                            entity.getChangePercent(), entity.getChangeValue(),
                            entity.getOrder(), entity.getCategory(), entity.getNow(),
                            entity.getOpen(), entity.getHigh(), entity.getPreClose(),
                            entity.getUserId()
                    });
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insert(List<StockEntity> stockEntities) {
        for (StockEntity entity : stockEntities) {
            insert(entity);
        }
    }

    /**
     * 删除用户的所有自选股
     *
     * @param userId
     */
    public void delete(long userId) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // 开始事务
        db.beginTransaction();
        try {
            db.execSQL(
                    "DELETE FROM t_zxg WHERE _user_id=?",
                    new Object[]{userId});
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void delete(long userId, String market, String code) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(
                    "DELETE FROM t_zxg WHERE _user_id=? AND _market=? AND _code=?",
                    new Object[]{userId, market, code});
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void delete(StockEntity entity) {
        if (null != entity) {
            delete(entity.getUserId(), entity.getMarket(), entity.getCode());
        }
    }

    public void update(StockEntity entity) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(
                    "UPDATE t_zxg SET _now=?, _change_percent=?, _change_value=?, _order=?, _open=?, _high=?, _category=?  " +
                            "WHERE _user_id=? AND _market=? AND _code=? AND _pre_close=? ",
                    new Object[]{
                            entity.getNow(), entity.getChangePercent(), entity.getChangeValue(),
                            entity.getOrder(), entity.getOpen(), entity.getHigh(), entity.getCategory(),
                            entity.getUserId(), entity.getMarket(), entity.getCode(), entity.getPreClose()
                    });
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void update(List<StockEntity> source) {
        if (null == source) {
            return;
        }
        for (StockEntity entity : source) {
            update(entity);
        }
    }

    public void updateOrder(long userId, String market, String code, String order) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        // 开始事务
        db.beginTransaction();
        try {
            db.execSQL(
                    "UPDATE t_zxg SET _order=? WHERE _user_id=? AND _market=? AND _code=?",
                    new Object[]{order, userId, market, code});
            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // db.close();
        }
    }

    /**
     * 清除表数据，使用删除表并重新建表的方法
     */
    public void clearTableData() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        // 开始事务
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS t_zxg");
            mDBHelper.onCreate(db);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
