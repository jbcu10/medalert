package jbcu10.dev.medalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Reminder;
import jbcu10.dev.medalert.model.Time;

/**
 * Created by dev on 11/26/17.
 */

public class ReminderRepository extends SQLiteBaseHandler implements CrudRepository<Reminder> {


    public ReminderRepository(Context context) {
        super(context);
    }

    public Reminder getById(int reminderId) {
        try {
            Reminder reminder = new Reminder();
            String selectQuery = "SELECT  * FROM " + TABLE_REMINDER + " where id = '" + reminderId + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                reminder.setId(cursor.getInt(0));
                reminder.setUuid(cursor.getString(1));
                reminder.setDescription(cursor.getString(2));
                reminder.setTurnOn(cursor.getInt(3)>0);


            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching reminder: " + reminder.getDescription());
            if (reminder.getId() > 0) {
                return reminder;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public Reminder getByUuid(String uuid) {
        try {
            Reminder reminder = new Reminder();
            String selectQuery = "SELECT  * FROM " + TABLE_REMINDER + " where uuid = '" + uuid + "' ;";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("size in cursor", cursor.getCount() + "");
            if (cursor.moveToFirst()) {
                reminder.setId(cursor.getInt(0));
                reminder.setUuid(cursor.getString(1));
                reminder.setDescription(cursor.getString(2));
                reminder.setTurnOn(cursor.getInt(3)>0);


            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching reminder: " + reminder.getDescription());
            if (reminder.getId() > 0) {
                return reminder;
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reminder> getAll() {
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
                    reminder.setTurnOn(cursor.getInt(3)>0);

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

    public List<Time> getAllReminderTime(String reminderUuid) {
        try {
            List<Time> times = new LinkedList<>();
            String selectQuery = "SELECT  * FROM " + TABLE_REMINDER_TIME + " where " + KEY_REMINDER_UUID + "='" + reminderUuid + "' order by " + KEY_ID + " asc";

            Log.d(TAG, "selectQuery: " + selectQuery);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Log.d(TAG, "get medicines uuid: " + cursor.getString(2));
                    Time time = new Time();
                    time.setUuid(cursor.getString(2));
                    time.setTime(cursor.getString(3));
                    time.setIntentId(cursor.getInt(4));
                    times.add(time);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            Log.d(TAG, "Fetching time from database: " + times.get(0).getTime());
            return times;
        } catch (Exception e) {
            Log.d(TAG, "ERROR --------------- " + e.getMessage());
            return null;
        }

    }

    @Override
    public boolean create(Reminder reminder) {
        try {
            Log.d(TAG, reminder.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, reminder.getUuid());
            values.put(KEY_DESCRIPTION, reminder.getDescription());
            values.put(KEY_SWITCH, reminder.isTurnOn()?1:0);

            long id = db.insert(TABLE_REMINDER, null, values);
            db.close();

            if (reminder.getMedicineList() != null) {
                for (Medicine medicine : reminder.getMedicineList()) {
                    this.createReminderMedicine(reminder.getUuid(), medicine.getUuid());
                }
            }
            if (reminder.getTime() != null) {
                for (Time time : reminder.getTime()) {
                    this.createReminderTime(reminder.getUuid(), time);
                }
            }
            if (reminder.getPatient() != null) {
                this.createReminderPatient(reminder.getUuid(), reminder.getPatient().getUuid());
            }


            Log.d(TAG, "NEW REMINDER IS CREATED W/ AN ID: " + id);
            return id > 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, ERROR + e);
            return false;
        }
    }

    @Override
    public boolean update(Reminder reminder) {
        try {
            Log.d(TAG, reminder.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DESCRIPTION, reminder.getDescription());
            values.put(KEY_SWITCH, reminder.isTurnOn()?1:0);
            long id = db.update(TABLE_REMINDER, values, KEY_ID + "= '" + reminder.getId() + "'", null);
            db.close();
            Log.d(TAG, "NEW TABLE_STORE IS UPDATED W/ AN ID: " + id);
            if (reminder.getMedicineList() != null) {
                this.deleteReminderMedicine(reminder.getUuid());
                for (Medicine medicine : reminder.getMedicineList()) {
                    this.createReminderMedicine(reminder.getUuid(), medicine.getUuid());
                }
            }
            if (reminder.getTime() != null) {

                this.deleteReminderTime(reminder.getUuid());

                for (Time time : reminder.getTime()) {
                    if(!time.getTime().equals("removed")) {
                        this.createReminderTime(reminder.getUuid(), time);
                    }
                }
            }
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
            return     false;
        }
    }

    @Override
    public boolean deleteById(int reminderId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_REMINDER, KEY_ID + "= '" + reminderId + "'", null);
            db.close();
            Log.d(TAG, "Reminder is deleted: " + id);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }    }

    private void createReminderMedicine(String reminderUuid, String medicineUuid) {
        try {
            Log.d(TAG, "reminder: " + reminderUuid + " &medicine: " + medicineUuid);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REMINDER_UUID, reminderUuid);
            values.put(KEY_MEDICINE_UUID, medicineUuid);

            long id = db.insert(TABLE_REMINDER_MEDICINE, null, values);
            db.close();
            Log.d(TAG, "NEW TABLE_REMINDER_MEDICINE IS CREATED W/ AN ID: " + id);
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
        }
    }

    private boolean deleteReminderMedicine(String uuid ) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_REMINDER_MEDICINE, KEY_REMINDER_UUID + "= '" + uuid + "'", null);
            db.close();
            Log.d(TAG, "Reminder time is deleted: " + uuid);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }
    }

    private void createReminderPatient(String reminderUuid, String patientUuid) {
        try {
            Log.d(TAG, "reminder: " + reminderUuid + " & patient: " + patientUuid);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REMINDER_UUID, reminderUuid);
            values.put(KEY_PATIENT_UUID, patientUuid);

            long id = db.insert(TABLE_REMINDER_PATIENT, null, values);
            db.close();
            Log.d(TAG, "NEW TABLE_REMINDER_PATIENT IS CREATED W/ AN ID: " + id);
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
        }
    }

    private void createReminderTime(String reminderUuid, Time time) {
        try {
            Log.d(TAG, "reminder: " + reminderUuid + " & time: " + time);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_REMINDER_UUID, reminderUuid);
            values.put(KEY_TIME_UUID, time.getUuid());
            values.put(KEY_TIME, time.getTime());
            values.put(KEY_INTENT_ID, time.getIntentId());

            long id = db.insert(TABLE_REMINDER_TIME, null, values);
            db.close();
            Log.d(TAG, "NEW TABLE_REMINDER_TIME IS CREATED W/ AN ID: " + id);
        } catch (Exception e) {
            Log.d(TAG, ERROR + e);
        }
    }

    private boolean deleteReminderTime(String uuid ) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_REMINDER_TIME, KEY_REMINDER_UUID + "= '" + uuid + "'", null);
            db.close();
            Log.d(TAG, "Reminder time is deleted: " + uuid);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }
    }
    public boolean deleteTimeByUuid(String uuid){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.delete(TABLE_REMINDER_TIME, KEY_TIME_UUID + "= '" + uuid + "'", null);
            db.close();
            Log.d(TAG, "Reminder time is deleted: " + uuid);
            return id > 0;
        } catch (Exception e) {
            Log.d(TAG, ERROR + e.getMessage());
            return false;
        }
    }
}
