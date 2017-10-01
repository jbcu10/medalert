package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Medicine;


/**
 * Created by dev on 10/1/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "med_alert";

    private static final String TABLE_MEDICINE= "medicine";
    private static final String KEY_ID = "id";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENERIC_NAME = "genericName";
    private static final String KEY_DIAGNOSIS = "diagnosis";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DOCTOR_ID = "doctor_id";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_EXPIRATION = "expiration";
    private static final String ERROR = "ERROR: ";
    private static final String TEXT =  " TEXT,";


    @Override
    public void onCreate(SQLiteDatabase db) {
       this.createMedicineTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    
    // Table Medicine Handler
    private void createMedicineTable(SQLiteDatabase db){
        try{

            Log.d(TAG, "CREATING TABLE_MEDICINE...");
            String createMedicinesTable = "CREATE TABLE " +
                    TABLE_MEDICINE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_UUID +TEXT
                    + KEY_NAME +TEXT
                    + KEY_GENERIC_NAME +TEXT
                    + KEY_DIAGNOSIS +TEXT
                    + KEY_DESCRIPTION +TEXT
                    + KEY_DOCTOR_ID +TEXT
                    + KEY_EXPIRATION +" INTEGER,"

                    + KEY_TOTAL + " INTEGER" + ")";
            db.execSQL(createMedicinesTable);
            Log.d(TAG, "TABLE_MEDICINE IS CREATED ...");

        }
        catch (Exception e){
            Log.d(TAG,"ERROR --------------- "+ e.getMessage());
        }
    }

    public List<Medicine> getAllMedicine() {
        try {
            List<Medicine> medicines  = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_MEDICINE+ " order by "+KEY_ID+" desc" ;
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
                    medicine.setTotal(cursor.getInt(8));

                    medicines.add(medicine);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching medicines from database: " + medicines.get(0).getName().toString());
            return medicines;
        }
        catch (Exception e){
            Log.d(TAG,"ERROR --------------- "+ e.getMessage());
            return null;
        }

    }

    public boolean createMedicine(Medicine medicine) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, medicine.getUuid());
            values.put(KEY_NAME, medicine.getName());
            values.put(KEY_GENERIC_NAME, medicine.getGenericName());
            values.put(KEY_DIAGNOSIS, medicine.getDiagnosis());
            values.put(KEY_DESCRIPTION, medicine.getDescription());
            if(medicine.getDoctor()!=null) {
                values.put(KEY_DOCTOR_ID, medicine.getDoctor().getId());
            }
            values.put(KEY_EXPIRATION, medicine.getExpiration());
            values.put(KEY_TOTAL, medicine.getTotal());
            long id = db.insert(TABLE_MEDICINE, null, values);
            db.close();
            Log.d(TAG, "NEW MEDICINE IS CREATED W/ AN ID: " + id);
            return id > 0;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
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
            if(medicine.getDoctor()!=null) {
                values.put(KEY_DOCTOR_ID, medicine.getDoctor().getId());
            }
            values.put(KEY_EXPIRATION, medicine.getExpiration());
            values.put(KEY_TOTAL, medicine.getTotal());
            long id = db.update(TABLE_MEDICINE, values, KEY_ID +"= '"+medicine.getId()+"'",null );
            db.close();
            Log.d(TAG, "NEW MEDICINE IS CREATED W/ AN ID: " + id);
            return id > 0;
        }
        catch (Exception e){
            Log.d(TAG,ERROR + e.getMessage());
            return false;
        }
    }

}
