package jbcu10.dev.medalert.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.UUID;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.FirstAidConstant;
import jbcu10.dev.medalert.config.Permission;
import jbcu10.dev.medalert.db.FirstAidRepository;
import jbcu10.dev.medalert.db.StoreRepository;
import jbcu10.dev.medalert.location.GPSTracker;
import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Instructions;
import jbcu10.dev.medalert.model.Store;

public class SplashScreenActivity extends AppCompatActivity {
    public FirstAidRepository firstAidRepository;
    public StoreRepository storeRepository;
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
        storeRepository = new StoreRepository(SplashScreenActivity.this);
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

        if(storeRepository.getAll() == null) {
            String[][] store = {{"Mercury drugs", "Mabini", "15.486544", "120.973581"},
                    {"Evamed pharmacy", "", "15.4886504", "120.9718253"},
                    {"TGP", "", "15.4887936", "120.9722788"},
                    {"Angel Drug Store", "", "15.4893923", "120.9715652"},
                    {"Manson Drug Store", "", "15.4884246", "120.9726107"},
                    {"Gapan Drug Store", "", "15.4897619", "120.9718253"},
                    {"South Star Drug", "", "15.488467", "120.9716307"}};

            for (int i = 0; i < store.length; i++) {
                Store store1 = new Store();
                store1.setUuid(UUID.randomUUID().toString());
                store1.setName(store[i][0]);
                store1.setAddress(store[i][1]);
                store1.setLat(Double.parseDouble(store[i][2]));
                store1.setLon(Double.parseDouble(store[i][3]));
                storeRepository.create(store1);
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
            intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        };
        handler.postDelayed(r, 3000);


    }
    public void changePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
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
