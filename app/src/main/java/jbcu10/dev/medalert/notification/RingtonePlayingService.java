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
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.ReminderActivity;
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        String uuid=intent.getExtras().getString("uuid");
        Log.e("uuid ",uuid);
        AppController appController = AppController.getInstance();
        appController.setReminderUuid(uuid);
        MedicineRepository medicineRepository = new MedicineRepository(getApplicationContext());
        PatientRepository patientRepository = new PatientRepository(getApplicationContext());
        RelativeRepository relativeRepository = new RelativeRepository(getApplicationContext());
        List<Medicine> medicines =medicineRepository.getAllReminderMedicine(uuid);

        StringBuffer stringBuffer = new StringBuffer();

        for(Medicine medicine:medicines){
            stringBuffer.append(medicine.getName()+" - "+getSchedule(medicine.getSchedules())+"\n\n");
        }

        Patient patient = patientRepository.getReminderPatientByReminderUuid(intent.getExtras().getString("uuid"));
        List<Relative> relatives = relativeRepository.getAllRelativeByPatienUuid(patient.getUuid());
        String sms =intent.getExtras().getString("title")+ "\n\n"+intent.getExtras().getString("content")+"\n\n"+stringBuffer;

        Intent intent1 = new Intent(this.getApplicationContext(), ReminderActivity.class);
        intent1.putExtra("uuid",intent.getExtras().getString("uuid"));
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify  = new Notification.Builder(this)

                .setContentTitle(intent.getExtras().getString("title"))
                .setContentText(intent.getExtras().getString("content"))
                .setSmallIcon(R.drawable.ic_alert)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();



        if(!this.isRunning ) {
            Log.e("if there was not sound ", " and you want start");


           mMediaPlayer = MediaPlayer.create(this, R.raw.alarm);

            mMediaPlayer.start();


            mNM.notify(0, mNotify);

            this.isRunning = true;


        }
        else {
            Log.e("if there is sound ", " and you want end");

            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }

            Log.e("MyActivity", "In the service");
        if(relatives!=null) {

            sendSms(relatives,sms);
        }
        return START_NOT_STICKY;

    }


    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

        this.isRunning = false;
    }



    public void sendSms(List<Relative> relatives, String sms){
        for (Relative relative : relatives) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(relative.getContactNumber(), null,sms, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public StringBuffer getSchedule(List<String> schedules) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < schedules.size(); i++) {
            stringBuffer.append(schedules.get(i));
            if(i!=schedules.size()-1){
                stringBuffer.append(", ");
            }
        }
        return stringBuffer;
    }

}