package jbcu10.dev.medalert.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.StoreRepository;
import jbcu10.dev.medalert.model.Store;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private StoreRepository storeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        storeRepository = new StoreRepository(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        AppController appController = AppController.getInstance();
        Store store = storeRepository.getById(appController.getStoreId());
        mMap = googleMap;
        LatLng point = new LatLng(store.getLat(), store.getLon());
        MarkerOptions options = new MarkerOptions();
        options.position(point);
        options.title(store.getName());
        options.snippet(store.getAddress());
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
    }
}
