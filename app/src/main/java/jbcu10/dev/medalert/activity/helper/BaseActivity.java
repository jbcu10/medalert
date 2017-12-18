package jbcu10.dev.medalert.activity.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.activity.NewMedicineActivity;
import jbcu10.dev.medalert.activity.NewPatientActivity;
import jbcu10.dev.medalert.db.TokenRepository;
import jbcu10.dev.medalert.model.Token;

/**
 * Created by jb on 25/09/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void exitApplication(Activity activity) {
        new MaterialDialog.Builder(activity)
                .title("Exit Application")
                .content("Are you sure you want to Exit?")
                .positiveText("Logout")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        TokenRepository tokenRepository = new TokenRepository(activity);
                        tokenRepository.deleteById(1);
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                }).neutralText("Exit without Logging Out")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                }).negativeText("Cancel")
                .show();
    }

    public void addMedicine(final Activity activity, String contentMedicine) {
        new MaterialDialog.Builder(activity)
                .title("Add Medicine")
                .content(contentMedicine)
                .positiveText("Create")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                        Intent intent = new Intent(activity, NewMedicineActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .cancelable(false)
                .show();
    }

    public void addPatient(final Activity activity, String contentPatient) {

        new MaterialDialog.Builder(activity)
                .title("Add Patient")
                .content(contentPatient)
                .positiveText("Create")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                        Intent intent = new Intent(activity, NewPatientActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).negativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Intent intent = new Intent(activity, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .cancelable(false)
                .show();
    }

    public boolean isValid(String string){
        return  string!=null&&!string.isEmpty();
    }
    public void showError(String field){
        Snackbar.make(findViewById(android.R.id.content), field+" is missing!", Snackbar.LENGTH_LONG).show();
    }
}
