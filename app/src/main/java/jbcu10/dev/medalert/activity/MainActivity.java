package jbcu10.dev.medalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.db.DatabaseHandler;
import jbcu10.dev.medalert.model.Medicine;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public DatabaseHandler db;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView1)
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = new DatabaseHandler(MainActivity.this);
        db.createMedicine(new Medicine(UUID.randomUUID().toString(),"biogesic","paracetamol","flu","Lorem ipsum dolorsit amit",10,10,null));

        Log.d(TAG,"medicine total: "+String.valueOf(db.getAllMedicine()!=null ? db.getAllMedicine().size():404 ));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.edit_medicine:

                return true;
            case R.id.delete_medicine:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.fab)
    public void onClickFAB(View view) {

        Intent  intent = new Intent(MainActivity.this, NewMedicineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @OnClick(R.id.textView2)
    public void onClickLL2(View view) {

        Intent  intent = new Intent(MainActivity.this, MedicineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }@OnClick(R.id.textView1)
    public void onClickLL1(View view) {

        Intent  intent = new Intent(MainActivity.this, MedicineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
