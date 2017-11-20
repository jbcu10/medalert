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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.FirstAidActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.FirstAid;

/**
 * Created by dev on 10/14/17.
 */

public class FirstAidAdapter extends ArrayAdapter<FirstAid> {
    private static final String TAG = "First Aid Adapter";

    static class ViewHolder {
        TextView txt_name ,txt_description ;

    }
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;
    public FirstAidAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
    }
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        FirstAidAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_firstaid, parent, false);
            viewHolder = new FirstAidAdapter.ViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
            viewHolder.txt_description = convertView.findViewById(R.id.txt_description);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FirstAidAdapter.ViewHolder) convertView.getTag();
        }
        final FirstAid firstAid = getItem(position);

        viewHolder.txt_name.setText(firstAid.getName());
        viewHolder.txt_description.setText(firstAid.getDescription());

      convertView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               try {
                                                  Log.d("id","first aid "+firstAid.getId());

                                                   AppController appController = AppController.getInstance();
                                                   appController.setFirstAidId(firstAid.getId());
                                                   Intent intent = new Intent(getContext(), FirstAidActivity.class);
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
