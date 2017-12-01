package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Patient;

/**
 * Created by dev on 11/26/17.
 */

public class PatientRepository extends SQLiteBaseHandler implements CrudRepository<Patient> {
    public PatientRepository(Context context) {
        super(context);
    }

    @Override
    public List<Patient> getAll() {
        try {
            List<Patient> patients = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_PATIENT + " order by " + KEY_ID + " desc";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    Patient patient = new Patient();
                    patient.setId(cursor.getInt(0));
                    patient.setUuid(cursor.getString(1));
                    patient.setFirstName(cursor.getString(2));
                    patient.setMiddleName(cursor.getString(3));
                    patient.setLastName(cursor.getString(4));
                    patient.setContactNumber(cursor.getString(5));
                    patient.setGender(cursor.getString(6));
                    patient.setEmail(cursor.getString(7));
                    patients.add(patient);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Patients from database: " + patients.get(0).getLastName());
            return patients;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }
    }

    @Override
    public Patient getById(int id) {
        try {
            Patient patient = new Patient();
            String selectQuery = "SELECT  * FROM " + TABLE_PATIENT + " where id = '" + id + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                patient.setId(cursor.getInt(0));
                patient.setUuid(cursor.getString(1));
                patient.setFirstName(cursor.getString(2));
                patient.setMiddleName(cursor.getString(3));
                patient.setLastName(cursor.getString(4));
                patient.setContactNumber(cursor.getString(5));
                patient.setGender(cursor.getString(6));
                patient.setEmail(cursor.getString(7));
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Patient: " + patient.getFirstName());
            if (patient.getId() > 0) {
                return patient;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public Patient getByUuid(String uuid) {
        try {
            Patient patient = new Patient();
            String selectQuery = "SELECT  * FROM " + TABLE_PATIENT + " where uuid = '" + uuid + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                patient.setId(cursor.getInt(0));
                patient.setUuid(cursor.getString(1));
                patient.setFirstName(cursor.getString(2));
                patient.setMiddleName(cursor.getString(3));
                patient.setLastName(cursor.getString(4));
                patient.setContactNumber(cursor.getString(5));
                patient.setGender(cursor.getString(6));
                patient.setEmail(cursor.getString(7));
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Patient: " + patient.getFirstName());
            if (patient.getId() > 0) {
                return patient;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    public Patient getByLastNameAndFirstName(String lastName, String firstName) {
        try {
            Patient patient = new Patient();
            String selectQuery = "SELECT  * FROM " + TABLE_PATIENT + " where lastName = '" + lastName + "' and firstName = '" + firstName + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                patient.setId(cursor.getInt(0));
                patient.setUuid(cursor.getString(1));
                patient.setFirstName(cursor.getString(2));
                patient.setMiddleName(cursor.getString(3));
                patient.setLastName(cursor.getString(4));
                patient.setContactNumber(cursor.getString(5));
                patient.setGender(cursor.getString(6));
                patient.setEmail(cursor.getString(7));
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching Patient: " + patient.getFirstName());
            if (patient.getId() > 0) {
                return patient;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean create(Patient patient) {
        try {
            Log.d(TAG, patient.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, patient.getUuid());
            values.put(KEY_FIRST_NAME, patient.getFirstName());
            values.put(KEY_MIDDLE_NAME, patient.getMiddleName());
            values.put(KEY_LAST_NAME, patient.getLastName());
            values.put(KEY_CONTACT_NUMBER, patient.getContactNumber());
            values.put(KEY_EMAIL, patient.getEmail());
            values.put(KEY_GENDER, patient.getGender());

            long id = db.insert(TABLE_PATIENT, null, values);
            db.close();
            Log.d(TAG, "NEW PATIENT IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(Patient patient) {
        try {
            Log.d(TAG, patient.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, patient.getUuid());
            values.put(KEY_FIRST_NAME, patient.getFirstName());
            values.put(KEY_MIDDLE_NAME, patient.getMiddleName());
            values.put(KEY_LAST_NAME, patient.getLastName());
            values.put(KEY_CONTACT_NUMBER, patient.getContactNumber());
            values.put(KEY_EMAIL, patient.getEmail());
            values.put(KEY_GENDER, patient.getGender());
            long id = db.update(TABLE_PATIENT, values, KEY_ID + "= '" + patient.getId() + "'", null);
            db.close();
            Log.d(TAG, "NEW TABLE_PATIENT IS UPDATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean deleteById(int patientId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_PATIENT, KEY_ID + "= '" + patientId + "'", null);
            db.close();
            Log.d(TAG, "Patient is deleted: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }
    }

    public Patient getReminderPatientByReminderUuid(String reminderUuid) {
        try {
            Patient patient = new Patient();
            String selectQuery = "SELECT  * FROM " + TABLE_REMINDER_PATIENT + " where " + KEY_REMINDER_UUID + "='" + reminderUuid + "'";


            Log.d(TAG, "selectQuery: " + selectQuery);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {

                patient = this.getByUuid(cursor.getString(1));

            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching patient: " + patient.getFirstName());
            return patient;

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

    }
}
