package com.example.try1.ui.map;

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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.try1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private PlacesClient placesClient;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        Places.initialize(getActivity().getApplicationContext(),"AIzaSyDMgJuxO_J7Kea505ASzq_EhVBoHdGt1Fw");
        placesClient = Places.createClient(getActivity());
        getCurrentLocation();

    }

    //method get current location
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void getCurrentLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                LatLng currentLocation = new LatLng(latitude, longitude);
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Saya Disini"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                locationManager.removeUpdates(this);
                findNearestRestaurants(latitude, longitude, 5);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 1000, 10, locationListener);
    }


    //method find restaurant

    private void findNearestRestaurants(double latitude, double longitude, int numRestaurants) {
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(latitude - 0.1, longitude - 0.1),
                new LatLng(latitude + 0.1, longitude + 0.1)
        );

        //you can change this array list query to your favorite restaurant or whatever you want to put here. This query is written based on where this application is built
        List<String> queryList = Arrays.asList("richeese ahmad yani", "wingz burangrang", "sate gino", "nasi tim ayam gizi atjep bandung", "sate ayam madura enjoy bang kumis"); //queries as array

        FindAutocompletePredictionsRequest.Builder requestBuilder = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(RectangularBounds.newInstance(bounds));

        for (String query : queryList) {
            requestBuilder.setQuery(query); //call query

            FindAutocompletePredictionsRequest request = requestBuilder.build();

            Task<FindAutocompletePredictionsResponse> predictionsTask = placesClient.findAutocompletePredictions(request);
            predictionsTask.addOnSuccessListener((response) -> {
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                for (AutocompletePrediction prediction : predictions) {
                    String placeId = prediction.getPlaceId();
                    fetchPlaceDetails(placeId);
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    //to be handled
                }
            });
        }
    }

//get places name
    private void fetchPlaceDetails(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        Task<FetchPlaceResponse> placeResponse = placesClient.fetchPlace(request);
        placeResponse.addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                FetchPlaceResponse response = task.getResult();
                if (response != null) {
                    Place place = response.getPlace();
                    LatLng location = place.getLatLng();

                    if (location != null) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(place.getName())
                        );
                    }
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    //to be handled
                }
            }
        });
    }
}

