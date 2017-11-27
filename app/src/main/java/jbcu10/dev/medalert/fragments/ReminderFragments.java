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
import jbcu10.dev.medalert.activity.NewRemindersActivity;
import jbcu10.dev.medalert.adapter.ReminderAdapter;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Reminder;

/**
 * Created by dev on 10/11/17.
 */

public class ReminderFragments extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    public ReminderRepository reminderRepository;
    private static final String LOADING_PLOTS = "Loading Medicines...";
    private static final String ERROR = "Error:";
    ProgressDialog pDialog;
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private ReminderAdapter mAdapter;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public ReminderFragments() {
        // Required empty public constructor
    }
    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_medicines, container, false);
        ButterKnife.bind(this,rootView);
        reminderRepository = new ReminderRepository(getActivity());
        pDialog = new ProgressDialog(getActivity());
        getActivity().setTitle("Reminders");

        List<Reminder> reminders = reminderRepository.getAll();
        initializeGridView();
        if(reminders!=null) {
            onLoadMoreItems(reminders);
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
        mAdapter = new ReminderAdapter(getActivity(), R.id.txt_name, R.id.imageView);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setListAdapter(mAdapter);
            }

        });
    }
    private void onLoadMoreItems(List<Reminder> reminders) {
        for (Reminder data : reminders) {
            Log.d("reminders",reminders.toString());
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
        Intent intent = new Intent(getActivity(), NewRemindersActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
