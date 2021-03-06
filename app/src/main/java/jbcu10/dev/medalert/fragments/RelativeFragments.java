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

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.activity.NewRelativeActivity;
import jbcu10.dev.medalert.activity.PatientActivity;
import jbcu10.dev.medalert.adapter.RelativeAdapter;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Relative;

/**
 * Created by dev on 10/12/17.
 */

public class RelativeFragments extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String LOADING_PLOTS = "Loading Relatives...";
    private static final String ERROR = "Error:";
    public RelativeRepository relativeRepository;
    ProgressDialog pDialog;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    View rootView;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private RelativeAdapter mAdapter;

    public RelativeFragments() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_relative, null, false);
        ButterKnife.bind(this, rootView);
        relativeRepository = new RelativeRepository(getActivity());
        pDialog = new ProgressDialog(getActivity());

        getActivity().setTitle("Relatives");
        List<Relative> relatives = relativeRepository.getAll();
        if(PatientActivity.patientUuid !=null) {
            relatives = relativeRepository.getAllRelativeByPatienUuid(PatientActivity.patientUuid);
        }
        initializeGridView();
        if (relatives != null) {
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
        PatientActivity.destination =1;
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
