package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import java.util.UUID;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.Permission;
import jbcu10.dev.medalert.db.FirstAidRepository;
import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Instructions;

public class SplashScreenActivity extends AppCompatActivity {
    public FirstAidRepository firstAidRepository;
    ProgressBar progressbar;
    Intent intent;
    Permission permission =new Permission();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        firstAidRepository = new FirstAidRepository(SplashScreenActivity.this);
        permission.requestSendSmsPermission(this);
        if (firstAidRepository.getAll() == null) {
            for (int a = 0; a < 10; a++) {
                String firstAidUuid = UUID.randomUUID().toString();
                FirstAid firstAid = new FirstAid();
                firstAid.setName("FirstAid: " + a);
                firstAid.setDescription("Lorem Ipsum Dolorsit amit");
                firstAid.setUuid(firstAidUuid);
                for (int b = 0; b < 3; b++) {
                    Instructions instructions = new Instructions();
                    instructions.setInstruction((b + 1) + ". Lorem Ipsum Dolorsit amit.");
                    instructions.setUuid(UUID.randomUUID().toString());
                    firstAidRepository.createInstruction(firstAidUuid, instructions);
                }
                firstAidRepository.create(firstAid);
            }
        }


        progressbar = findViewById(R.id.progressBar2);
        final Handler handler = new Handler();
        Runnable r = () -> {

            intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        };
        handler.postDelayed(r, 5000);
    }


}
