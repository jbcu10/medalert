package jbcu10.dev.medalert.activity;

import android.support.v7.app.AppCompatActivity;

import jbcu10.dev.medalert.R;

/**
 * Created by jb on 25/09/2017.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
