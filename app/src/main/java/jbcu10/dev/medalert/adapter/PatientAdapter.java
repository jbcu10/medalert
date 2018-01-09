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

import com.squareup.picasso.Picasso;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.PatientActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.model.Patient;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private final LayoutInflater mLayoutInflater;
    PatientRepository patientRepository;

    public PatientAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_patient, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
            viewHolder.txt_gender = convertView.findViewById(R.id.txt_gender);
            viewHolder.txt_contact_number = convertView.findViewById(R.id.txt_contact_number);
            viewHolder.txt_email = convertView.findViewById(R.id.txt_email);
            viewHolder.image_gender = convertView.findViewById(R.id.image_gender);
            viewHolder.ch_enabled = convertView.findViewById(R.id.ch_enabled);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Patient patient = getItem(position);


        try {

            Log.d("set",patient.getImageUri());
            if(!patient.getImageUri().equals("")){
                // Uri uri = Uri.parse(patient.getImageUri());
                Picasso.with(getContext()).invalidate(patient.getImageUri());
                Picasso.with(getContext()).load(patient.getImageUri()).into( viewHolder.image_gender);

            }
            viewHolder.image_gender.setImageDrawable(this.getImageGender(patient) != null ? this.getImageGender(patient) : getContext().getResources().getDrawable(R.drawable.male));

        }
        catch (Exception e){
            viewHolder.image_gender.setImageDrawable(this.getImageGender(patient) != null ? this.getImageGender(patient) : getContext().getResources().getDrawable(R.drawable.male));

        }

        viewHolder.txt_name.setText(patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName());
        viewHolder.txt_contact_number.setText(patient.getContactNumber());
        viewHolder.txt_email.setText(patient.getEmail());
        viewHolder.txt_gender.setText(patient.getGender());
        viewHolder.ch_enabled.setChecked(patient.isEnabled());
        Activity activity = (Activity) getContext();
        convertView.setOnClickListener(view -> {
                    try {

                        AppController appController = AppController.getInstance();
                        appController.setPatientId(patient.getId());
                        Intent intent = new Intent(getContext(), PatientActivity.class);
                        getContext().startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
        );
        patientRepository = new PatientRepository(activity);
        viewHolder.ch_enabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                patient.setEnabled(true);
            } if (!isChecked){
                patient.setEnabled(false);
            }
            patientRepository.update(patient);
        });

        return convertView;
    }

    private Drawable getImageGender(Patient patient) {
        Log.d("Gender", patient.getGender());
        if (patient.getGender() != null && patient.getGender().toLowerCase().equals("female")) {
            return getContext().getResources().getDrawable(R.drawable.female);
        }
        return getContext().getResources().getDrawable(R.drawable.male);
    }

    static class ViewHolder {
        TextView txt_name, txt_gender, txt_contact_number, txt_email;
        ImageView image_gender;
        CheckBox ch_enabled;

    }


}