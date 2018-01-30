package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.PatientHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.Patient;

public class EditPatientActivity extends PatientHelperActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);
        AppController appController = AppController.getInstance();
        patient = appController.getPatient();
        initializedViews();
        setPatientValue(patient);

    }


    public void setPatientValue(Patient patient) {
        edit_first_name.setText(patient.getFirstName());
        edit_middle_name.setText(patient.getMiddleName());
        edit_last_name.setText(patient.getLastName());
        edit_contact_number.setText(patient.getContactNumber());
        edit_email.setText(patient.getEmail());
        edit_gender.setText(patient.getGender());
    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        new MaterialDialog.Builder(EditPatientActivity.this)
                .title("Save Patient?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            boolean isCreated = patientRepository.update(new Patient(patient.getId(), patient.getUuid(),
                                    edit_first_name.getText().toString(), edit_middle_name.getText().toString()
                                    , edit_last_name.getText().toString(), edit_contact_number.getText().toString()
                                    , edit_email.getText().toString(), edit_gender.getText().toString(),patient.getImageUri()));

                            if (isCreated) {
                                Intent intent = new Intent(EditPatientActivity.this, PatientActivity.class);
                                AppController appController = AppController.getInstance();
                                appController.setPatientId(patientRepository.getByUuid(patient.getUuid()).getId());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                            if (!isCreated) {
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Save Patient!", Snackbar.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }


                    }
                }).show();

    }
}
