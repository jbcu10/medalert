package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Instructions;

/**
 * Created by dev on 11/26/17.
 */

public class FirstAidRepository extends SQLiteBaseHandler implements CrudRepository<FirstAid> {
    public FirstAidRepository(Context context) {
        super(context);
    }

    private List<Instructions> getInstructions(String firstAidUuid) {
        try {
            List<Instructions> instructionsList = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_INSTRUCTIONS + " where " + KEY_FIRST_AID_UUID + "='" + firstAidUuid + "' order by " + KEY_ID + " asc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Instructions instructions = new Instructions();
                    instructions.setId(cursor.getInt(0));
                    instructions.setUuid(cursor.getString(2));
                    instructions.setInstruction(cursor.getString(3));
                    instructionsList.add(instructions);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching medicines from database: " + instructionsList.get(0).getInstruction());
            return instructionsList;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

    }

    public boolean createInstruction(String firstAidUuid, Instructions instructions) {
        try {
            Log.d(TAG, instructions.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, instructions.getUuid());
            values.put(KEY_INSTRUCTION, instructions.getInstruction());
            values.put(KEY_FIRST_AID_UUID, firstAidUuid);
            long id = db.insert(TABLE_INSTRUCTIONS, null, values);
            db.close();
            Log.d(TAG, "NEW INSTRUCTION IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }


    @Override
    public List<FirstAid> getAll() {
        try {
            List<FirstAid> firstAids = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_FIRST_AID + " order by " + KEY_NAME + " asc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    FirstAid firstAid = new FirstAid();
                    firstAid.setId(cursor.getInt(0));
                    firstAid.setUuid(cursor.getString(1));
                    firstAid.setName(cursor.getString(2));
                    firstAid.setDescription(cursor.getString(3));
                    firstAids.add(firstAid);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching first aid from database: " + firstAids.get(0).getName().toString());
            return firstAids;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }
    }

    @Override
    public FirstAid getById(int id) {
        try {
            FirstAid firstAid = new FirstAid();
            String selectQuery = "SELECT  * FROM " + TABLE_FIRST_AID + " where id = '" + id + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                firstAid.setId(cursor.getInt(0));
                String uuid = cursor.getString(1);
                firstAid.setUuid(uuid);
                firstAid.setName(cursor.getString(2));
                firstAid.setDescription(cursor.getString(3));
                firstAid.setInstructionsList(getInstructions(uuid));
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching first aid: " + firstAid.getName());
            if (firstAid.getId() > 0) {
                return firstAid;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public FirstAid getByUuid(String uuid) {
        return null;
    }

    @Override
    public boolean create(FirstAid firstAid) {
        try {
            Log.d(TAG, firstAid.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, firstAid.getUuid());
            values.put(KEY_NAME, firstAid.getName());
            values.put(KEY_DESCRIPTION, firstAid.getDescription());
            long id = db.insert(TABLE_FIRST_AID, null, values);
            db.close();
            Log.d(TAG, "NEW FIRST AID IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(FirstAid firstAid) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
