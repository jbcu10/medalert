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
import jbcu10.dev.medalert.activity.FirstAidActivity;
import jbcu10.dev.medalert.activity.MapsActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Store;

/**
 * Created by dev on 10/14/17.
 */

public class PharmacyAdapter extends ArrayAdapter<Store> {

    private final LayoutInflater mLayoutInflater;

    public PharmacyAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        PharmacyAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_pharmacy, parent, false);
            viewHolder = new PharmacyAdapter.ViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
            viewHolder.txt_address = convertView.findViewById(R.id.txt_address);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PharmacyAdapter.ViewHolder) convertView.getTag();
        }
        final Store store = getItem(position);

        viewHolder.txt_name.setText(store.getName());
        viewHolder.txt_address.setText(store.getAddress());

        convertView.setOnClickListener(view -> {
                    try {
                        Log.d("id", "first aid " + store.getId());

                        AppController appController = AppController.getInstance();
                        appController.setStoreId(store.getId());
                        Intent intent = new Intent(getContext(), MapsActivity.class);
                        getContext().startActivity(intent);

                        Activity activity = (Activity) getContext();
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
        );


        return convertView;
    }

    static class ViewHolder {
        TextView txt_name, txt_address;

    }

}
