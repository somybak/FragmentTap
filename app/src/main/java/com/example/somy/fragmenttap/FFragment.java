package com.example.somy.fragmenttap;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    MainActivity activity;
    LocationManager manager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=  (MainActivity) context;
        manager = activity.getLocationManager();


    }

    public FFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                10,
                locationListener);

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                3000,
                10,
                locationListener);
          }


    @Override // version check. provider might be different. GPS-10m or Network-200m(location information in protocol)
    public void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        manager.removeUpdates(locationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_f, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Dae-ki"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20));
    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            float accuracy = location.getAccuracy();

            String provider = location.getProvider();

            // my location
            LatLng myPosition = new LatLng(latitude, longitude);
            // Add a marker in my location and move the camera
            mMap.addMarker(new MarkerOptions().position(myPosition).title("I'm here"));
                                                                        // Marker title
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 50));

            // Zoom. move camera

        }

        @Override //
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
