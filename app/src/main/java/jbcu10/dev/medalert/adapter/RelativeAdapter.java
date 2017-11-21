package jbcu10.dev.medalert.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.MedicineActivity;
import jbcu10.dev.medalert.activity.RelativeActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.Medicine;
import jbcu10.dev.medalert.model.Relative;

public class RelativeAdapter extends ArrayAdapter<Relative> {

    private static final String TAG = "Relative adapter";

    static class ViewHolder {
        TextView txt_name,txt_relation,txt_contact_number,txt_email;
        ImageView image_relation;

    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public RelativeAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_relative, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
            viewHolder.txt_relation = convertView.findViewById(R.id.txt_relation);
            viewHolder.txt_contact_number = convertView.findViewById(R.id.txt_contact_number);
            viewHolder.txt_email = convertView.findViewById(R.id.txt_email);
           viewHolder.image_relation = convertView.findViewById(R.id.image_relation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
         final Relative relative = getItem(position);
        viewHolder.image_relation.setImageDrawable(this.getImageRelation(relative)!=null? this.getImageRelation(relative): getContext().getResources().getDrawable(R.drawable.relative));


        viewHolder.txt_name.setText(relative.getFirstName()+" "+relative.getMiddleName()+" "+relative.getLastName());
        viewHolder.txt_contact_number.setText(relative.getContactNumber());
        viewHolder.txt_email.setText(relative.getEmail());
        viewHolder.txt_relation.setText(relative.getRelationship());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    AppController appController = AppController.getInstance();
                    appController.setRelativeId(relative.getId());
                    Intent intent = new Intent(getContext(), RelativeActivity.class);
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

    private Drawable getImageRelation(Relative relative){
        try {
            if(relative.getRelationship()!=null&&relative.getRelationship().toLowerCase().equals("mother")){
                return  getContext().getResources().getDrawable(R.drawable.mother);
            }if(relative.getRelationship()!=null&&relative.getRelationship().toLowerCase().equals("father")){
                return  getContext().getResources().getDrawable(R.drawable.father);
            }if(relative.getRelationship()!=null&&relative.getRelationship().toLowerCase().equals("grand father")){
                return  getContext().getResources().getDrawable(R.drawable.grandfather);
            }if(relative.getRelationship()!=null&&relative.getRelationship().toLowerCase().equals("grand mother")){
                return  getContext().getResources().getDrawable(R.drawable.grandmother);
            }
            if(relative.getRelationship()!=null&&relative.getRelationship().toLowerCase().equals("sister")){
                return  getContext().getResources().getDrawable(R.drawable.daughter);
            }
            if(relative.getRelationship()!=null&&relative.getRelationship().toLowerCase().equals("brother")){
                return  getContext().getResources().getDrawable(R.drawable.boy);
            }
            return  getContext().getResources().getDrawable(R.drawable.relative);

        }
        catch (Exception e){
            Log.d("Error",e.getMessage());
            return null;
        }
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