package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Medicine;

/**
 * Created by dev on 11/26/17.
 */

public class MedicineCRUDHandler extends SQLiteBaseHandler {
    public MedicineCRUDHandler(Context context) {
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
}
