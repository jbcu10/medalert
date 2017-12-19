package jbcu10.dev.medalert.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.activity.NewRemindersActivity;
import jbcu10.dev.medalert.activity.ReminderActivity;
import jbcu10.dev.medalert.adapter.ReminderAdapter;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Reminder;

/**
 * Created by dev on 10/11/17.
 */

public class ReminderFragments extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String LOADING_PLOTS = "Loading Medicines...";
    private static final String ERROR = "Error:";
    public  ReminderRepository reminderRepository;
    public  PatientRepository patientRepository;
    ProgressDialog pDialog;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    View rootView;
    private StaggeredGridView mGridView;
    public  boolean mHasRequestedMore;
    public  ReminderAdapter mAdapter;

    public ReminderFragments() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_medicines, container, false);
        ButterKnife.bind(this, rootView);
        reminderRepository = new ReminderRepository(getActivity());
        patientRepository = new PatientRepository(getActivity());
        pDialog = new ProgressDialog(getActivity());
        getActivity().setTitle("Reminders");

        List<Reminder> reminders = reminderRepository.getAll();
        initializeGridView();
        if (reminders != null) {
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

        Reminder reminder =((Reminder) adapterView.getItemAtPosition(i));
        AppController appController = AppController.getInstance();
        appController.setReminderId(reminder.getId());
        Intent intent = new Intent(getContext(), ReminderActivity.class);
        getContext().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        /*new MaterialDialog.Builder(getContext())
                .title("Reminder for: " + patientRepository.getReminderPatientByReminderUuid(reminder.getUuid()).toString())
                .content("What do you want to do with this item?")
                .positiveText("View")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AppController appController = AppController.getInstance();
                        appController.setReminderId(reminder.getId());
                        Intent intent = new Intent(getContext(), ReminderActivity.class);
                        getContext().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                })
                .negativeText("Delete")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {

                            boolean isDeleted = reminderRepository.deleteById(reminder.getId());
                            if (isDeleted) {

                                List<Reminder> reminders = reminderRepository.getAll();
                                initializeGridView();
                                if (reminders != null) {
                                    onLoadMoreItems(reminders);
                                }
                            }
                            if (!isDeleted) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Failed to Delete Medicine!", Snackbar.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }


                    }
                }).show();*/
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
            Log.d("reminders", reminders.toString());
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
