package jbcu10.dev.medalert.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jbcu10.dev.medalert.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragments extends Fragment {


    public ReminderFragments() {
        // Required empty public constructor
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        getActivity().setTitle("Reminders");

        return rootView;
    }

}
