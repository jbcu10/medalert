package jbcu10.dev.medalert.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import jbcu10.dev.medalert.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragments extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    View rootView;


    public MapFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, null, false);
        getActivity().setTitle("Drug Stores");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(15.4848938, 120.9760381);



        String[][] store = {{"Mercury drugs", "Mabini", "15.486544","120.973581"},
                {"Evamed pharmacy","", "15.4886504","120.9718253"},
                {"TGP","", "15.4887936","120.9722788"},
                {"Angel Drug Store","", "15.4893923","120.9715652"},
                {"Manson Drug Store","", "15.4884246","120.9726107"},
                        {"Gapan Drug Store","","15.4897619", "120.9718253"},
                        {"South Star Drug","","15.488467","120.9716307"}};

        for(int i =0;i<store.length;i++) {
            LatLng point = new LatLng(Double.parseDouble(store[i][2]), Double.parseDouble(store[i][3]));
            MarkerOptions options = new MarkerOptions();
            options.position(point);
            options.title(store[i][0]);
            options.snippet(store[i][1]);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
            mMap.addMarker(options);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

}
