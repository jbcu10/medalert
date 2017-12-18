package jbcu10.dev.medalert.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Reminder;

public class DialogActivity extends Activity {
    public MedicineRepository medicineRepository;
    public ReminderRepository reminderRepository;
    public PatientRepository patientRepository;
    Reminder reminder = null;
    TextView txt_title;
    LinearLayout ll_view;
    @BindView(R.id.img_close)
    protected ImageButton img_close;
    @BindView(R.id.img_forward)
    protected ImageButton img_forward;
    protected Button button_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.activity_dialog);
        initialize();
    }

    private void initialize(){
        ButterKnife.bind(this);
        txt_title = findViewById(R.id.txt_title);
        ll_view = findViewById(R.id.ll_view);
        medicineRepository = new MedicineRepository(this);
        reminderRepository = new ReminderRepository(this);
        patientRepository = new PatientRepository(this);
        AppController appController = AppController.getInstance();
        reminder = reminderRepository.getById(appController.getReminderId());
        if(reminder ==null){
            reminder=      reminderRepository.getByUuid(appController.getReminderUuid());
        }
        setView(reminder);
    }
    private void setView(Reminder reminder) {
        txt_title.setText("Reminder for: " + patientRepository.getReminderPatientByReminderUuid(reminder.getUuid()).toString());

        List<Medicine> medicines = medicineRepository.getAllReminderMedicine(reminder.getUuid());
        int a = 1;
        for (Medicine medicine : medicines) {
                 TextView textView = new TextView(this);
                textView.setText(a + ". " + medicine.getName() + " - " + medicine.getDosage() + " - " + medicine.getTotal());
                textView.setId(medicine.getId());
                textView.setTextColor(Color.BLACK);
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.setMargins(10, 10, 10, 10);
                textView.setLayoutParams(ll);

                ll_view.addView(textView);
                a++;

        }
        TextView textView = new TextView(this);
        textView.setText(reminder.getDescription());
        textView.setId(reminder.getId());
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1));

        ll_view.addView(textView);

    }

    public void exitApplication() {

                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();

    }

    @OnClick(R.id.img_forward)
    public void onClickImgForward(View view) {
        Intent gotoMain = new Intent(this,HomeActivity.class);
        startActivity(gotoMain);
        finish();
    }@OnClick(R.id.img_close)
    public void onClickImgClose(View view) {
        exitApplication();
    }
}
