package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Token;

/**
 * Created by dev on 12/16/17.
 */

public class TokenRepository extends SQLiteBaseHandler implements CrudRepository<Token> {
    public TokenRepository(Context context) {
        super(context);
    }

    @Override
    public List<Token> getAll() {
        try {
            List<Token> tokens = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_TOKEN +" order by " + KEY_ID + " asc";

            Log.d(TAG, "selectQuery: " + selectQuery);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Log.d(TAG, "get medicines uuid: " + cursor.getString(1));

                    Token token = new Token();
                     token.setId(cursor.getInt(0));
                     token.setToken(cursor.getString(1));
                    tokens.add(token);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching tokens from database: " + tokens.get(0).getToken());
            return tokens;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }    }

    @Override
    public Token getById(int id) {
        try {
            Token token = new Token();
            String selectQuery = "SELECT  * FROM " + TABLE_PATIENT + " where id = '" + id + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                token.setId(cursor.getInt(0));
                token.setToken(cursor.getString(1));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Token: " + token.getToken());
            if (token.getId() > 0) {
                return token;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public Token getByUuid(String uuid) {
        return null;
    }

    @Override
    public boolean create(Token token) {
        try {
            deleteById(1);
            Log.d(TAG, token.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
             values.put(KEY_TOKEN, token.getToken());
            long id = db.insert(TABLE_TOKEN, null, values);
            db.close();
            Log.d(TAG, "NEW TOKEN IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(Token token) {
        return false;
    }

    @Override
    public boolean deleteById(int tokenId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_TOKEN, null, null);
            db.close();
            Log.d(TAG, "TOKEN is deleted: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }
    }
}
