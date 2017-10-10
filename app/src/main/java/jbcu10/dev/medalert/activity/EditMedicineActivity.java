package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.DatabaseHandler;
import jbcu10.dev.medalert.model.Medicine;

public class EditMedicineActivity extends BaseActivity implements  TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    @BindView(R.id.edit_expiration) EditText edit_expiration;
    @BindView(R.id.edit_type) EditText edit_type;
    @BindView(R.id.edit_name) EditText edit_name;
    @BindView(R.id.edit_generic_name) EditText edit_generic_name;
    @BindView(R.id.edit_description) EditText edit_description;
    @BindView(R.id.edit_diagnosis) EditText edit_diagnosis;
    @BindView(R.id.button_submit)
    Button button_submit;
    Calendar calendar;
    private static final String TAG = EditMedicineActivity.class.getSimpleName();
    public DatabaseHandler db;
    public static final String DATEPICKER_TAG = "Date Picker";
    DatePickerDialog datePickerDialog;
    Medicine medicine = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        ButterKnife.bind(this);
        initializedViews();
        db = new DatabaseHandler(EditMedicineActivity.this);
        AppController appController =AppController.getInstance();

        medicine =appController.getMedicine();
        setMedicineData(medicine);
        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(EditMedicineActivity.this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),false);

    }
    @OnClick(R.id.edit_expiration)
    public void onClickEditExpiration(View view) {
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
    }


    private void setMedicineData(Medicine medicine){
        edit_name.setText(medicine.getName());
        edit_generic_name.setText(medicine.getGenericName());
        edit_description.setText(medicine.getDescription());
        edit_diagnosis.setText(medicine.getDiagnosis());

        Date date = new Date(medicine.getExpiration());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        edit_expiration.setText(df.format(date));
        edit_type.setText(medicine.getType());

    }
    @OnClick(R.id.edit_type)
    public void onClickEditType(View view) {

        new MaterialDialog.Builder(this)
                .title("Select Type")
                .items(R.array.type)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        edit_type.setText(text);
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
        edit_expiration.setText(sday + "-" + smonth+"-"+year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }

    public void initializedViews(){
        edit_expiration = findViewById(R.id.edit_expiration);
        edit_type = findViewById(R.id.edit_type);
        edit_name = findViewById(R.id.edit_name);
        edit_generic_name = findViewById(R.id.edit_generic_name);
        edit_description = findViewById(R.id.edit_description);
        edit_diagnosis = findViewById(R.id.edit_diagnosis);

    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {

        new MaterialDialog.Builder(EditMedicineActivity.this)
                .title("Save Medicine?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String expirationDateString = edit_expiration.getText().toString();
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date expirationDate;
                        long milliseconds=0;
                        try {
                            expirationDate = df.parse(expirationDateString);
                            milliseconds = expirationDate.getTime();

                        } catch (ParseException e) {
                            Log.d("Error",e.getMessage());
                        }



                        try{
                            boolean isCreated = db.updateMedicine(new Medicine(medicine.getId(),UUID.randomUUID().toString(),edit_name.getText().toString(),edit_generic_name.getText().toString(),edit_diagnosis.getText().toString(),edit_description.getText().toString(),milliseconds,10,null,edit_type.getText().toString()));

                            if(isCreated){
                                Intent intent = new Intent(EditMedicineActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }if(!isCreated){
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Save Medicine!", Snackbar.LENGTH_LONG).show();
                            }

                        }
                        catch (Exception e){
                            Log.d("Error",e.getMessage());
                        }


                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                }).show();

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
