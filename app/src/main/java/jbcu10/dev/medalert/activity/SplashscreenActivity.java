package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import jbcu10.dev.medalert.R;
public class SplashScreenActivity extends AppCompatActivity {
    ProgressBar progressbar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        progressbar = (ProgressBar) findViewById(R.id.progressBar2);
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        };
        handler.postDelayed(r, 5000);
    }

}
