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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.MainActivity;
import jbcu10.dev.medalert.activity.MedicineActivity;
import jbcu10.dev.medalert.activity.NewMedicineActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.Medicine;

public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private static final String TAG = "Medicine Adapter";

    static class ViewHolder {
        TextView txt_name,txt_genric_name,txt_diagnosis,txt_description,txt_expiration,txt_doctor_name, txt_total;
        Button btnGo;
        ImageView image_type;

    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public MedicineAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_medicine, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
            viewHolder.txt_description = convertView.findViewById(R.id.txt_description);
            viewHolder.txt_doctor_name = convertView.findViewById(R.id.txt_doctor_name);
            viewHolder.txt_genric_name = convertView.findViewById(R.id.txt_generic_name);
            viewHolder.txt_diagnosis = convertView.findViewById(R.id.txt_diagnosis);
            viewHolder.txt_expiration = convertView.findViewById(R.id.txt_expiration);
            viewHolder.txt_total = convertView.findViewById(R.id.txt_total);
            viewHolder.image_type = convertView.findViewById(R.id.image_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Medicine medicine = getItem(position);
        if(medicine.getType()!=null&&medicine.getType().equals("Tablet")){
            viewHolder.image_type.setImageDrawable(getContext().getResources().getDrawable(R.drawable.pill));
        }
        viewHolder.txt_name.setText(medicine.getName());
        viewHolder.txt_diagnosis.setText(medicine.getDiagnosis());
        viewHolder.txt_genric_name.setText(medicine.getGenericName());
        viewHolder.txt_description.setText(medicine.getDescription());
        viewHolder.txt_total.setText(String.valueOf(medicine.getTotal()));

        Date date = new Date(medicine.getExpiration());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        viewHolder.txt_expiration.setText(df.format(date));
        viewHolder.txt_doctor_name.setText(medicine.getDoctor()!=null ?medicine.getDoctor().getFirstName()+" "+medicine.getDoctor().getLastName():"Not Available");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("id","medicine "+medicine.getId());

                    AppController appController = AppController.getInstance();
                    appController.setMedicineId(medicine.getId());
                    Intent intent = new Intent(getContext(), MedicineActivity.class);
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