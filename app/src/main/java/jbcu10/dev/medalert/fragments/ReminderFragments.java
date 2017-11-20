package jbcu10.dev.medalert.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.NewRemindersActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragments extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    public ReminderFragments() {
        // Required empty public constructor
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        ButterKnife.bind(this,rootView);
        getActivity().setTitle("Reminders");

        return rootView;
    }
    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent intent = new Intent(getActivity(), NewRemindersActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
