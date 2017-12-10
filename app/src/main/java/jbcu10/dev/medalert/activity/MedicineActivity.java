package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

public class MedicineActivity extends BaseActivity {
    public MedicineRepository medicineRepository;
    TextView txt_name, txt_generic_name, txt_description, txt_diagnosis, txt_expiration, txt_doctor_name;
    Medicine medicine = null;
    ImageView image_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        initialize();
        setMedicineValue(medicine);
    }

    private void initialize() {
        txt_diagnosis = findViewById(R.id.txt_diagnosis);
        txt_name = findViewById(R.id.txt_name);
        txt_generic_name = findViewById(R.id.txt_generic_name);
        txt_description = findViewById(R.id.txt_description);
        txt_expiration = findViewById(R.id.txt_expiration);
        txt_doctor_name = findViewById(R.id.txt_doctor_name);
        image_type = findViewById(R.id.image_type);
        medicineRepository = new MedicineRepository(MedicineActivity.this);
        HomeActivity.selectedItem = 1;
        AppController appController = AppController.getInstance();
        medicine = medicineRepository.getById(appController.getMedicineId());

    }

    private void setMedicineValue(Medicine medicine) {

        txt_name.setText(medicine.getName());
        txt_generic_name.setText(medicine.getGenericName());
        txt_description.setText(medicine.getDescription());
        txt_diagnosis.setText(medicine.getDiagnosis());
        if (medicine.getType() != null && medicine.getType().equals("Tablet")) {
            image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_lozenge));
        }
        if (medicine.getType() != null && medicine.getType().equals("Injectible")) {
            image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_syringe));
        }
        if (medicine.getType() != null && medicine.getType().equals("Capsule")) {
            image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill));
        }
        if (medicine.getType() != null && medicine.getType().equals("Ointment")) {
            image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_ointment));
        }
        Date date = new Date(medicine.getExpiration());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        txt_expiration.setText(df.format(date));
        txt_doctor_name.setText(medicine.getDoctor() != null ? medicine.getDoctor().getFirstName() + " " + medicine.getDoctor().getFirstName() : "Not Available..");

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
                this.editMedicine();
                return true;
            case R.id.delete_medicine:
                this.deleteMedicine();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editMedicine() {
        AppController appController = AppController.getInstance();
        appController.setMedicine(medicine);
        Intent intent = new Intent(MedicineActivity.this, EditMedicineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void deleteMedicine() {
        new MaterialDialog.Builder(MedicineActivity.this)
                .title("Delete Medicine?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            boolean isDeleted = medicineRepository.deleteById(medicine.getId());
                            if (isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Successfully Deleted Medicine!", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(MedicineActivity.this, HomeActivity.class);
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
}
