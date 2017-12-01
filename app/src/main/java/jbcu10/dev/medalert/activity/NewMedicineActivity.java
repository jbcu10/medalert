package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;

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
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

public class NewMedicineActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    public static final String DATEPICKER_TAG = "Date Picker";
    private static final String TAG = NewMedicineActivity.class.getSimpleName();
    public MedicineRepository medicineRepository;
    @BindView(R.id.edit_expiration)
    EditText edit_expiration;
    @BindView(R.id.edit_type)
    EditText edit_type;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_generic_name)
    EditText edit_generic_name;
    @BindView(R.id.edit_description)
    EditText edit_description;
    @BindView(R.id.edit_diagnosis)
    EditText edit_diagnosis;
    @BindView(R.id.edit_total)
    EditText edit_total;
    @BindView(R.id.button_submit)
    Button button_submit;
    Calendar calendar;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        ButterKnife.bind(this);
        initializedViews();
        medicineRepository = new MedicineRepository(NewMedicineActivity.this);

        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @OnClick(R.id.edit_expiration)
    public void onClickEditExpiration(View view) {

        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);


    }


    @OnClick(R.id.edit_type)
    public void onClickEditType(View view) {

        new MaterialDialog.Builder(this)
                .title("Select Type")
                .items(R.array.type)
                .itemsCallbackSingleChoice(-1, (dialog, view1, which, text) -> {

                    edit_type.setText(text);
                    return true;
                })
                .positiveText("Submit")
                .show();

    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {

        new MaterialDialog.Builder(NewMedicineActivity.this)
                .title("Save Medicine?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {

                    String expirationDateString = edit_expiration.getText().toString();
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date expirationDate;
                    long milliseconds = 0;
                    try {
                        expirationDate = df.parse(expirationDateString);
                        milliseconds = expirationDate.getTime();

                    } catch (ParseException e) {
                        Log.d("Error", e.getMessage());
                    }


                    try {
                        String uuid = UUID.randomUUID().toString();
                        boolean isCreated = medicineRepository.create(new Medicine(uuid, edit_name.getText().toString(), edit_generic_name.getText().toString(), edit_diagnosis.getText().toString(), edit_description.getText().toString(), milliseconds, Integer.parseInt(edit_total.getText().toString()), null, edit_type.getText().toString()));

                        if (isCreated) {
                            Intent intent = new Intent(NewMedicineActivity.this, MedicineActivity.class);
                            AppController appController = AppController.getInstance();
                            appController.setMedicineId(medicineRepository.getByUuid(uuid).getId());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                        if (!isCreated) {
                            Snackbar.make(findViewById(android.R.id.content), "Failed to Save Medicine!", Snackbar.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }


                })
                .onNegative((dialog, which) -> {
                }).show();

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        month = month + 1;
        String smonth = month + "";
        String sday = day + "";
        if (month < 10) {
            smonth = "0" + month;
        }
        if (day < 10) {
            sday = "0" + day;
        }
        edit_expiration.setText(sday + "-" + smonth + "-" + year);
    }


    public void initializedViews() {
        edit_expiration = findViewById(R.id.edit_expiration);
        edit_type = findViewById(R.id.edit_type);
        edit_name = findViewById(R.id.edit_name);
        edit_generic_name = findViewById(R.id.edit_generic_name);
        edit_description = findViewById(R.id.edit_description);
        edit_diagnosis = findViewById(R.id.edit_diagnosis);
        edit_total = findViewById(R.id.edit_total);

    }
}
