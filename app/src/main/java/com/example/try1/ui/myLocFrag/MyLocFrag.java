package com.example.try1.ui.myLocFrag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.try1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

//10120069 Rendy Agustin IF2//

public class MyLocFrag extends Fragment implements OnMapReadyCallback {

    private GoogleMap gmap;
    private PlacesClient placesClient;
    private LocationManager locManager;
    private LocationListener locListen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_loc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_loc);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        gmap = map;
        Places.initialize(getActivity().getApplicationContext(),"AIzaSyDMgJuxO_J7Kea505ASzq_EhVBoHdGt1Fw");
        placesClient = Places.createClient(getActivity());
        getMyLocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void getMyLocation() {
        locManager = (LocationManager)  requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locListen = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng currentLocation = new LatLng(latitude, longitude);
                //clear marker
                gmap.clear();

                //add marker
                gmap.addMarker(new MarkerOptions().position(currentLocation).title("Lokasi Saya"));

                //move focus camera and zoom
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f));

                // Stop listening for location updates
                locManager.removeUpdates(this);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 1000, 10, locListen);
    }
}