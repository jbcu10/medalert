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

    public List<Medicine> getAllMedicine() {
        try {
            List<Medicine> medicines = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_MEDICINE + " order by " + KEY_ID + " desc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Medicine medicine = new Medicine();
                    medicine.setId(cursor.getInt(0));
                    medicine.setUuid(cursor.getString(1));
                    medicine.setName(cursor.getString(2));
                    medicine.setGenericName(cursor.getString(3));
                    medicine.setDiagnosis(cursor.getString(4));
                    medicine.setDescription(cursor.getString(5));
                    // getDoctor    medicine.setDoctor(cursor.getString(6));
                    medicine.setExpiration(cursor.getLong(7));
                    medicine.setType(cursor.getString(8));
                    medicine.setTotal(cursor.getInt(9));

                    medicines.add(medicine);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching medicines from database: " + medicines.get(0).getName().toString());
            return medicines;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

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

    public boolean createMedicine(Medicine medicine) {
        try {
            Log.d(TAG, medicine.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, medicine.getUuid());
            values.put(KEY_NAME, medicine.getName());
            values.put(KEY_GENERIC_NAME, medicine.getGenericName());
            values.put(KEY_DIAGNOSIS, medicine.getDiagnosis());
            values.put(KEY_DESCRIPTION, medicine.getDescription());
            if (medicine.getDoctor() != null) {
                values.put(KEY_DOCTOR_ID, medicine.getDoctor().getId());
            }
            values.put(KEY_EXPIRATION, medicine.getExpiration());
            values.put(KEY_TYPE, medicine.getType());
            values.put(KEY_TOTAL, medicine.getTotal());
            long id = db.insert(TABLE_MEDICINE, null, values);
            db.close();
            Log.d(TAG, "NEW MEDICINE IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
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
    }public boolean createReminder(Reminder reminder) {
        try {
            Log.d(TAG, reminder.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, reminder.getUuid());
            values.put(KEY_DESCRIPTION, reminder.getDescription());

            long id = db.insert(TABLE_REMINDER, null, values);
            db.close();
            Log.d(TAG, "NEW REMINDER IS CREATED W/ AN ID: " + id);
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

    public boolean updateMedicine(Medicine medicine) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, medicine.getUuid());
            values.put(KEY_NAME, medicine.getName());
            values.put(KEY_GENERIC_NAME, medicine.getGenericName());
            values.put(KEY_DIAGNOSIS, medicine.getDiagnosis());
            values.put(KEY_DESCRIPTION, medicine.getDescription());
            if (medicine.getDoctor() != null) {
                values.put(KEY_DOCTOR_ID, medicine.getDoctor().getId());
            }
            values.put(KEY_EXPIRATION, medicine.getExpiration());
            values.put(KEY_TYPE, medicine.getType());

            values.put(KEY_TOTAL, medicine.getTotal());
            long id = db.update(TABLE_MEDICINE, values, KEY_ID + "= '" + medicine.getId() + "'", null);
            db.close();
            Log.d(TAG, " MEDICINE IS UPDATED WITH AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
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

    public Medicine getMedicine(int medicineId) {
        try{
            Medicine medicine = new Medicine();
            String selectQuery = "SELECT  * FROM " + TABLE_MEDICINE + " where id = '"+medicineId+"' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst())
            {
                medicine.setId(cursor.getInt(0));
                medicine.setUuid(cursor.getString(1));
                medicine.setName(cursor.getString(2));
                medicine.setGenericName(cursor.getString(3));
                medicine.setDiagnosis(cursor.getString(4));
                medicine.setDescription(cursor.getString(5));
                // getDoctor    medicine.setDoctor(cursor.getString(6));
                medicine.setExpiration(cursor.getLong(7));
                medicine.setType(cursor.getString(8));
                medicine.setTotal(cursor.getInt(9));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Medicine: " + medicine.getName());
            if(medicine.getId()>0) {
                return medicine;
            }
            return null;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return null;
        }
    }
    public Medicine getMedicineByUuid(String uuid) {
        try{
            Medicine medicine = new Medicine();
            String selectQuery = "SELECT  * FROM " + TABLE_MEDICINE + " where uuid = '"+uuid+"' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst())
            {
                medicine.setId(cursor.getInt(0));
                medicine.setUuid(cursor.getString(1));
                medicine.setName(cursor.getString(2));
                medicine.setGenericName(cursor.getString(3));
                medicine.setDiagnosis(cursor.getString(4));
                medicine.setDescription(cursor.getString(5));
                // getDoctor    medicine.setDoctor(cursor.getString(6));
                medicine.setExpiration(cursor.getLong(7));
                medicine.setType(cursor.getString(8));
                medicine.setTotal(cursor.getInt(9));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Medicine: " + medicine.getName());
            if(medicine.getId()>0) {
                return medicine;
            }
            return null;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return null;
        }
    }

    public boolean deleteMedicine(int medicineId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_MEDICINE, KEY_ID +"= '"+medicineId+"'",null );
            db.close();
            Log.d(TAG, "Medicine is deleted: " + id);
            return id > 0;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return false;
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
