package jbcu10.dev.medalert.fragments;

import android.app.ProgressDialog;
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
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.adapter.FirstAidAdapter;
import jbcu10.dev.medalert.db.DatabaseCRUDHandler;
import jbcu10.dev.medalert.model.FirstAid;

/**
 * Created by dev on 10/12/17.
 */

public class FirstAidFragments extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    public DatabaseCRUDHandler db;
    private static final String LOADING_PLOTS = "Loading First Aid...";
    private static final String ERROR = "Error:";
    ProgressDialog pDialog;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private FirstAidAdapter mAdapter;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    public FirstAidFragments() {
        // Required empty public constructor
    }
    View rootView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_firstaid, null, false);
        ButterKnife.bind(this,rootView);
        db = new DatabaseCRUDHandler(getActivity());
        pDialog = new ProgressDialog(getActivity());

        getActivity().setTitle("First Aid");
        List<FirstAid> relatives = db.getAllFirstAid();
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

    


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initializeGridView() {
        mAdapter = new FirstAidAdapter(getActivity(), R.id.txt_name, R.id.imageView);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setListAdapter(mAdapter);
            }

        });
    }
    private void onLoadMoreItems(List<FirstAid> relatives) {
        for (FirstAid data : relatives) {
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
