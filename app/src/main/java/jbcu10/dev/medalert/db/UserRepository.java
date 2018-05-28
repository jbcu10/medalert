package jbcu10.dev.medalert.db;

/**
 * Created by dev on 12/16/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import jbcu10.dev.medalert.model.User;
public class UserRepository extends SQLiteBaseHandler implements CrudRepository<User> {
    public UserRepository(Context context) {
        super(context);
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getById(int id) {
        try {
            User user = new User();
            String selectQuery = "SELECT  * FROM " + TABLE_USER + " where id = '" + id + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                user.setId(cursor.getInt(0));
                user.setFirstName(cursor.getString(1));
                user.setLastName(cursor.getString(2));

                user.setEmail(cursor.getString(3));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching user: " + user.getFirstName());
            if (user.getId() > 0) {
                return user;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public User getByUuid(String uuid) {
        return null;
    }

    @Override
    public boolean create(User user) {
        try {
            Log.d(TAG, user.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
             values.put(KEY_FIRST_NAME, user.getFirstName());
             values.put(KEY_LAST_NAME, user.getLastName());
             values.put(KEY_EMAIL, user.getEmail());
           long id = db.insert(TABLE_USER, null, values);
            db.close();
            Log.d(TAG, "NEW USER IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            Log.d(TAG, user.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_FIRST_NAME, user.getFirstName());
            values.put(KEY_LAST_NAME, user.getLastName());
            values.put(KEY_EMAIL, user.getEmail());
            long id = db.update(TABLE_USER, values, KEY_ID + "= '" + user.getId() + "'", null);
            db.close();
            Log.d(TAG, "NEW TABLE_PATIENT IS UPDATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }    }

    @Override
    public boolean deleteById(int userId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_USER, KEY_ID + "= '" + userId + "'", null);
            db.close();
            Log.d(TAG, "Patient is deleted: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }    }
}
