package jbcu10.dev.medalert.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.LinkedList;
import java.util.List;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.EditPatientActivity;
import jbcu10.dev.medalert.activity.HomeActivity;
import jbcu10.dev.medalert.activity.MedicineActivity;
import jbcu10.dev.medalert.activity.PatientActivity;
import jbcu10.dev.medalert.activity.ReminderActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.fragments.MapFragments;
import jbcu10.dev.medalert.fragments.ReminderFragments;
import jbcu10.dev.medalert.model.Patient;
import jbcu10.dev.medalert.model.Reminder;

/**
 * Created by dev on 10/14/17.
 */

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private final LayoutInflater mLayoutInflater;
    ReminderRepository reminderRepository;
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
            viewHolder.image_delete = convertView.findViewById(R.id.image_delete);
            viewHolder.switch1 = convertView.findViewById(R.id.switch1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ReminderAdapter.ViewHolder) convertView.getTag();
        }
        final Reminder reminder = getItem(position);
        Activity activity = (Activity) getContext();
        patientRepository = new PatientRepository(activity);
        reminderRepository = new ReminderRepository(activity);
        viewHolder.txt_name.setText("Reminder for: " + patientRepository.getReminderPatientByReminderUuid(reminder.getUuid()).toString());
        viewHolder.txt_description.setText(reminder.getDescription());
        viewHolder.switch1.setChecked(reminder.isTurnOn());
        viewHolder.switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reminder.setTurnOn(isChecked);
            reminderRepository.update(reminder);
            Log.v("Switch State=", ""+isChecked);}

        );

        convertView.setOnClickListener(view -> {
                    try {
                        Log.d("id", "first aid " + reminder.getId());

                        AppController appController = AppController.getInstance();
                        appController.setReminderId(reminder.getId());
                        Intent intent = new Intent(getContext(), ReminderActivity.class);
                        getContext().startActivity(intent);

                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
        );

        viewHolder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReminderById(reminder.getId());
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView txt_name, txt_description;
        ImageView image_delete;
        Switch switch1;

    }


    private void deleteReminderById(int id) {
        new MaterialDialog.Builder(getContext())
                .title("Delete Reminder?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            Activity activity = (Activity) getContext();

                            boolean isDeleted = reminderRepository.deleteById(id);
                            if (isDeleted) {

                                Snackbar.make(activity.findViewById(android.R.id.content), "Successfully Deleted Reminder!", Snackbar.LENGTH_LONG).show();
                                List<Reminder> reminders = new LinkedList<>();
                                if(reminderRepository.getAll()!=null){
                                    reminders = reminderRepository.getAll();
                                }
                                ReminderFragments.reloadItem(reminders);
                            }
                            if (!isDeleted) {
                                Snackbar.make(activity.findViewById(android.R.id.content), "Failed to Delete Medicine!", Snackbar.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }


                    }
                }).show();


    }

}
