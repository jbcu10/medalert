package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import butterknife.ButterKnife;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Relative;

public class RelativeActivity extends BaseActivity {
    private static final String TAG = RelativeActivity.class.getSimpleName();
    public RelativeRepository relativeRepository;
    TextView txt_name, txt_relation, txt_contact_number, txt_email;
    ImageView image_relation;
    Relative relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative);
        ButterKnife.bind(this);
        relativeRepository = new RelativeRepository(RelativeActivity.this);
        AppController appController = AppController.getInstance();
        relative = relativeRepository.getById(appController.getRelativeId());
        initializeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        PatientActivity.destination=0;
    }

    private void initializeView() {
        txt_name = findViewById(R.id.txt_name);
        txt_relation = findViewById(R.id.txt_relation);
        txt_contact_number = findViewById(R.id.txt_contact_number);
        txt_email = findViewById(R.id.txt_email);
        image_relation = findViewById(R.id.image_relation);
        txt_name.setText(relative.getFirstName() + " " + relative.getMiddleName() + " " + relative.getLastName());
        txt_contact_number.setText(relative.getContactNumber());
        txt_email.setText(relative.getEmail());
        txt_relation.setText(relative.getRelationship());
        image_relation.setImageDrawable(this.getImageRelation(relative) != null ? this.getImageRelation(relative) : getResources().getDrawable(R.drawable.relative));


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
                this.editRelative();
                return true;
            case R.id.delete_medicine:
                this.deleteRelative();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Drawable getImageRelation(Relative relative) {
        try {
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("mother")) {
                return getResources().getDrawable(R.drawable.mother);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("father")) {
                return getResources().getDrawable(R.drawable.father);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("grand father")) {
                return getResources().getDrawable(R.drawable.grandfather);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("grand mother")) {
                return getResources().getDrawable(R.drawable.grandmother);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("sister")) {
                return getResources().getDrawable(R.drawable.daughter);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("brother")) {
                return getResources().getDrawable(R.drawable.boy);
            }
            return getResources().getDrawable(R.drawable.relative);

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }

    private void deleteRelative() {
        new MaterialDialog.Builder(RelativeActivity.this)
                .title("Delete Relative?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            boolean isDeleted = relativeRepository.deleteById(relative.getId());
                            if (isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Successfully Deleted Medicine!", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(RelativeActivity.this, HomeActivity.class);
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

    private void editRelative() {
        AppController appController = AppController.getInstance();
        appController.setRelative(relative);
        Intent intent = new Intent(RelativeActivity.this, EditRelativeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}
