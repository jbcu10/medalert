package jbcu10.dev.medalert.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.db.DatabaseHandler;

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
