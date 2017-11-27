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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.PatientActivity;
import jbcu10.dev.medalert.activity.RelativeActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.Patient;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private static final String TAG = "Patient adapter";

    static class ViewHolder {
        TextView txt_name,txt_gender,txt_contact_number,txt_email;
        ImageView image_gender;

    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public PatientAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
         final Patient patient = getItem(position);

        viewHolder.image_gender.setImageDrawable(this.getImageGender(patient)!=null? this.getImageGender(patient): getContext().getResources().getDrawable(R.drawable.male));
        viewHolder.txt_name.setText(patient.getFirstName()+" "+patient.getMiddleName()+" "+patient.getLastName());
        viewHolder.txt_contact_number.setText(patient.getContactNumber());
        viewHolder.txt_email.setText(patient.getEmail());
        viewHolder.txt_gender.setText(patient.getGender());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    AppController appController = AppController.getInstance();
                    appController.setPatientId(patient.getId());
                    Intent intent = new Intent(getContext(), PatientActivity.class);
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

    private Drawable getImageGender(Patient patient){
        Log.d("Gender",patient.getGender());
            if(patient.getGender()!=null && patient.getGender().toLowerCase().equals("female")){
                return  getContext().getResources().getDrawable(R.drawable.female);
            }
            return  getContext().getResources().getDrawable(R.drawable.male);
         }




}