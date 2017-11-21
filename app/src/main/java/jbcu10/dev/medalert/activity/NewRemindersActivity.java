package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import jbcu10.dev.medalert.db.DatabaseCRUDHandler;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Reminder;

public class NewRemindersActivity extends BaseActivity implements  TimePickerDialog.OnTimeSetListener {
    public DatabaseCRUDHandler db;
    @BindView(R.id.button_submit)
    Button button_submit;

    LinearLayout ll_alarm_handler, ll_medicine_handler;
    EditText edit_description;
    TimePickerDialog timePickerDialog;
    int a =0;
    public static final String TIMEPICKER_TAG = "Time Picker";
    ArrayList<String> strings = new ArrayList<>();
    List<String> timeStrings = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminders);
        ButterKnife.bind(this);
        initializedViews();
        db = new DatabaseCRUDHandler(this);
        List<Medicine> medicines =db.getAllMedicine();

        if (medicines==null) {
            addMedicine(this);
        }
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true, false);
        if (medicines!=null) {
            timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);


        for (Medicine medicine:medicines){
            final CheckBox checkBoxMedicine = new CheckBox(this);
            checkBoxMedicine.setText(medicine.getName());
            checkBoxMedicine.setId(medicine.getId());
            checkBoxMedicine.setHint(medicine.getUuid());
            checkBoxMedicine.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            checkBoxMedicine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        strings.add(checkBoxMedicine.getHint().toString());
                    }else{
                        strings.remove(checkBoxMedicine.getHint().toString());
                    }

                }
            });
            ll_medicine_handler.addView(checkBoxMedicine);

        }
        }

    }
    public void initializedViews(){
        ll_alarm_handler = findViewById(R.id.ll_alarm_handler);
        ll_medicine_handler = findViewById(R.id.ll_medicine_handler);
        edit_description = findViewById(R.id.edit_description);
    }

    public Reminder setReminder(){
        Reminder reminder =new Reminder();
        reminder.setDescription(edit_description.getText().toString());
        reminder.setUuid(UUID.randomUUID().toString());
        if(!strings.isEmpty()) {
            List<Medicine> medicines = new LinkedList<>();
            for (String uuid : strings) {
                Medicine medicine = db.getMedicineByUuid(uuid);
                medicines.add(medicine);
            }
            reminder.setMedicineList(medicines);
        }
        if(!timeStrings.isEmpty()) {

            reminder.setTime(timeStrings);
        }

        return reminder;
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String sMinute = minute+"";
        String shourOfDay = hourOfDay+"";
        if (hourOfDay < 10) {
            shourOfDay = "0" + hourOfDay;
        }if (minute < 10) {
            sMinute = "0" + minute;
        }
        String time =shourOfDay + ":" + sMinute;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.button_submit)
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
                            boolean isCreated = db.createReminder(setReminder());

                            if (isCreated) {
                                Intent intent = new Intent(NewRemindersActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                            if (!isCreated) {
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Save Reminder!", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());

                        }
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            }
        }).show();

    }
}
