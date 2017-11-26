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
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Relative;
import jbcu10.dev.medalert.model.Reminder;


/**
 * Created by dev on 10/1/17.
 */

public class DatabaseCRUDHandler extends SQLiteBaseHandler {

    public DatabaseCRUDHandler(Context context) {
        super(context);
    }


    public List<Reminder> getAllReminders() {
        try {
            List<Reminder> reminders = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_REMINDER + " order by " + KEY_ID + " desc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Reminder reminder = new Reminder();
                    reminder.setId(cursor.getInt(0));
                    reminder.setUuid(cursor.getString(1));
                    reminder.setDescription(cursor.getString(2));
                    reminders.add(reminder);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching reminders from database: " + reminders.get(0).getDescription());
            return reminders;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

    }
    public List<FirstAid> getAllFirstAid() {
        try {
            List<FirstAid> firstAids = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_FIRST_AID + " order by " + KEY_ID + " desc";
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
            Log.d(TAG, "Fetching medicines from database: " + firstAids.get(0).getName().toString());
            return firstAids;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

    }
    private List<Instructions> getInstructions(String firstAidUuid) {
        try {
            List<Instructions> instructionsList = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_INSTRUCTIONS + " where "+KEY_FIRST_AID_UUID+"='"+firstAidUuid+"' order by " + KEY_ID + " asc";
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



    public boolean createRelative(Relative relative) {
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

    public boolean createFirstAid(FirstAid firstAid) {
        try {
            Log.d(TAG, firstAid.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, firstAid.getUuid());
            values.put(KEY_NAME, firstAid.getName());
            values.put(KEY_DESCRIPTION, firstAid.getDescription());
            long id = db.insert(TABLE_FIRST_AID, null, values);
            db.close();
            Log.d(TAG, "NEW RELATIVE IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
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
    public boolean createReminder(Reminder reminder) {
        try {
            Log.d(TAG, reminder.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, reminder.getUuid());
            values.put(KEY_DESCRIPTION, reminder.getDescription());

            long id = db.insert(TABLE_REMINDER, null, values);
            if(reminder.getMedicineList()!=null) {
                for (Medicine medicine : reminder.getMedicineList()) {
                    this.createReminderMedicine(reminder.getUuid(),medicine.getUuid());
                }
            }
            db.close();

            if(reminder.getMedicineList()!=null) {
                for (Medicine medicine : reminder.getMedicineList()) {
                    this.createReminderMedicine(reminder.getUuid(),medicine.getUuid());
                }
            }if(reminder.getTime()!=null) {
                for (String time: reminder.getTime()) {
                    this.createReminderTime(reminder.getUuid(),time);
                }
            }


            Log.d(TAG, "NEW REMINDER IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    public boolean createReminderMedicine(String reminderUuid,String medicineUuid) {
        try {
            Log.d(TAG, "reminder: "+reminderUuid+" &medicine: "+medicineUuid);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REMINDER_UUID, reminderUuid);
            values.put(KEY_MEDICINE_UUID, medicineUuid);

            long id = db.insert(TABLE_REMINDER_MEDICINE, null, values);
            db.close();
            Log.d(TAG, "NEW TABLE_REMINDER_MEDICINE IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }public boolean createReminderTime(String reminderUuid,String time) {
        try {
            Log.d(TAG, "reminder: "+reminderUuid+" & time: "+time);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REMINDER_UUID, reminderUuid);
            values.put(KEY_TIME, time);

            long id = db.insert(TABLE_REMINDER_TIME, null, values);
            db.close();
            Log.d(TAG, "NEW TABLE_REMINDER_TIME IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }


    public boolean updateRelative(Relative relative) {
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

    public FirstAid getFirstAid(int firstAidId){
        try{
            FirstAid firstAid = new FirstAid();
            String selectQuery = "SELECT  * FROM " + TABLE_FIRST_AID + " where id = '"+firstAidId+"' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst())
            {
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
            if(firstAid.getId()>0) {
                return firstAid;
            }
            return null;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return null;
        }
    }



    public List<Relative> getAllRelative() {
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

    public boolean deleteRelative(int relativeId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_RELATIVE, KEY_ID +"= '"+relativeId+"'",null );
            db.close();
            Log.d(TAG, "Relative is deleted: " + id);
            return id > 0;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return false;
        }
    }

    public Relative getRelative(int relativeId) {
        try{
            Relative relative = new Relative();
            String selectQuery = "SELECT  * FROM " + TABLE_RELATIVE + " where id = '"+relativeId+"' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst())
            {
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
            if(relative.getId()>0) {
                return relative;
            }
            return null;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return null;
        }
    }
    public Relative getRelativeByUuid(String uuid) {
        try{
            Relative relative = new Relative();
            String selectQuery = "SELECT  * FROM " + TABLE_RELATIVE + " where uuid = '"+uuid+"' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst())
            {
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
            if(relative.getId()>0) {
                return relative;
            }
            return null;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return null;
        }
    }


}
