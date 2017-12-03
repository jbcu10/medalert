package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Relative;

/**
 * Created by dev on 11/26/17.
 */

public class RelativeRepository extends SQLiteBaseHandler implements CrudRepository<Relative> {
    public RelativeRepository(Context context) {
        super(context);
    }

    @Override
    public boolean create(Relative relative) {
        try {
            Log.d(TAG, relative.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, relative.getUuid());
            values.put(KEY_FIRST_NAME, relative.getFirstName());
            values.put(KEY_MIDDLE_NAME, relative.getMiddleName());
            values.put(KEY_LAST_NAME, relative.getLastName());
            values.put(KEY_CONTACT_NUMBER, relative.getContactNumber());
            values.put(KEY_EMAIL, relative.getEmail());
            values.put(KEY_RELATIONSHIP, relative.getRelationship());

            long id = db.insert(TABLE_RELATIVE, null, values);
            db.close();
            Log.d(TAG, "NEW RELATIVE IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(Relative relative) {
        try {
            Log.d(TAG, relative.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, relative.getUuid());
            values.put(KEY_FIRST_NAME, relative.getFirstName());
            values.put(KEY_MIDDLE_NAME, relative.getMiddleName());
            values.put(KEY_LAST_NAME, relative.getLastName());
            values.put(KEY_CONTACT_NUMBER, relative.getContactNumber());
            values.put(KEY_EMAIL, relative.getEmail());
            values.put(KEY_RELATIONSHIP, relative.getRelationship());

            long id = db.update(TABLE_RELATIVE, values, KEY_ID + "= '" + relative.getId() + "'", null);
            db.close();
            Log.d(TAG, "NEW RELATIVE IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public List<Relative> getAll() {
        try {
            List<Relative> relatives = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_RELATIVE + " order by " + KEY_ID + " desc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Relative relative = new Relative();
                    relative.setId(cursor.getInt(0));
                    relative.setUuid(cursor.getString(1));
                    relative.setFirstName(cursor.getString(2));
                    relative.setMiddleName(cursor.getString(3));
                    relative.setLastName(cursor.getString(4));
                    relative.setContactNumber(cursor.getString(5));
                    relative.setEmail(cursor.getString(6));
                    relative.setRelationship(cursor.getString(7));
                    relatives.add(relative);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching relatives from database: " + relatives.get(0).getLastName());
            return relatives;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

    }

    @Override
    public boolean deleteById(int relativeId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_RELATIVE, KEY_ID + "= '" + relativeId + "'", null);
            db.close();
            Log.d(TAG, "Relative is deleted: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }
    }

    @Override
    public Relative getById(int relativeId) {
        try {
            Relative relative = new Relative();
            String selectQuery = "SELECT  * FROM " + TABLE_RELATIVE + " where id = '" + relativeId + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                relative.setId(cursor.getInt(0));
                relative.setUuid(cursor.getString(1));
                relative.setFirstName(cursor.getString(2));
                relative.setMiddleName(cursor.getString(3));
                relative.setLastName(cursor.getString(4));
                relative.setContactNumber(cursor.getString(5));
                relative.setEmail(cursor.getString(6));
                relative.setRelationship(cursor.getString(7));
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Medicine: " + relative.getFirstName());
            if (relative.getId() > 0) {
                return relative;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public Relative getByUuid(String uuid) {
        try {
            Relative relative = new Relative();
            String selectQuery = "SELECT  * FROM " + TABLE_RELATIVE + " where uuid = '" + uuid + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                relative.setId(cursor.getInt(0));
                relative.setUuid(cursor.getString(1));
                relative.setFirstName(cursor.getString(2));
                relative.setMiddleName(cursor.getString(3));
                relative.setLastName(cursor.getString(4));
                relative.setContactNumber(cursor.getString(5));
                relative.setEmail(cursor.getString(6));
                relative.setRelationship(cursor.getString(7));
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Medicine: " + relative.getFirstName());
            if (relative.getId() > 0) {
                return relative;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }
}
