package jbcu10.dev.medalert.activity.helper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.EditPatientActivity;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.model.Patient;

/**
 * Created by dev on 12/10/17.
 */

public class PatientHelperActivity extends BaseActivity {
    public PatientRepository patientRepository;
    @BindView(R.id.edit_first_name)
    protected  EditText edit_first_name;
    @BindView(R.id.edit_middle_name)
    protected EditText edit_middle_name;
    @BindView(R.id.edit_last_name)
    protected EditText edit_last_name;
    @BindView(R.id.edit_contact_number)
    protected EditText edit_contact_number;
    @BindView(R.id.edit_email)
    protected EditText edit_email;
    @BindView(R.id.edit_gender)
    protected EditText edit_gender;
    @BindView(R.id.button_submit)
    protected Button button_submit;
    public Patient patient;

    public void initializedViews() {
        ButterKnife.bind(this);
        patientRepository = new PatientRepository(this);
        edit_first_name = findViewById(R.id.edit_first_name);
        edit_middle_name = findViewById(R.id.edit_middle_name);
        edit_last_name = findViewById(R.id.edit_last_name);
        edit_contact_number = findViewById(R.id.edit_contact_number);
        edit_email = findViewById(R.id.edit_email);
        edit_gender = findViewById(R.id.edit_gender);
        HomeActivity.selectedItem =2;
    }

    public boolean isPatientValid(){
        if(!isValid(edit_first_name.getText().toString())){
            showError("First name");
            return false;
        }if(!isValid(edit_middle_name.getText().toString())){
            showError("Middle name");
            return false;
        }if(!isValid(edit_last_name.getText().toString())){
            showError("Last name");
            return false;
        }
        if(!isValid(edit_contact_number.getText().toString())){
            showError("Contact number");
            return false;
        }
        if(!isValid(edit_email.getText().toString())){
            showError("Email");
            return false;
        }
        if(!isValid(edit_gender.getText().toString())){
            showError("Gender");
            return false;
        }

        return true;
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
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
