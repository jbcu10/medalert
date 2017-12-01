package jbcu10.dev.medalert.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.ReminderActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.model.Reminder;

/**
 * Created by dev on 10/14/17.
 */

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private final LayoutInflater mLayoutInflater;
    PatientRepository patientRepository;

    public ReminderAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ReminderAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_reminder, parent, false);
            viewHolder = new ReminderAdapter.ViewHolder();
            viewHolder.txt_description = convertView.findViewById(R.id.txt_description);
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ReminderAdapter.ViewHolder) convertView.getTag();
        }
        final Reminder reminder = getItem(position);
        Activity activity = (Activity) getContext();
        patientRepository = new PatientRepository(activity);
        viewHolder.txt_name.setText("Reminder for: " + patientRepository.getReminderPatientByReminderUuid(reminder.getUuid()).toString());
        viewHolder.txt_description.setText(reminder.getDescription());

        convertView.setOnClickListener(view -> {
                    try {
                        Log.d("id", "first aid " + reminder.getId());

                        AppController appController = AppController.getInstance();
                        appController.setReminderId(reminder.getId());
                        Intent intent = new Intent(getContext(), ReminderActivity.class);
                        getContext().startActivity(intent);

                        Activity activity1 = (Activity) getContext();
                        activity1.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
        );


        return convertView;
    }

    static class ViewHolder {
        TextView txt_name, txt_description;

    }

}
