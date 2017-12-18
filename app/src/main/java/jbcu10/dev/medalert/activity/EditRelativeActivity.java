package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import jbcu10.dev.medalert.activity.helper.RelativeHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.model.Relative;

public class EditRelativeActivity extends RelativeHelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_relative);
        AppController appController = AppController.getInstance();
        relative = appController.getRelative();
        initializedViews();
        setRelativeValue(relative);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        PatientActivity.destination=0;
    }


    private void setRelativeValue(Relative relative){
        edit_first_name.setText(relative.getFirstName());
        edit_middle_name.setText(relative.getMiddleName());
        edit_last_name.setText(relative.getLastName());
        edit_contact_number.setText(relative.getContactNumber());
        edit_email.setText(relative.getEmail());
        edit_relationship.setText(relative.getRelationship());
    }
    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {

        new MaterialDialog.Builder(EditRelativeActivity.this)
                .title("Save Relative?")
                .content("Are you sure you want save this items?")
                .positiveText("Save")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            boolean isCreated = relativeRepository.update(new Relative(relative.getId(), relative.getUuid().toString(),
                                    edit_first_name.getText().toString(), edit_middle_name.getText().toString()
                                    , edit_last_name.getText().toString(), edit_contact_number.getText().toString()
                                    , edit_email.getText().toString(), edit_relationship.getText().toString(),true));

                            if (isCreated) {
                                Intent intent = new Intent(EditRelativeActivity.this, RelativeActivity.class);
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
                }).show();

    }
}
