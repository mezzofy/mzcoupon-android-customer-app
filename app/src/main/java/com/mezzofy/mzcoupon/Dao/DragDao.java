package com.mezzofy.mzcoupon.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by udhayinforios on 23/2/16.
 */
public class DragDao {

    private static final String TABLE_PROMO_HISTORY = "tbl_promo_history";
    private SQLiteDatabase mDB;
//    private GetsoDb database;

    public DragDao(Context context) {
//        database = new GetsoDb(context);
    }

//    public boolean addPromo(DrawRes res) {
//
//        boolean resval = false;
//        mDB = database.open();
//        String selectQuery = "SELECT * FROM  " + TABLE_PROMO_HISTORY + "  WHERE promoid=" + res.getPromoid();
//        Cursor cursor = mDB.rawQuery(selectQuery, null);
//        if (cursor.getCount() <= 0) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("type", res.getType());
//            contentValues.put("code", res.getCode());
//            contentValues.put("promoid", res.getPromoid());
//            contentValues.put("createdon", res.getCreatedon());
//            contentValues.put("prod_id", res.getId());
//            contentValues.put("title", res.getTitle());
//            contentValues.put("name", res.getName());
//            contentValues.put("desc", res.getDesc());
//            contentValues.put("image", res.getImage());
//            mDB.insert(TABLE_PROMO_HISTORY, null, contentValues);
//
//            resval = true;
//        } else {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("type", res.getType());
//            contentValues.put("code", res.getCode());
//            contentValues.put("promoid", res.getPromoid());
//            contentValues.put("createdon", res.getCreatedon());
//            contentValues.put("prod_id", res.getId());
//            contentValues.put("title", res.getTitle());
//            contentValues.put("name", res.getName());
//            contentValues.put("desc", res.getDesc());
//            contentValues.put("image", res.getImage());
//            mDB.update(TABLE_PROMO_HISTORY, contentValues, " promoid=" + res.getPromoid(), null);
//
//            resval = false;
//        }
//        cursor.close();
//        mDB.close();
//        return resval;
//    }
//
//    public PromoHistoryRes getPromocode(String promoid) {
//
//        mDB = database.open();
//        PromoHistoryRes tempres = new PromoHistoryRes();
//
//        String selectQuery = "select *  FROM  " + TABLE_PROMO_HISTORY + " WHERE promoid ='" + promoid + "'";
//        Cursor cursor = mDB.rawQuery(selectQuery, null);
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//
//                tempres.setType(cursor.getString(1));
//                tempres.setCode(cursor.getString(2));
//                tempres.setPromoid(cursor.getString(3));
//                tempres.setCreatedon(cursor.getString(4));
//            }
//        }
//        cursor.close();
//        mDB.close();
//        return tempres;
//    }
//
//
//    public ArrayList<DrawRes> getPromocodeList() {
//
//        mDB = database.open();
//
//        ArrayList<DrawRes> resList = new ArrayList<>();
//        String selectQuery = "select *  FROM  " + TABLE_PROMO_HISTORY ;
//        Cursor cursor = mDB.rawQuery(selectQuery, null);
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    DrawRes tempres = new DrawRes();
//                    tempres.setType(cursor.getString(1));
//                    tempres.setCode(cursor.getString(2));
//                    tempres.setPromoid(cursor.getString(3));
//                    tempres.setCreatedon(cursor.getString(4));
//                    tempres.setId(cursor.getInt(5));
//                    tempres.setTitle(cursor.getString(6));
//                    tempres.setName(cursor.getString(7));
//                    tempres.setDesc(cursor.getString(8));
//                    tempres.setImage(cursor.getString(9));
//                    resList.add(tempres);
//                }while(cursor.moveToNext());
//            }
//        }
//        cursor.close();
//        mDB.close();
//        return resList;
//    }
//    public void deletepromocode(String id) {
//
//        mDB = database.open();
//
//        String selectQuery = "delete  from " + TABLE_PROMO_HISTORY +" WHERE promoid='"+ id +"'";
//        mDB.execSQL(selectQuery);
//
//        mDB.close();
//
//    }


}
