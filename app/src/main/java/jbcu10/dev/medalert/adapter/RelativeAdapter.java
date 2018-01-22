package jbcu10.dev.medalert.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.PatientActivity;
import jbcu10.dev.medalert.activity.RelativeActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.db.ReminderRepository;
import jbcu10.dev.medalert.model.Relative;

public class RelativeAdapter extends ArrayAdapter<Relative> {

    RelativeRepository relativeRepository;
    private final LayoutInflater mLayoutInflater;

    public RelativeAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
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
            viewHolder.ch_enabled = convertView.findViewById(R.id.ch_enabled);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Relative relative = getItem(position);


        try {

            Log.d("set",relative.getImageUri());
            if(!relative.getImageUri().equals("")){
                // Uri uri = Uri.parse(patient.getImageUri());
                Glide.with(getContext()).load(relative.getImageUri()).into(viewHolder.image_relation);

            }
            if(relative.getImageUri().equals("")) {
                viewHolder.image_relation.setImageDrawable(this.getImageRelation(relative) != null ? this.getImageRelation(relative) : getContext().getResources().getDrawable(R.drawable.relative));
            }
        }
        catch (Exception e){
            viewHolder.image_relation.setImageDrawable(this.getImageRelation(relative) != null ? this.getImageRelation(relative) : getContext().getResources().getDrawable(R.drawable.relative));

        }



        viewHolder.txt_name.setText(relative.getFirstName() + " " + relative.getMiddleName() + " " + relative.getLastName());
        viewHolder.txt_contact_number.setText(relative.getContactNumber());
        viewHolder.txt_email.setText(relative.getEmail());
        viewHolder.txt_relation.setText(relative.getRelationship());
        viewHolder.ch_enabled.setChecked(relative.isNotify());
        Activity activity = (Activity) getContext();

        relativeRepository = new RelativeRepository(activity);

        viewHolder.ch_enabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                relative.setNotify(true);
            } if (!isChecked){
                relative.setNotify(false);
            }
            relativeRepository.update(relative);
             });
        convertView.setOnClickListener(view -> {
                    try {
                        PatientActivity.destination =1;
                        AppController appController = AppController.getInstance();
                        appController.setRelativeId(relative.getId());
                        Intent intent = new Intent(getContext(), RelativeActivity.class);
                        getContext().startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
        );

        return convertView;
    }

    private Drawable getImageRelation(Relative relative) {
        try {
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("mother")) {
                return getContext().getResources().getDrawable(R.drawable.mother);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("father")) {
                return getContext().getResources().getDrawable(R.drawable.father);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("grand father")) {
                return getContext().getResources().getDrawable(R.drawable.grandfather);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("grand mother")) {
                return getContext().getResources().getDrawable(R.drawable.grandmother);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("sister")) {
                return getContext().getResources().getDrawable(R.drawable.daughter);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("brother")) {
                return getContext().getResources().getDrawable(R.drawable.boy);
            }
            return getContext().getResources().getDrawable(R.drawable.relative);

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }

    static class ViewHolder {
        TextView txt_name, txt_relation, txt_contact_number, txt_email;
        ImageView image_relation;
        CheckBox ch_enabled;

    }


}