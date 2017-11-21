package jbcu10.dev.medalert.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.FirstAidActivity;
import jbcu10.dev.medalert.activity.ReminderActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Reminder;

/**
 * Created by dev on 10/14/17.
 */

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private static final String TAG = "Reminder Adapter";

    static class ViewHolder {
        TextView  txt_name,txt_description ;

    }
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;
    public ReminderAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
    }
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();


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

        viewHolder.txt_name.setText("Reminder: "+reminder.getId());
        viewHolder.txt_description.setText(reminder.getDescription());

      convertView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               try {
                                                  Log.d("id","first aid "+reminder.getId());

                                                   AppController appController = AppController.getInstance();
                                                   appController.setReminderId(reminder.getId());
                                                   Intent intent = new Intent(getContext(), ReminderActivity.class);
                                                   getContext().startActivity(intent);

                                                   Activity activity = (Activity) getContext();
                                                   activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                               }
                                               catch (Exception e){
                                                   Log.d("Error",e.getMessage());
                                               }
                                           }
                                       }
        );


        return convertView;
    }


    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

}
