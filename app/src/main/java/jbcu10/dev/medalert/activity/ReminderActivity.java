package jbcu10.dev.medalert.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Reminder;
import jbcu10.dev.medalert.model.Time;
import jbcu10.dev.medalert.notification.AlarmReceiver;

public class ReminderActivity extends AppCompatActivity {
    public MedicineRepository medicineRepository;
    public ReminderRepository reminderRepository;
    public PatientRepository patientRepository;
    List<String> timeStrings = new LinkedList<>();
    AlarmManager alarmManager;

    Context context;
    Reminder reminder = null;
    Patient patient = null;
    LinearLayout ll_alarm_handler, ll_medicine_handler;
    EditText edit_description;
    TextView txt_patient;
    @BindView(R.id.button_submit)
    Button button_submit;
    ArrayList<String> strings = new ArrayList<>();
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        medicineRepository = new MedicineRepository(this);
        reminderRepository = new ReminderRepository(this);
        patientRepository = new PatientRepository(this);
        HomeActivity.selectedItem = 0;

        initializedViews();
        AppController appController = AppController.getInstance();
        reminder = reminderRepository.getById(appController.getReminderId());
        if (reminder == null) {
            reminder = reminderRepository.getByUuid(appController.getReminderUuid());
        }
        edit_description.setText(reminder.getDescription());
        reminder.setMedicineList(medicineRepository.getAllReminderMedicine(reminder.getUuid()));
        reminder.setTime(reminderRepository.getAllReminderTime(reminder.getUuid()));

        List<Medicine> medicines = medicineRepository.getAll();
        int sameUuid = 0;
        for (Medicine medicine : medicines) {
            if (reminder.getMedicineList() != null) {
                for (Medicine medicine1 : reminder.getMedicineList()) {
                    if (medicine1.getUuid().equals(medicine.getUuid())) {
                        sameUuid++;
                    }
                }
            }
            final CheckBox checkBoxMedicine = new CheckBox(this);
            checkBoxMedicine.setText(medicine.getName());
            checkBoxMedicine.setId(medicine.getId());
            checkBoxMedicine.setHint(medicine.getUuid());
            if (sameUuid > 0) {
                checkBoxMedicine.setChecked(true);
            }
            checkBoxMedicine.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            checkBoxMedicine.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    strings.add(checkBoxMedicine.getHint().toString());
                } else {
                    strings.remove(checkBoxMedicine.getHint().toString());
                }

            });
            ll_medicine_handler.addView(checkBoxMedicine);
            sameUuid = 0;
        }
        if(reminder.getTime()!=null) {
            for (Time time : reminder.getTime()) {
                timeStrings.add(time.getTime());

                setTimeView(time);
            }
        }

        patient = patientRepository.getReminderPatientByReminderUuid(reminder.getUuid());
        if (patient != null) {
            reminder.setPatient(patient);
            txt_patient.setText(patient.toString());
        }
    }

    private void setTimeView(Time time) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(4);
        int id = a;
        TextView txtAlarm = new TextView(this);
        txtAlarm.setText(time.getTime());
        txtAlarm.setId(id);
        txtAlarm.setTextColor(Color.BLACK);
        txtAlarm.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));


        ImageView image_delete = new ImageView(this);
        image_delete.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_red));
        image_delete.setId(id);
        image_delete.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 3));
        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_alarm_handler.removeView((View) image_delete.getParent());
                if (image_delete.getId() < timeStrings.size() - 1) {
                    timeStrings.set(image_delete.getId(), "removed");
                }
                if (image_delete.getId() == timeStrings.size() - 1) {
                    timeStrings.remove(image_delete.getId());
                    a--;
                }
                cancelAlarmByTime(time);
            }
        });
        linearLayout.addView(txtAlarm);
        linearLayout.addView(image_delete);
        ll_alarm_handler.addView(linearLayout);
        a++;
    }


    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_edit, menu);
        return true;
    }

    public void initializedViews() {
        ll_alarm_handler = findViewById(R.id.ll_alarm_handler);
        ll_medicine_handler = findViewById(R.id.ll_medicine_handler);
        edit_description = findViewById(R.id.edit_description);
        txt_patient = findViewById(R.id.txt_patient);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.context = this;
    }


    public void cancelAlarmByTime(Time time) {
        final Calendar calendar = Calendar.getInstance();
        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        String[] timeArray = time.getTime().split(":");
        final int hour = Integer.parseInt(timeArray[0]);
        final int minute = Integer.parseInt(timeArray[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        myIntent.putExtra("title", "Reminder for Patient - " + reminder.getPatient().getFirstName() + " " + reminder.getPatient().getLastName());
        myIntent.putExtra("content", reminder.getDescription());
        myIntent.putExtra("uuid", reminder.getUuid());
        Log.d("time", String.valueOf(new Date(calendar.getTimeInMillis())));
        PendingIntent pending_intent = PendingIntent.getBroadcast(getApplicationContext(), time.getIntentId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pending_intent);
        reminderRepository.deleteTimeByUuid(time.getUuid());

    }
}
