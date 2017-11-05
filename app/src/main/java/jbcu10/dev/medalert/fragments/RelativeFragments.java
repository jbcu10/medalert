package jbcu10.dev.medalert.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.activity.NewRelativeActivity;
import jbcu10.dev.medalert.adapter.RelativeAdapter;
import jbcu10.dev.medalert.db.DatabaseHandler;
import jbcu10.dev.medalert.model.Relative;

/**
 * Created by dev on 10/12/17.
 */

public class RelativeFragments extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    public DatabaseHandler db;
    private static final String LOADING_PLOTS = "Loading Relatives...";
    private static final String ERROR = "Error:";
    ProgressDialog pDialog;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private RelativeAdapter mAdapter;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    public RelativeFragments() {
        // Required empty public constructor
    }
    View rootView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_relative, null, false);
        ButterKnife.bind(this,rootView);
        db = new DatabaseHandler(getActivity());
        pDialog = new ProgressDialog(getActivity());

        getActivity().setTitle("Relatives");
        List<Relative> relatives = db.getAllRelative();
        initializeGridView();
        if(relatives!=null) {
            onLoadMoreItems(relatives);
        }
        return rootView;

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

    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent intent = new Intent(getActivity(), NewRelativeActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initializeGridView() {
        mAdapter = new RelativeAdapter(getActivity(), R.id.txt_name, R.id.imageView);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setListAdapter(mAdapter);
            }

        });
    }
    private void onLoadMoreItems(List<Relative> relatives) {
        for (Relative data : relatives) {
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
        hideDialog();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(this);
    }

}
