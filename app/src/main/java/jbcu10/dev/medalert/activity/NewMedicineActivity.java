package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.MedicineHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.Medicine;

public class NewMedicineActivity extends MedicineHelperActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        ButterKnife.bind(this);
        initializedViews();
    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        if(isMedicineValid()) {
            new MaterialDialog.Builder(NewMedicineActivity.this)
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
                                String uuid = UUID.randomUUID().toString();
                                boolean isCreated = medicineRepository.create(new Medicine(uuid, edit_name.getText().toString(), edit_generic_name.getText().toString(), edit_diagnosis.getText().toString(), edit_description.getText().toString(), milliseconds, Integer.parseInt(edit_total.getText().toString()), null, edit_type.getText().toString(), true,edit_dosage.getText().toString()));

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

                        }
                    }).show();
        }
    }





}
