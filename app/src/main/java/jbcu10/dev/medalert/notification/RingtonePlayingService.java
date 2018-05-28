package jbcu10.dev.medalert.notification;

/**
 * Created by dev on 12/3/17.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jbcu10.dev.medalert.activity.DialogActivity;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Relative;
import jbcu10.dev.medalert.model.Reminder;

public class RingtonePlayingService extends Service {

    private boolean isRunning;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "In the Richard service");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        String uuid=intent.getExtras().getString("uuid");
        Log.e("uuid ",uuid);
        AppController appController = AppController.getInstance();
        appController.setReminderUuid(uuid);
        ReminderRepository reminderRepository = new ReminderRepository(getApplicationContext());
        MedicineRepository medicineRepository = new MedicineRepository(getApplicationContext());
        PatientRepository patientRepository = new PatientRepository(getApplicationContext());
        RelativeRepository relativeRepository = new RelativeRepository(getApplicationContext());

        Reminder reminder =reminderRepository.getByUuid(uuid);
        if(reminder.isTurnOn()) {
            appController.setReminderId(reminder.getId());

            List<Medicine> medicines = medicineRepository.getAllReminderMedicine(uuid);

            StringBuffer stringBuffer = new StringBuffer();
            int a = 1;
            for (Medicine medicine : medicines) {
                stringBuffer.append(a + ". " + medicine.getName() + " - " + medicine.getDosage() + " - " + medicine.getTotal() + " - " + medicine.getStock() + " remaining" + "\n");
                a++;
            }
            //stringBuffer.append(reminder.getDescription());
            Patient patient = patientRepository.getReminderPatientByReminderUuid(intent.getExtras().getString("uuid"));
            List<Relative> relatives = relativeRepository.getAllRelativeByPatienUuid(patient.getUuid());
            String sms = intent.getExtras().getString("title") + "\n\n" + intent.getExtras().getString("content") + "\n\n" + stringBuffer;

            Intent intent1 = new Intent(this.getApplicationContext(), DialogActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

            Notification.Style style = new Notification.BigTextStyle()
                    .setBigContentTitle(intent.getExtras().getString("title"))
                    .bigText(stringBuffer);

            Notification builder = new Notification.Builder(this)
                    .setContentTitle(intent.getExtras().getString("title"))
                    .setContentText(sms)
                    .setSmallIcon(R.drawable.ic_alert)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setStyle(style)
                    .build();

            mMediaPlayer = MediaPlayer.create(this, R.raw.alarm);

            mMediaPlayer.start();


            mNM.notify(0, builder);

            this.isRunning = true;

            Log.e("MyActivity", "In the service");
            if (relatives != null) {

                sendSms(relatives, sms);
            }
        }
            return START_NOT_STICKY;


    }


    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

        this.isRunning = false;
    }


    public void sendSms(List<Relative> relatives, String sms) {
        for (Relative relative : relatives) {
            if(relative.isNotify()) {
                try {
                    Log.d("sms", sms);
                    Log.d("relative", relative.getContactNumber());

                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> msgArray = smsManager.divideMessage(sms);

                    smsManager.sendMultipartTextMessage(relative.getContactNumber(), null, msgArray, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again later!",
                            Toast.LENGTH_LONG).show();
                    Log.e("sms error", e.getMessage());
                }
            }
        }
    }


}