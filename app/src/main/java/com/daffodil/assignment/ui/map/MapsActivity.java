package com.daffodil.assignment.ui.map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daffodil.assignment.R;
import com.daffodil.assignment.ui.input_user_details.view.AddUserDetailActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.daffodil.assignment.common.AppConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnPoiClickListener {

    private static final String TAG = "MapsActivity";
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 50;
    private GoogleMap mMap;
    private boolean locationPermissionGranted;
    private LatLng currentLatLng;
    private LocationManager locationManager;
    private Marker pickUpMarker;
    private Button nextPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        nextPageBtn = findViewById(R.id.next_btn);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        // Initialize Places.
//        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create a new Places client instance.
        /*PlacesClient placesClient = Places.createClient(this);

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();*/

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

           /* placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                    *//*textView.append(String.format("Place '%s' has likelihood: %f\n",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));*//*
                    currentLatLng = placeLikelihood.getPlace().getLatLng();
                    Toast.makeText(this, placeLikelihood.getPlace().getName() + " Found", Toast.LENGTH_SHORT).show();
                }
            })).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                }
            });*/
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission();
        }
        mMap.setOnPoiClickListener(this);
        mMap.setMyLocationEnabled(true);
        // Create a LatLngBounds that includes Australia.
        LatLngBounds INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712),
                new LatLng(28.20453, 97.34466));

        // Set the camera to the greatest possible zoom level that includes the bounds
        mMap.setLatLngBoundsForCameraTarget(INDIA);
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                // Add a marker in New Delhi and move the camera
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                        .title(getString(R.string.me))
                        .snippet(currentLatLng.latitude + " : " + currentLatLng.longitude)
                        .draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            }
        });


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null) {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(pickUpMarker != null){
                pickUpMarker.remove();
            }
            pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                    .title(getString(R.string.me))
                    .snippet(currentLatLng.latitude + " : " + currentLatLng.longitude)
                    .draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        }
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


    void nextPage(){
        Intent intent = new Intent(this, AddUserDetailActivity.class);
        startActivity(intent);

    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        if(mMap != null) {
            currentLatLng = pointOfInterest.latLng;
            if (pickUpMarker != null) {
                pickUpMarker.remove();
            }
            pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                    .title(getString(R.string.me))
                    .snippet(pointOfInterest.name)
                    .draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        }
    }
}