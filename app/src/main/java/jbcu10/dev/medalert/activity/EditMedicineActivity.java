package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.MedicineHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

public class EditMedicineActivity extends MedicineHelperActivity implements DatePickerDialog.OnDateSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        ButterKnife.bind(this);
        initializedViews();
        AppController appController = AppController.getInstance();
        medicine = appController.getMedicine();
        setMedicineValue(medicine);
    }

    private void setMedicineValue(Medicine medicine) {
        edit_name.setText(medicine.getName());
        edit_generic_name.setText(medicine.getGenericName());
        edit_description.setText(medicine.getDescription());
        edit_diagnosis.setText(medicine.getDiagnosis());
        Date date = new Date(medicine.getExpiration());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        edit_expiration.setText(df.format(date));
        edit_type.setText(medicine.getType());
        edit_total.setText(String.valueOf(medicine.getTotal()));
        edit_dosage.setText(medicine.getDosage());
        edit_stock.setText(medicine.getStock());
    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        if (isMedicineValid()) {
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
                            long milliseconds = 0;
                            try {
                                expirationDate = df.parse(expirationDateString);
                                milliseconds = expirationDate.getTime();

                            } catch (ParseException e) {
                                Log.d("Error", e.getMessage());
                            }


                            try {
                                boolean isCreated = medicineRepository.update(new Medicine(medicine.getId(), medicine.getUuid(), edit_name.getText().toString(), edit_generic_name.getText().toString(), edit_diagnosis.getText().toString(), edit_description.getText().toString(), milliseconds, Integer.parseInt(edit_total.getText().toString()), null, edit_type.getText().toString(), medicine.isEnabled(),edit_dosage.getText().toString(),Integer.parseInt(edit_stock.getText().toString())));
                                if (isCreated) {
                                    Intent intent = new Intent(EditMedicineActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                                if (!isCreated) {
                                    Snackbar.make(findViewById(android.R.id.content), "Failed to Save Medicine!", Snackbar.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.d("Error", e.getMessage());
                            }
                        }

                    }).show();
        }

    }

}
