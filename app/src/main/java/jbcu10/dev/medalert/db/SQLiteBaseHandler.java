package jbcu10.dev.medalert.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dev on 11/20/17.
 */

public class SQLiteBaseHandler extends SQLiteOpenHelper {
    protected static final String TAG = CrudRepository.class.getSimpleName();
    protected static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "med_alert";

    //tables
    protected static final String TABLE_MEDICINE = "medicine";
    protected static final String TABLE_RELATIVE = "relative";
    protected static final String TABLE_PATIENT = "patient";
    protected static final String TABLE_PATIENT_RELATIVE = "patient_relative";
    protected static final String TABLE_FIRST_AID = "firstAid";
    protected static final String TABLE_INSTRUCTIONS = "instruction";
    protected static final String TABLE_REMINDER = "reminder";
    protected static final String TABLE_REMINDER_MEDICINE = "reminder_medicine";
    protected static final String TABLE_REMINDER_TIME = "reminder_time";
    protected static final String TABLE_REMINDER_PATIENT = "reminder_patient";
    //all
    protected static final String KEY_ID = "id";
    protected static final String KEY_UUID = "uuid";
    protected static final String ERROR = "ERROR: ";
    protected static final String TEXT = " TEXT,";

    //medicine
    protected static final String KEY_NAME = "name";
    protected static final String KEY_GENERIC_NAME = "genericName";
    protected static final String KEY_DIAGNOSIS = "diagnosis";
    protected static final String KEY_DESCRIPTION = "description";
    protected static final String KEY_DOCTOR_ID = "doctor_id";
    protected static final String KEY_TOTAL = "total";
    protected static final String KEY_EXPIRATION = "expiration";
    protected static final String KEY_TYPE = "type";

    //person
    protected static final String KEY_FIRST_NAME = "firstName";
    protected static final String KEY_MIDDLE_NAME = "middleName";
    protected static final String KEY_LAST_NAME = "lastName";
    protected static final String KEY_CONTACT_NUMBER = "contactNumber";
    protected static final String KEY_EMAIL = "email";
    protected static final String KEY_GENDER = "gender";
    protected static final String KEY_RELATIONSHIP = "relationship";

    //first aid
    protected static final String KEY_FIRST_AID_UUID = "firstAidUuid";
    protected static final String KEY_INSTRUCTION = "instruction";

    //reminders
    protected static final String KEY_MEDICINE_UUID = "medicine_uuid";
    protected static final String KEY_REMINDER_UUID = "reminder_uuid";
    protected static final String KEY_PATIENT_UUID = "patient_uuid";
    protected static final String KEY_RELATIVE_UUID = "relative_uuid";
    protected static final String KEY_TIME = "time";


    SQLiteBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createMedicineTable(db);
        this.createRelativeTable(db);
        this.createFirstAidTable(db);
        this.createInstructionsTable(db);
        this.createReminderTable(db);
        this.createReminderMedicineTable(db);
        this.createReminderTimeTable(db);
        this.createPatientTable(db);
        this.createPatientRelativeTable(db);
        this.createReminderPatient(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELATIVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIRST_AID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT_RELATIVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER_PATIENT);
        Log.d(TAG, "Database tables deleted");
        onCreate(db);
    }

    // Table Medicine Handler
    private void createMedicineTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_MEDICINE...");
            String createMedicinesTable = "CREATE TABLE " +
                    TABLE_MEDICINE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_UUID + TEXT
                    + KEY_NAME + TEXT
                    + KEY_GENERIC_NAME + TEXT
                    + KEY_DIAGNOSIS + TEXT
                    + KEY_DESCRIPTION + TEXT
                    + KEY_DOCTOR_ID + TEXT
                    + KEY_EXPIRATION + TEXT
                    + KEY_TYPE + " INTEGER,"

                    + KEY_TOTAL + " INTEGER" + ")";
            db.execSQL(createMedicinesTable);
            Log.d(TAG, "TABLE_MEDICINE IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table First Aid Handler
    private void createFirstAidTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_FIRST_AID...");
            String createFirstAidTable = "CREATE TABLE " +
                    TABLE_FIRST_AID + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_UUID + TEXT
                    + KEY_NAME + TEXT
                    + KEY_DESCRIPTION + " TEXT" + ")";
            db.execSQL(createFirstAidTable);
            Log.d(TAG, "TABLE_FIRST_AID IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table Reminder
    private void createReminderTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_REMINDER...");
            String createReminderTable = "CREATE TABLE " +
                    TABLE_REMINDER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_UUID + TEXT
                    + KEY_DESCRIPTION + " TEXT" + ")";
            db.execSQL(createReminderTable);
            Log.d(TAG, "TABLE_REMINDER IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    //Table Reminder Medicine
    private void createReminderMedicineTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_REMINDER_MEDICINE...");
            String createReminderMedicineTable = "CREATE TABLE " +
                    TABLE_REMINDER_MEDICINE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_MEDICINE_UUID + TEXT
                    + KEY_REMINDER_UUID + " TEXT" + ")";
            db.execSQL(createReminderMedicineTable);
            Log.d(TAG, "TABLE_REMINDER_MEDICINE IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    //Table Reminder Time
    private void createReminderTimeTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_REMINDER_TIME...");
            String createReminderTimeTable = "CREATE TABLE " +
                    TABLE_REMINDER_TIME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_REMINDER_UUID + TEXT
                    + KEY_TIME + " TEXT" + ")";
            db.execSQL(createReminderTimeTable);
            Log.d(TAG, "TABLE_REMINDER_TIME IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table Instruction Handler
    private void createInstructionsTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_INSTRUCTIONS...");
            String createInstructionTable = "CREATE TABLE " +
                    TABLE_INSTRUCTIONS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_FIRST_AID_UUID + TEXT
                    + KEY_UUID + TEXT
                    + KEY_INSTRUCTION + " TEXT" + ")";
            db.execSQL(createInstructionTable);
            Log.d(TAG, "TABLE_INSTRUCTIONS IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table Relative Handler
    private void createRelativeTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_RELATIVES...");
            String createRelativeTable = "CREATE TABLE " +
                    TABLE_RELATIVE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_UUID + TEXT
                    + KEY_FIRST_NAME + TEXT
                    + KEY_MIDDLE_NAME + TEXT
                    + KEY_LAST_NAME + TEXT
                    + KEY_CONTACT_NUMBER + TEXT
                    + KEY_EMAIL + TEXT
                    + KEY_RELATIONSHIP + " TEXT" + ")";
            db.execSQL(createRelativeTable);
            Log.d(TAG, "TABLE_MEDICINE IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table Patient Handler
    private void createPatientTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_PATIENT...");
            String createPatientTable = "CREATE TABLE " +
                    TABLE_PATIENT + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_UUID + TEXT
                    + KEY_FIRST_NAME + TEXT
                    + KEY_MIDDLE_NAME + TEXT
                    + KEY_LAST_NAME + TEXT
                    + KEY_CONTACT_NUMBER + TEXT
                    + KEY_GENDER + TEXT
                    + KEY_EMAIL + " TEXT" + ")";
            db.execSQL(createPatientTable);
            Log.d(TAG, "TABLE_PATIENT IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table Patient Relative Handler
    private void createPatientRelativeTable(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_PATIENT...");
            String createPatientRelativeTable = "CREATE TABLE " +
                    TABLE_PATIENT_RELATIVE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_PATIENT_UUID + TEXT
                    + KEY_RELATIVE_UUID + " TEXT" + ")";
            db.execSQL(createPatientRelativeTable);
            Log.d(TAG, "TABLE_PATIENT_RELATIVE IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

    // Table Reminder Patient Handler
    private void createReminderPatient(SQLiteDatabase db) {
        try {

            Log.d(TAG, "CREATING TABLE_REMINDER_PATIENT...");
            String createReminderPatient = "CREATE TABLE " +
                    TABLE_REMINDER_PATIENT + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_PATIENT_UUID + TEXT
                    + KEY_REMINDER_UUID + " TEXT" + ")";
            db.execSQL(createReminderPatient);
            Log.d(TAG, "TABLE_REMINDER_PATIENT IS CREATED ...");

        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
        }
    }

}
