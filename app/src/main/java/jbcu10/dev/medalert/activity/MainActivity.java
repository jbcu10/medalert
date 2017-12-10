package jbcu10.dev.medalert.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.adapter.MedicineAdapter;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

public class MainActivity extends BaseActivity implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOADING_PLOTS = "Loading Medicines...";
    private static final String ERROR = "Error:";
    public MedicineRepository medicineRepository;
    ProgressDialog pDialog;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private MedicineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        medicineRepository = new MedicineRepository(MainActivity.this);
        pDialog = new ProgressDialog(this);

        List<Medicine> medicines = medicineRepository.getAll();
        initializeGridView();
        if (medicines != null) {
            onLoadMoreItems(medicines);
        }
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

            case R.id.people:
                Intent intent = new Intent(MainActivity.this, RelativeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent intent = new Intent(MainActivity.this, NewMedicineActivity.class);
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
        //mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
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
