package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.fragments.RelativeFragments;
import jbcu10.dev.medalert.fragments.ReminderFragments;
import jbcu10.dev.medalert.model.Patient;

public class PatientActivity extends BaseActivity {
    private static final String TAG = PatientActivity.class.getSimpleName();
    public PatientRepository patientRepository;
    TextView txt_name, txt_relation, txt_contact_number, txt_email;
    ImageView image_gender;
    Patient patient;
    Fragment fragment;
    public static int destination = 0;
    public static String patientUuid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ButterKnife.bind(this);
        patientRepository = new PatientRepository(PatientActivity.this);
        AppController appController = AppController.getInstance();
        patient = patientRepository.getById(appController.getPatientId());
        HomeActivity.selectedItem =2;

        patientUuid = patient.getUuid();
        initializeView();
        fragment = new RelativeFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_patient, fragment);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(destination==0) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    private void initializeView() {
        txt_name = findViewById(R.id.txt_name);
        txt_relation = findViewById(R.id.txt_relation);
        txt_contact_number = findViewById(R.id.txt_contact_number);
        txt_email = findViewById(R.id.txt_email);
        image_gender = findViewById(R.id.image_relation);
        txt_name.setText(patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName());
        txt_contact_number.setText(patient.getContactNumber());
        txt_email.setText(patient.getEmail());
        txt_relation.setText(patient.getGender());
        image_gender.setImageDrawable(this.getImageGender(patient) != null ? this.getImageGender(patient) : getResources().getDrawable(R.drawable.male));


    }

    private Drawable getImageGender(Patient patient) {
        Log.d("Gender", patient.getGender());
        if (patient.getGender() != null && patient.getGender().toLowerCase().equals("female")) {
            return this.getResources().getDrawable(R.drawable.female);
        }
        return this.getResources().getDrawable(R.drawable.male);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.edit_medicine:
                this.editPatient();
                return true;
            case R.id.delete_medicine:
                this.deletePatient();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deletePatient() {
        new MaterialDialog.Builder(PatientActivity.this)
                .title("Delete Patient?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            boolean isDeleted = patientRepository.deleteById(patient.getId());
                            if (isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Successfully Deleted Medicine!", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(PatientActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }
                            if (!isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Delete Medicine!", Snackbar.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }

                    }
                }).show();


    }

    private void editPatient() {
        AppController appController = AppController.getInstance();
        appController.setPatient(patient);
        Intent intent = new Intent(PatientActivity.this, EditPatientActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


}
