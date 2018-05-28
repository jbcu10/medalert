package jbcu10.dev.medalert.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Relative;
import jbcu10.dev.medalert.model.Reminder;

public class DialogActivity extends Activity {
    public MedicineRepository medicineRepository;
    public ReminderRepository reminderRepository;
    public PatientRepository patientRepository;
    public RelativeRepository relativeRepository;
    Reminder reminder = null;
    Patient patient = null;
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
        relativeRepository = new RelativeRepository(this);
        AppController appController = AppController.getInstance();
        reminder = reminderRepository.getById(appController.getReminderId());


        patient = patientRepository.getReminderPatientByReminderUuid(reminder.getUuid());

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
                textView.setText(a + ". " + medicine.getName() + " - " + medicine.getDosage() + " - " +
                        medicine.getStock()+" remaining - "+medicine.getTotal());
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
        sendCurrentStock();

        Intent gotoMain = new Intent(this,HomeActivity.class);
        startActivity(gotoMain);
        finish();
    }@OnClick(R.id.img_close)
    public void onClickImgClose(View view) {
        sendCurrentStock();
        exitApplication();
    }

    public void sendCurrentStock(){
        List<Relative> relatives = relativeRepository.getAllRelativeByPatienUuid(patient.getUuid());
        StringBuilder stringBuffer = new StringBuilder();
        int a = 1;
        List<Medicine> medicines =medicineRepository.getAllReminderMedicine(reminder.getUuid());
        for(Medicine medicine:medicines){
            medicine.setStock(medicine.getStock()-medicine.getTotal());
            medicineRepository.update(medicine);
            String medicineString = medicine.getName() + " - " + medicine.getDosage() + " - " + medicine.getStock()+" remaining";

            if(medicine.getStock()<=2) {
                sendSms(relatives,"Medicine is almost out of stock.\n\n"+medicineString);
            }
            stringBuffer.append(a).append(". ").append(medicineString).append("\n");
            a++;
        }
        stringBuffer.append(reminder.getDescription());
        String sms =  "Medicine Taken\n\n"+stringBuffer;
        sendSms(relatives,sms);
    }

    public void sendSms(List<Relative> relatives, String sms) {
        if(relatives!=null) {
            for (Relative relative : relatives) {
                if (relative.isNotify()) {
                    try {
                        Log.d("sms", sms);
                        Log.d("relative", relative.getContactNumber());

                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> msgArray = smsManager.divideMessage(sms);

                        smsManager.sendMultipartTextMessage(relative.getContactNumber(), null, msgArray, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again later!",
                                Toast.LENGTH_LONG).show();
                        Log.e("sms error", e.getMessage());
                    }
                }
            }
        }
    }

}
