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
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Reminder;
import jbcu10.dev.medalert.model.Time;
import jbcu10.dev.medalert.notification.AlarmReceiver;


public class NewRemindersActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String TIMEPICKER_TAG = "Time Picker";
    public MedicineRepository medicineRepository;
    public ReminderRepository reminderRepository;
    public PatientRepository patientRepository;
    @BindView(R.id.button_submit_reminder)
    Button button_submit_reminder;
    @BindView(R.id.button_alarm)
    ImageButton button_alarm;

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
        initializedViews();


        ArrayList<Patient> patients = new ArrayList<>();
        List<Patient> patientsList = patientRepository.getAllEnabledPatient();
        HomeActivity.selectedItem = 0;
        if (patientsList == null) {
            addPatient(this, "Create new Patient?");
        }
        if (patientsList != null) {
            patients.addAll(patientsList);

            ArrayAdapter<Patient> patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patients);
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
        }


        List<Medicine> medicines = medicineRepository.getAllEnabledMedicine();
        if (patientsList != null && medicines == null) {
            addMedicine(this, "It seems that you don't have any medicine. Create a medicine to continue.");
        }
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), false);
        if (patientsList != null && medicines != null) {
            timePickerDialog.show(getFragmentManager(), TIMEPICKER_TAG);
            for (Medicine medicine : medicines) {
                LinearLayout ll_horizontalHandler = new LinearLayout(this);
                ll_horizontalHandler.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                ll_horizontalHandler.setWeightSum(4);
                final CheckBox checkBoxMedicine = new CheckBox(this);
                checkBoxMedicine.setText(medicine.getName() + " - " + medicine.getDosage() + " - " + medicine.getStock()+ " remaining");
                checkBoxMedicine.setId(medicine.getId());
                checkBoxMedicine.setHint(medicine.getUuid());
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 3;
                checkBoxMedicine.setLayoutParams(params);
                checkBoxMedicine.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        strings.add(checkBoxMedicine.getHint().toString());
                    } else {
                        strings.remove(checkBoxMedicine.getHint().toString());
                    }

                });
                EditText editText = new EditText(this);
                editText.setId(medicine.getId());
                editText.setText(String.valueOf(medicine.getTotal()));
                editText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        String total = editText.getText().toString();
                        if (total.equals("")||total.equals("0")){
                            total = "1";
                            editText.setText(total);
                        }
                        medicine.setTotal(Integer.parseInt(total));
                        medicineRepository.update(medicine);
                        return false;
                    }
                });
                params.weight = 1;

                editText.setLayoutParams(params);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                ll_horizontalHandler.addView(checkBoxMedicine);
                ll_horizontalHandler.addView(editText);

                ll_medicine_handler.addView(ll_horizontalHandler);

            }
        }

    }

    public void initializedViews() {
        ll_alarm_handler = findViewById(R.id.ll_alarm_handler);
        ll_medicine_handler = findViewById(R.id.ll_medicine_handler);
        edit_description = findViewById(R.id.edit_description);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.context = this;
        medicineRepository = new MedicineRepository(this);
        patientRepository = new PatientRepository(this);
        reminderRepository = new ReminderRepository(this);
        spinner = findViewById(R.id.spinner);

        ButterKnife.bind(this);

    }

    public Reminder setReminder() {
        Reminder reminder = new Reminder();
        reminder.setDescription(edit_description.getText().toString());
        reminder.setUuid(UUID.randomUUID().toString());
        reminder.setTurnOn(true);

        if (strings != null || !strings.isEmpty()) {
            Log.d("medicines", strings.size() + "");

            List<Medicine> medicines = new LinkedList<>();
            for (String uuid : strings) {
                Medicine medicine = medicineRepository.getByUuid(uuid);
                medicines.add(medicine);
            }
            reminder.setMedicineList(medicines);
        }
        if (!timeStrings.isEmpty()) {

            List<Time> times = new LinkedList<>();
            for (String time : timeStrings) {
                times.add(new Time(UUID.randomUUID().toString(), time, new Random().nextInt(6)));
            }
            reminder.setTime(times);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case R.id.add_medicine:
                addMedicine(this, "Create New Medicine?");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.button_alarm)
    public void onClickButtonAlarm(View view) {
        timePickerDialog.show(getFragmentManager(), TIMEPICKER_TAG);
    }

    @OnClick(R.id.button_submit_reminder)
    public void onClickButtonSubmit(View view) {
        Reminder reminder = setReminder();
        Log.d("medicines", reminder.getMedicineList().size() + "");

        if (isReminderValid(reminder)) {

            new MaterialDialog.Builder(NewRemindersActivity.this)
                    .title("Save Reminder?")
                    .content("Are you sure you want save this items?")
                    .positiveText("Save")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            try {
                                boolean isCreated = reminderRepository.create(reminder);
                                if (isCreated) {
                                    setAlarmFromTimer(reminder);
                                    Intent intent = new Intent(NewRemindersActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                    if (!isCreated) {
                                        Snackbar.make(findViewById(android.R.id.content), "Failed to Save Reminder!", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("Error", e.getMessage());
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Save Reminder!", Snackbar.LENGTH_LONG).show();


                            }
                        }
                    }).show();
        }
    }

    private boolean isReminderValid(Reminder reminder) {

        if (reminder.getPatient() == null) {
            Snackbar.make(findViewById(android.R.id.content), "Please select a patient for reminder!", Snackbar.LENGTH_LONG).show();
            return false;
        }


        if (reminder.getMedicineList() == null || reminder.getMedicineList().isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Please select a medicine for reminder!", Snackbar.LENGTH_LONG).show();
            return false;

        }

        for(Medicine medicine: reminder.getMedicineList()){
            if(medicine.getStock()<=0){
                Snackbar.make(findViewById(android.R.id.content), medicine.getName()+"  is out of stock !", Snackbar.LENGTH_LONG).show();
                return false;
            }
            if(medicine.getTotal()>medicine.getStock()){
                Snackbar.make(findViewById(android.R.id.content), "You set "+medicine.getName()+" number of times to take higher than its stock !", Snackbar.LENGTH_LONG).show();
                return false;
            }
        }
        if (reminder.getTime() == null) {
            Snackbar.make(findViewById(android.R.id.content), "Please add time for reminder!", Snackbar.LENGTH_LONG).show();
            return false;

        }

        return true;
    }


    public void setAlarmFromTimer(Reminder reminder) {
        final Calendar calendar = Calendar.getInstance();
        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        for (Time time : reminder.getTime()) {

            if (!time.equals("removed")) {
                String[] timeArray = time.getTime().split(":");
                final int hour = Integer.parseInt(timeArray[0]);
                final int minute = Integer.parseInt(timeArray[1]);

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                myIntent.putExtra("title", "Reminder for Patient - " + reminder.getPatient().getFirstName() + " " + reminder.getPatient().getLastName());
                myIntent.putExtra("content", reminder.getDescription());
                myIntent.putExtra("uuid", reminder.getUuid());
                Log.d("time", String.valueOf(new Date(calendar.getTimeInMillis())));
                PendingIntent pending_intent = PendingIntent.getBroadcast(getApplicationContext(), time.getIntentId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= 19) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                }
            }
            //alarmManager.cancel();
        }
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        //String secondString = second < 10 ? "0"+second : ""+second;
        final String time = hourString + ":" + minuteString;


        boolean match = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            match = timeStrings.stream().anyMatch(time::contains);
        }
        if (match) {
            Snackbar.make(findViewById(android.R.id.content), "Time Already Exist!", Snackbar.LENGTH_LONG).setActionTextColor(Color.RED).show();
        }
        if (!match) {
            timeStrings.add(time);
            setTimeView(time);

        }
    }

    private void setTimeView(String time) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(4);
        int id = a;
        TextView txtAlarm = new TextView(this);
        txtAlarm.setText(time);
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
            }
        });
        linearLayout.addView(txtAlarm);
        linearLayout.addView(image_delete);
        ll_alarm_handler.addView(linearLayout);
        a++;
    }
}
