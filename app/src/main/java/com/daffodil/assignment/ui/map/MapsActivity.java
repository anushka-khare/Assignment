package com.daffodil.assignment.ui.map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daffodil.assignment.R;
import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.ui.input_user_details.view.AddUserDetailActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.daffodil.assignment.common.AppConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnPoiClickListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "MapsActivity";
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 50;
    private GoogleMap mMap;
    private boolean locationPermissionGranted;
    private LatLng currentLatLng;
    private LocationManager locationManager;
    private Marker pickUpMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private SearchView search_place;
    private static int maxResults = 20;
    private Handler mHandler;
    private RecyclerView placesList;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeView();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mHandler = new Handler();

        geocoder = new Geocoder(this, Locale.getDefault());

        Button nextPageBtn = findViewById(R.id.next_btn);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        // Initialize Places.
//        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));


    }

    void initializeView() {

        search_place = findViewById(R.id.search_place);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        search_place.requestFocus();
        search_place.setSubmitButtonEnabled(false);
        search_place.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addresses = geocoder.getFromLocationName(s, maxResults);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, 1000);
                return false;
            }
        });
        placesList = findViewById(R.id.places_list);
        placesList.setLayoutManager(new LinearLayoutManager(this));



    }

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
        getLocationPermission();

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).


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

        // Create a LatLngBounds that includes Australia.
//        LatLngBounds INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712),
//                new LatLng(28.20453, 97.34466));
//
//        // Set the camera to the greatest possible zoom level that includes the bounds
//        mMap.setLatLngBoundsForCameraTarget(INDIA);
//        mMap.setMaxZoomPreference(14);

       /* pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                .title(getString(R.string.me))
                .draggable(true));*/
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                // Add a marker in New Delhi and move the camera
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                String address = getAddress(currentLatLng.latitude, currentLatLng.longitude);
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                        .title(getString(R.string.me))
                        .snippet(address)
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            mMap.setOnPoiClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                String address = getAddress(location.getLatitude(), location.getLongitude());
                                if (pickUpMarker == null) {
                                    pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                                            .title(getString(R.string.me))
                                            .snippet(address)
                                            .draggable(true));
                                } else {
                                    pickUpMarker.setPosition(currentLatLng);
                                    pickUpMarker.setSnippet(address);
                                }
                            }
                        }
                    });
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
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null) {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            String address = getAddress(currentLatLng.latitude, currentLatLng.longitude);
            if (pickUpMarker != null) {
                pickUpMarker.setPosition(currentLatLng);
                pickUpMarker.setSnippet(address);
            } else {
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                        .title(getString(R.string.me))
                        .snippet(address)
                        .draggable(true));
            }

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


    void nextPage() {
        if (pickUpMarker != null) {
            Intent intent = new Intent(this, AddUserDetailActivity.class);
            intent.putExtra(AppConstants.LAT_LNG, pickUpMarker.getPosition().toString());
            intent.putExtra(AppConstants.PLACE, pickUpMarker.getSnippet());
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.choose_a_pick_up_location), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        if (mMap != null) {
            currentLatLng = pointOfInterest.latLng;
            if (pickUpMarker != null) {
                pickUpMarker.setPosition(currentLatLng);
                pickUpMarker.setSnippet(pointOfInterest.name);
            } else {
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                        .title(pointOfInterest.name)
                        .snippet(getString(R.string.me))
                        .draggable(true));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMap != null) {
            currentLatLng = latLng;
            String address = getAddress(currentLatLng.latitude, currentLatLng.longitude);
            if (pickUpMarker != null) {
                pickUpMarker.setPosition(currentLatLng);
                pickUpMarker.setSnippet(address);
            } else {
                pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                        .title(address)
                        .snippet(getString(R.string.me))
                        .draggable(true));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        }
    }

    public String getAddress(double lat, double lng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}