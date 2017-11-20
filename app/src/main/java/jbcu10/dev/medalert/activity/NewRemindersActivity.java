package jbcu10.dev.medalert.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.db.DatabaseCRUDHandler;
import jbcu10.dev.medalert.model.Medicine;

public class NewRemindersActivity extends BaseActivity implements  TimePickerDialog.OnTimeSetListener {
    public DatabaseCRUDHandler db;
    @BindView(R.id.edit_time)
    EditText edit_time;
    TimePickerDialog timePickerDialog;

    public static final String TIMEPICKER_TAG = "Time Picker";
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


    }
    public void initializedViews(){
        edit_time = findViewById(R.id.edit_time);
       
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String sMinute;
        if (minute < 10) {
            sMinute = "0" + minute;
        } else {
            sMinute = minute + "";
        }
            edit_time.setText(hourOfDay + ":" + sMinute);
    }
    @OnClick(R.id.edit_time)
    public void onClickEditTime(View view) {

        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);


    }
}
