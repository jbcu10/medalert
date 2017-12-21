package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Store;

/**
 * Created by dev on 12/14/17.
 */

public class StoreRepository extends SQLiteBaseHandler implements CrudRepository<Store> {
    public StoreRepository(Context context) {
        super(context);
    }

    @Override
    public List<Store> getAll() {
        try {
            List<Store> stores = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_STORE + " order by " + KEY_ID + " desc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    Store store = new Store();
                    store.setId(cursor.getInt(0));
                    store.setUuid(cursor.getString(1));
                    store.setName(cursor.getString(2));
                    store.setAddress(cursor.getString(3));
                    store.setLon(cursor.getDouble(4));
                    store.setLon(cursor.getDouble(5));

                    stores.add(store);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Store from database: " + stores.get(0).getName());
            return stores;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }
    }

    @Override
    public Store getById(int id) {
        try {
            Store store = new Store();
            String selectQuery = "SELECT  * FROM " + TABLE_STORE + " where id = '" + id + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                store.setId(cursor.getInt(0));
                store.setUuid(cursor.getString(1));
                store.setName(cursor.getString(2));
                store.setAddress(cursor.getString(3));
                store.setLon(cursor.getDouble(4));
                store.setLon(cursor.getDouble(5));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Store: " + store.getName());
            if (store.getId() > 0) {
                return store;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public Store getByUuid(String uuid) {
        try {
            Store store = new Store();
            String selectQuery = "SELECT  * FROM " + TABLE_STORE + " where uuid = '" + uuid + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                store.setId(cursor.getInt(0));
                store.setUuid(cursor.getString(1));
                store.setName(cursor.getString(2));
                store.setAddress(cursor.getString(3));
                store.setLon(cursor.getDouble(4));
                store.setLon(cursor.getDouble(5));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Store: " + store.getName());
            if (store.getId() > 0) {
                return store;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Store store) {
        try {
            Log.d(TAG, store.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, store.getUuid());
            values.put(KEY_NAME, store.getName());
            values.put(KEY_ADDRESS, store.getAddress());
            values.put(KEY_LON, store.getLon());
            values.put(KEY_LAT, store.getLat());

            long id = db.insert(TABLE_STORE, null, values);
            db.close();
            Log.d(TAG, "NEW STORE IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(Store store) {
        try {
            Log.d(TAG, store.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, store.getUuid());
            values.put(KEY_NAME, store.getName());
            values.put(KEY_ADDRESS, store.getAddress());
            values.put(KEY_LON, store.getLon());
            values.put(KEY_LAT, store.getLat());
            long id = db.update(TABLE_STORE, values, KEY_ID + "= '" + store.getId() + "'", null);
            db.close();
            Log.d(TAG, "NEW TABLE_STORE IS UPDATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return     false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
