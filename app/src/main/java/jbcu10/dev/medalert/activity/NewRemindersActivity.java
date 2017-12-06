package jbcu10.dev.medalert.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Reminder;
import jbcu10.dev.medalert.notification.AlarmReceiver;
import jbcu10.dev.medalert.notification.NotificationHelper;

public class NewRemindersActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String TIMEPICKER_TAG = "Time Picker";
    public MedicineRepository medicineRepository;
    public ReminderRepository reminderRepository;
    public PatientRepository patientRepository;
    @BindView(R.id.button_submit_reminder)
    Button button_submit_reminder;
    LinearLayout ll_alarm_handler, ll_medicine_handler;
    EditText edit_description;
    TimePickerDialog timePickerDialog;
    int a = 0;
    ArrayList<String> strings = new ArrayList<>();
    List<String> timeStrings = new LinkedList<>();
    Spinner spinner;
    Patient patient;
    private AlarmReceiver alarm;
    AlarmManager alarmManager;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminders);
        ButterKnife.bind(this);
        initializedViews();
        this.context = this;


        medicineRepository = new MedicineRepository(this);
        patientRepository = new PatientRepository(this);
        reminderRepository = new ReminderRepository(this);
        spinner = findViewById(R.id.spinner);
        ArrayList<Patient> patients = new ArrayList<>();
        List<Patient> patientsList = patientRepository.getAllEnabledPatient();
        HomeActivity.selectedItem =0;
        if (patientsList == null) {
            addPatient(this, "Create new Patient?");
        }
        if (patientsList != null) {
            patients.addAll(patientsList);

            ArrayAdapter<Patient> patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patients);
            patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(patientAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    patient = (Patient) spinner.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            // patient = (Patient) spinner.getSelectedItem();

            //Log.d("uuid",patient.getUuid());
        }


        List<Medicine> medicines = medicineRepository.getAllEnabledMedicine();
        if (patientsList != null && medicines == null) {
            addMedicine(this, "It seems that you don't have any medicine. Create a medicine to continue.");
        }
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true, false);
        if (patientsList != null && medicines != null) {
            timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            for (Medicine medicine : medicines) {
                final CheckBox checkBoxMedicine = new CheckBox(this);
                checkBoxMedicine.setText(medicine.getName());
                checkBoxMedicine.setId(medicine.getId());
                checkBoxMedicine.setHint(medicine.getUuid());
                checkBoxMedicine.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                checkBoxMedicine.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        new MaterialDialog.Builder(this)
                                .title(medicine.getName())
                                .content(this.getSchedule(medicine.getSchedules()))
                                .positiveText("Ok")
                                .show();

                        strings.add(checkBoxMedicine.getHint().toString());
                    } else {
                        strings.remove(checkBoxMedicine.getHint().toString());
                    }

                });
                ll_medicine_handler.addView(checkBoxMedicine);

            }
        }

    }

    public void initializedViews() {
        ll_alarm_handler = findViewById(R.id.ll_alarm_handler);
        ll_medicine_handler = findViewById(R.id.ll_medicine_handler);
        edit_description = findViewById(R.id.edit_description);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    public Reminder setReminder() {
        Reminder reminder = new Reminder();
        reminder.setDescription(edit_description.getText().toString());
        reminder.setUuid(UUID.randomUUID().toString());
        if (!strings.isEmpty()) {
            List<Medicine> medicines = new LinkedList<>();
            for (String uuid : strings) {
                Medicine medicine = medicineRepository.getByUuid(uuid);
                medicines.add(medicine);
            }
            reminder.setMedicineList(medicines);
        }
        if (!timeStrings.isEmpty()) {

            reminder.setTime(timeStrings);
        }
        //if(patient!=null) {
        reminder.setPatient((Patient) spinner.getSelectedItem());
        //}

        return reminder;
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String sMinute = minute + "";
        String shourOfDay = hourOfDay + "";
        if (hourOfDay < 10) {
            shourOfDay = "0" + hourOfDay;
        }
        if (minute < 10) {
            sMinute = "0" + minute;
        }
        final String time = shourOfDay + ":" + sMinute;


        boolean match = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            match = timeStrings.stream().anyMatch(time::contains);
        }
        if (match) {
            Snackbar.make(findViewById(android.R.id.content), "Time Already Exist!", Snackbar.LENGTH_LONG).setActionTextColor(Color.RED).show();
        }
        if (!match) {
            timeStrings.add(time);
            TextView txtAlarm = new TextView(this);
            txtAlarm.setText(time);
            txtAlarm.setId(a++);
            txtAlarm.setTextColor(Color.BLACK);
            txtAlarm.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            ll_alarm_handler.addView(txtAlarm);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.add_alarm:
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);

                return true;
            case R.id.add_medicine:
                addMedicine(this, "Create New Medicine?");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.button_submit_reminder)
    public void onClickButtonSubmit(View view) {

        new MaterialDialog.Builder(NewRemindersActivity.this)
                .title("Save Reminder?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            Reminder reminder = setReminder();
                            Log.d("Reminder", reminder.getPatient().getUuid());

                            boolean isCreated = reminderRepository.create(reminder);

                            if (isCreated) {
                                setAlarmFromTimer(reminder);
                                Intent intent = new Intent(NewRemindersActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                            if (!isCreated) {
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Save Reminder!", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Error", e.getMessage());
                            Snackbar.make(findViewById(android.R.id.content), "Failed to Save Reminder!", Snackbar.LENGTH_LONG).show();


                        }
                    }
                }).show();

    }

    public void setAlarmFromTimer( Reminder reminder) {
        final Calendar calendar = Calendar.getInstance();
        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
            int a =0;
        for(String time:timeStrings) {
            String[] timeArray= time.split(":");
            final int hour = Integer.parseInt(timeArray[0]);
            final int minute =  Integer.parseInt(timeArray[1]);

            Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);


            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            myIntent.putExtra("title", "Reminder for - "+ reminder.getPatient().getFirstName());
            myIntent.putExtra("content", reminder.getDescription());
            myIntent.putExtra("uuid", reminder.getUuid());
            PendingIntent pending_intent = PendingIntent.getBroadcast(NewRemindersActivity.this, a, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

            a++;
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
