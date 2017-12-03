package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.model.Patient;

public class EditPatientActivity extends AppCompatActivity {
    public PatientRepository patientRepository;
    @BindView(R.id.edit_first_name)
    EditText edit_first_name;
    @BindView(R.id.edit_middle_name)
    EditText edit_middle_name;
    @BindView(R.id.edit_last_name)
    EditText edit_last_name;
    @BindView(R.id.edit_contact_number)
    EditText edit_contact_number;
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_gender)
    EditText edit_gender;
    @BindView(R.id.button_submit)
    Button button_submit;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);
        ButterKnife.bind(this);
        patientRepository = new PatientRepository(EditPatientActivity.this);
        AppController appController = AppController.getInstance();
        patient = appController.getPatient();
        initializedViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public void initializedViews() {
        edit_first_name = findViewById(R.id.edit_first_name);
        edit_middle_name = findViewById(R.id.edit_middle_name);
        edit_last_name = findViewById(R.id.edit_last_name);
        edit_contact_number = findViewById(R.id.edit_contact_number);
        edit_email = findViewById(R.id.edit_email);
        edit_gender = findViewById(R.id.edit_gender);
        edit_first_name.setText(patient.getFirstName());
        edit_middle_name.setText(patient.getMiddleName());
        edit_last_name.setText(patient.getLastName());
        edit_contact_number.setText(patient.getContactNumber());
        edit_email.setText(patient.getEmail());
        edit_gender.setText(patient.getGender());
    }

    @OnClick(R.id.edit_gender)
    public void onClickEditType(View view) {

        new MaterialDialog.Builder(this)
                .title("Select Gender")
                .items(R.array.gender)
                .itemsCallbackSingleChoice(-1, (dialog, view1, which, text) -> {

                    edit_gender.setText(text);
                    return true;
                })
                .positiveText("Select")
                .show();

    }


    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        new MaterialDialog.Builder(EditPatientActivity.this)
                .title("Save Patient?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                    try {
                        String uuid = UUID.randomUUID().toString();
                        boolean isCreated = patientRepository.update(new Patient(patient.getId(), uuid,
                                edit_first_name.getText().toString(), edit_middle_name.getText().toString()
                                , edit_last_name.getText().toString(), edit_contact_number.getText().toString()
                                , edit_email.getText().toString(), edit_gender.getText().toString()));

                        if (isCreated) {
                            Intent intent = new Intent(EditPatientActivity.this, PatientActivity.class);
                            AppController appController = AppController.getInstance();
                            appController.setPatientId(patientRepository.getByUuid(uuid).getId());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                        if (!isCreated) {
                            Snackbar.make(findViewById(android.R.id.content), "Failed to Save Patient!", Snackbar.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }


                })
                .onNegative((dialog, which) -> {
                }).show();

    }
}
