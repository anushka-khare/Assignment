package com.daffodil.assignment.ui.map;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodil.assignment.R;
import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.googlemaps.api.GoogleMapServiceManager;
import com.daffodil.assignment.googlemaps.api.model.GoogleApiResponse;
import com.daffodil.assignment.googlemaps.api.model.PlaceModel;
import com.daffodil.assignment.googlemaps.repo.location.ILocationRepository;
import com.daffodil.assignment.googlemaps.repo.location.LocationRepositiory;
import com.daffodil.assignment.ui.input_user_details.view.AddUserDetailActivity;
import com.daffodil.assignment.utilities.CustomAutoCompleteTextView;
import com.daffodil.assignment.utilities.DeviceUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.daffodil.assignment.common.AppConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnPoiClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraIdleListener {
    private LocationPickerAdapter locationPickerAdapter;
    private ILocationRepository locationRepository;
    private static final String TAG = "MapsActivity";
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 50;
    private static final float DEFAULT_ZOOM = 14;
    private GoogleMap mMap;
    private boolean locationAccessAllowed;
    private LatLng currentLatLng;
    private LocationManager locationManager;
    private Marker pickUpMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private Handler mHandler;
    private RecyclerView placesList;
    private List<Address> addresses;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private CustomAutoCompleteTextView edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationRepository = new LocationRepositiory(this);

        currentLatLng = new LatLng(28.4595, 77.0266);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, R.string.gps_not_enabled, Toast.LENGTH_SHORT).show();
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mHandler = new Handler();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());


        initializeView();


    }

    void initializeView() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        Button nextPageBtn = findViewById(R.id.next_btn);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
        locationPickerAdapter = new LocationPickerAdapter(getApplicationContext());

        edtSearch = findViewById(R.id.search_box);
        edtSearch.setAdapter(locationPickerAdapter);
        edtSearch.setAlwaysFilter(true);
        edtSearch.setFilterOnFocus(true);
        edtSearch.setOnEditorActionListener((v, actionId, event) -> false);
        edtSearch.setOnItemClickListener(onItemClickListener);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setDropDownWidth(DeviceUtils.getDeviceScreenWidth(this) - 5);
        edtSearch.setDropDownAnchor(edtSearch.getId());


    }
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PlaceModel model = locationPickerAdapter.getItem(position);
            assert model != null;

            locationRepository.getPlaceDetails(getBaseContext(), model.getPlace_id(), new GoogleMapServiceManager.IMapServiceListener() {
                @Override
                public void onSuccess(GoogleApiResponse routes) {
                    PlaceModel placeModel = routes.getPlaceModel();
                    if (placeModel != null && placeModel.getGeometry() != null) {
                        model.setGeometry(placeModel.getGeometry());
                        LatLng place = new LatLng(
                                placeModel.getGeometry().getGoogleLocation().getLat(),
                                placeModel.getGeometry().getGoogleLocation().getLng()
                        );
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                    }
                }

                @Override
                public void onFailure(String msg) {

                }
            });
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();

        updateLocationUI();

        getDeviceLocation();

        //For setting lat long bounds for INDIA, but somehow not working properly --TODO --

//        LatLngBounds INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712),
//                new LatLng(28.20453, 97.34466));
//
//        // Set the camera to the greatest possible zoom level that includes the bounds
//        mMap.setLatLngBoundsForCameraTarget(INDIA);

        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                // Add a marker in New Delhi and move the camera
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                String address = getAddress(currentLatLng.latitude, currentLatLng.longitude);
                updatePickUpMarker(address, currentLatLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(MapsActivity.this, R.string.gps_not_enabled, Toast.LENGTH_SHORT).show();
                }
            }
        });


        mMap.setOnCameraIdleListener(this);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationAccessAllowed = true;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
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
        locationAccessAllowed = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationAccessAllowed = true;
                updateLocationUI();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null) {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            String address = getAddress(currentLatLng.latitude, currentLatLng.longitude);
            updatePickUpMarker(address, currentLatLng);
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
            updatePickUpMarker(pointOfInterest.name, currentLatLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMap != null) {
            currentLatLng = latLng;
            String address = getAddress(currentLatLng.latitude, currentLatLng.longitude);
            updatePickUpMarker(address, currentLatLng);
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

    private void getDeviceLocation() {
        try {
            if (locationAccessAllowed) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                String address = getAddress(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                                updatePickUpMarker(address, currentLatLng);

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationAccessAllowed) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setOnPoiClickListener(this);
                mMap.setOnMapClickListener(this);

                //For getting updated locations if device
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updatePickUpMarker(String address, LatLng currentLatLng) {

        if (pickUpMarker != null) {
            pickUpMarker.setPosition(currentLatLng);
            pickUpMarker.setSnippet(address);
        } else {
            pickUpMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                    .title(getString(R.string.me))
                    .snippet(address)
                    .draggable(true));
        }
    }

    @Override
    public void onCameraIdle() {
        locationPickerAdapter.setCameraMove(true);
        LatLng data = mMap.getCameraPosition().target;
        locationPickerAdapter.setCurrentLocation(data);
    }
}