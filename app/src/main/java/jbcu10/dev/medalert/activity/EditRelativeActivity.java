package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.DatabaseCRUDHandler;
import jbcu10.dev.medalert.model.Relative;

public class EditRelativeActivity extends AppCompatActivity {
    Relative relative;
    @BindView(R.id.edit_first_name)
    EditText edit_first_name;
    @BindView(R.id.edit_middle_name) EditText edit_middle_name;
    @BindView(R.id.edit_last_name) EditText edit_last_name;
    @BindView(R.id.edit_contact_number) EditText edit_contact_number;
    @BindView(R.id.edit_email) EditText edit_email;
    @BindView(R.id.edit_relationship) EditText edit_relationship;
    @BindView(R.id.button_submit)
    Button button_submit;
    public DatabaseCRUDHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_relative);

        ButterKnife.bind(this);
        db = new DatabaseCRUDHandler(EditRelativeActivity.this);


        AppController appController = AppController.getInstance();
        relative = appController.getRelative();
        initializedViews();

    }

    public void initializedViews(){
        edit_first_name = findViewById(R.id.edit_first_name);
        edit_middle_name = findViewById(R.id.edit_middle_name);
        edit_last_name = findViewById(R.id.edit_last_name);
        edit_contact_number = findViewById(R.id.edit_contact_number);
        edit_email = findViewById(R.id.edit_email);
        edit_relationship = findViewById(R.id.edit_relationship);
        edit_first_name.setText(relative.getFirstName());
        edit_middle_name.setText(relative.getMiddleName());
        edit_last_name.setText(relative.getLastName());
        edit_contact_number.setText(relative.getContactNumber());
        edit_email.setText(relative.getEmail());
        edit_relationship.setText(relative.getRelationship());
    }

    @OnClick(R.id.edit_relationship)
    public void onClickEditType(View view) {

        new MaterialDialog.Builder(this)
                .title("Select Relationship")
                .items(R.array.relation)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        edit_relationship.setText(text);
                        return true;
                    }
                })
                .positiveText("Select")
                .show();

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
                        try{
                            boolean isCreated = db.updateRelative(new Relative(relative.getId(),UUID.randomUUID().toString(),
                                    edit_first_name.getText().toString(),edit_middle_name.getText().toString()
                                    ,edit_last_name.getText().toString(),edit_contact_number.getText().toString()
                                    , edit_email.getText().toString(),edit_relationship.getText().toString()));

                            if(isCreated){
                                Intent intent = new Intent(EditRelativeActivity.this, RelativeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }if(!isCreated){
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Save Relative!", Snackbar.LENGTH_LONG).show();
                            }

                        }
                        catch (Exception e){
                            Log.d("Error",e.getMessage());
                        }


                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                }).show();

    }
}
