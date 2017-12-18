package jbcu10.dev.medalert.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import java.util.UUID;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.FirstAidConstant;
import jbcu10.dev.medalert.config.Permission;
import jbcu10.dev.medalert.db.FirstAidRepository;
import jbcu10.dev.medalert.location.GPSTracker;
import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Instructions;

public class SplashScreenActivity extends AppCompatActivity {
    public FirstAidRepository firstAidRepository;
    ProgressBar progressbar;
    Intent intent;
    GPSTracker gps;
    double latitude, longitude;
    Permission permission = new Permission();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        firstAidRepository = new FirstAidRepository(SplashScreenActivity.this);
        if (firstAidRepository.getAll() == null) {
            for (int a = 0; a < 8; a++) {
                String firstAidUuid = UUID.randomUUID().toString();
                FirstAid firstAid = new FirstAid();
                firstAid.setName(FirstAidConstant.firstaid[a][0]);
                firstAid.setDescription("First aid for "+FirstAidConstant.firstaid[a][0]+".");
                firstAid.setUuid(firstAidUuid);
                for(int b =1;b<5;b++) {
                    if (FirstAidConstant.firstaid[a][b] != null&& !FirstAidConstant.firstaid[a][b].equals("")) {
                        Instructions instructions = new Instructions();
                        instructions.setInstruction(b +". "+ FirstAidConstant.firstaid[a][b]);
                        instructions.setUuid(UUID.randomUUID().toString());
                        firstAidRepository.createInstruction(firstAidUuid, instructions);
                    }
                }
                firstAidRepository.create(firstAid);

            }
        }


        changePermission();

        gps = new GPSTracker(SplashScreenActivity.this);
        if (gps.canGetLocation())

        {

            latitude = gps.getLatitude();

        } else

        {
            gps.showSettingsAlert();
        }
        progressbar = findViewById(R.id.progressBar2);
        final Handler handler = new Handler();
        Runnable r = () -> {
            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        };
        handler.postDelayed(r, 3000);


    }
    public void changePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 0);
        }if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }
}
