package jbcu10.dev.medalert.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.activity.NewMedicineActivity;
import jbcu10.dev.medalert.adapter.MedicineAdapter;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

/**
 * Created by dev on 10/11/17.
 */

public class MedicineFragments extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    public MedicineRepository medicineRepository;
    private static final String LOADING_PLOTS = "Loading Medicines...";
    private static final String ERROR = "Error:";
    ProgressDialog pDialog;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private MedicineAdapter mAdapter;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public MedicineFragments() {
        // Required empty public constructor
    }
    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_medicines, container, false);
        ButterKnife.bind(this,rootView);
        medicineRepository = new MedicineRepository(getActivity());
        pDialog = new ProgressDialog(getActivity());
        getActivity().setTitle("Medicines");

        List<Medicine> medicines = medicineRepository.getAll();
        initializeGridView();
        if(medicines!=null) {
            onLoadMoreItems(medicines);
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

    private void showDialog() {
        if (!pDialog.isShowing()) pDialog.show();
    }


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initializeGridView() {
        mAdapter = new MedicineAdapter(getActivity(), R.id.txt_name, R.id.imageView);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setListAdapter(mAdapter);
            }

        });
    }
    private void onLoadMoreItems(List<Medicine> medicines) {
        for (Medicine data : medicines) {
            Log.d("medicine",medicines.toString());
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

    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent intent = new Intent(getActivity(), NewMedicineActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
