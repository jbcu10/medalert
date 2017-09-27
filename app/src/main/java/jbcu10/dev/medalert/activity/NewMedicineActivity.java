package jbcu10.dev.medalert.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;

public class NewMedicineActivity extends BaseActivity  implements  TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{
    @BindView(R.id.editExpiration) EditText editExpiration;
    @BindView(R.id.editType) EditText editType;
     Calendar calendar;
    public static final String DATEPICKER_TAG = "Date Picker";

    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),false);

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    @OnClick(R.id.editExpiration)
    public void onClickEditExpiration(View view) {

        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);


    }
    @OnClick(R.id.editType)
    public void onClickEditType(View view) {

        new MaterialDialog.Builder(this)
                .title("Select Type")
                .items(R.array.type)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        editType.setText(text);
                        return true;
                    }
                })
                .positiveText("Submit")
                .show();

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        month = month+1;
        String smonth = month+"";
        String sday = day+"";
        if (month < 10) {
            smonth = "0" + month;
        }
        if (day < 10) {
            sday = "0" + day;
        }
        editExpiration.setText(sday + "-" + smonth+"-"+year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }
}
