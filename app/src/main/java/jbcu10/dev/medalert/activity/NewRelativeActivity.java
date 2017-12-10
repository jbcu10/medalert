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

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.activity.helper.RelativeHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.model.Relative;

public class NewRelativeActivity extends RelativeHelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_relative);
        initializedViews();
    }


    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        new MaterialDialog.Builder(NewRelativeActivity.this)
                .title("Save Relative?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    try {
                        String uuid = UUID.randomUUID().toString();
                        boolean isCreated = relativeRepository.createPatientRelative(PatientActivity.patientUuid, new Relative(uuid,
                                edit_first_name.getText().toString(), edit_middle_name.getText().toString()
                                , edit_last_name.getText().toString(), edit_contact_number.getText().toString()
                                , edit_email.getText().toString(), edit_relationship.getText().toString()));

                        if (isCreated) {
                            Intent intent = new Intent(NewRelativeActivity.this, RelativeActivity.class);
                            AppController appController = AppController.getInstance();
                            appController.setRelativeId(relativeRepository.getByUuid(uuid).getId());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                        if (!isCreated) {
                            Snackbar.make(findViewById(android.R.id.content), "Failed to Save Relative!", Snackbar.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }

                }) .show();

    }

}
