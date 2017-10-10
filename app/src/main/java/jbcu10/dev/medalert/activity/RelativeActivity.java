package jbcu10.dev.medalert.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.adapter.MedicineAdapter;
import jbcu10.dev.medalert.adapter.RelativeAdapter;
import jbcu10.dev.medalert.db.DatabaseHandler;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Relative;

public class RelativeActivity extends BaseActivity implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private static final String TAG = MainActivity.class.getSimpleName();
    public DatabaseHandler db;
    private static final String LOADING_PLOTS = "Loading Relatives...";
    private static final String ERROR = "Error:";
    ProgressDialog pDialog;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private RelativeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative);
        ButterKnife.bind(this);
        db = new DatabaseHandler(RelativeActivity.this);
        pDialog = new ProgressDialog(this);

        List<Relative> relatives = db.getAllRelative();
        initializeGridView();
        Log.d("relative total",relatives.size()+"");
        if(relatives!=null) {
            onLoadMoreItems(relatives);
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent intent = new Intent(RelativeActivity.this, NewRelativeActivity.class);
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
        mGridView = findViewById(R.id.grid_view);
        mAdapter = new RelativeAdapter(this, R.id.txt_name, R.id.image_relation);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
    }
    private void onLoadMoreItems(List<Relative> relatives) {
        for (Relative data : relatives) {
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
        hideDialog();
    }
}
