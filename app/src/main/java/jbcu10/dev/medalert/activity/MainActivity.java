package jbcu10.dev.medalert.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.adapter.MedicineAdapter;
import jbcu10.dev.medalert.db.DatabaseHandler;
import jbcu10.dev.medalert.model.Medicine;

public class MainActivity extends BaseActivity implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public DatabaseHandler db;
    private static final String LOADING_PLOTS = "Loading Medicines...";
    private static final String ERROR = "Error:";
    ProgressDialog pDialog;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private MedicineAdapter mAdapter;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = new DatabaseHandler(MainActivity.this);
        pDialog = new ProgressDialog(this);

        List<Medicine> medicines = db.getAllMedicine();
        initializeGridView();
        if(medicines!=null) {
            onLoadMoreItems(medicines);
        }
    }
    
    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent  intent = new Intent(MainActivity.this, NewMedicineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initializeGridView() {
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        mAdapter = new MedicineAdapter(this, R.id.txt_name, R.id.imageView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
    }
    private void onLoadMoreItems(List<Medicine> medicines) {
        for (Medicine data : medicines) {
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
        hideDialog();
    }

}
