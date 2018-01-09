package jbcu10.dev.medalert.activity.helper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

/**
 * Created by dev on 12/10/17.
 */

public class MedicineHelperActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edit_expiration)
    protected EditText edit_expiration;
    @BindView(R.id.edit_type)
    protected EditText edit_type;
    @BindView(R.id.edit_name)
    protected EditText edit_name;
    @BindView(R.id.edit_generic_name)
    protected EditText edit_generic_name;
    @BindView(R.id.edit_description)
    protected EditText edit_description;
    @BindView(R.id.edit_diagnosis)
    protected EditText edit_diagnosis;
    @BindView(R.id.edit_total)
    protected EditText edit_total;
    @BindView(R.id.edit_dosage)
    protected EditText edit_dosage;
    @BindView(R.id.edit_stock)
    protected EditText edit_stock;
    @BindView(R.id.button_submit)


    protected Button button_submit;
    protected static final String DATEPICKER_TAG = "Date Picker";
    protected Calendar calendar;
    protected DatePickerDialog datePickerDialog;
    protected MedicineRepository medicineRepository;
    protected Medicine medicine = null;

    protected void initializedViews() {
        edit_expiration = findViewById(R.id.edit_expiration);
        edit_type = findViewById(R.id.edit_type);
        edit_name = findViewById(R.id.edit_name);
        edit_generic_name = findViewById(R.id.edit_generic_name);
        edit_description = findViewById(R.id.edit_description);
        edit_diagnosis = findViewById(R.id.edit_diagnosis);
        edit_total = findViewById(R.id.edit_total);
        edit_dosage = findViewById(R.id.edit_dosage);
        edit_stock = findViewById(R.id.edit_stock);
        medicineRepository = new MedicineRepository(this);
        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        HomeActivity.selectedItem = 1;

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }


    protected boolean isMedicineValid() {
        if (!isValid(edit_name.getText().toString())) {
            showError("Name");
            return false;
        }
        if (!isValid(edit_generic_name.getText().toString())) {
            showError("Generic name");
            return false;
        }
        if (!isValid(edit_diagnosis.getText().toString())) {
            showError("diagnosis");
            return false;
        }
        if (!isValid(edit_description.getText().toString())) {
            showError("Description");
            return false;
        }
        if (!isValid(edit_type.getText().toString())) {
            showError("Type");
            return false;
        }
        if (!isValid(edit_total.getText().toString())) {
            showError("Total");
            return false;
        }if (!isValid(edit_stock.getText().toString())) {
            showError("Stock");
            return false;
        }

        return true;
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


}


