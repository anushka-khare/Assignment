package com.daffodil.assignment.googlemaps.repo.location;

import android.content.Context;

import com.daffodil.assignment.R;
import com.daffodil.assignment.googlemaps.api.GoogleMapServiceManager;
import com.daffodil.assignment.googlemaps.api.IGoogleApiRequestManager;
import com.daffodil.assignment.googlemaps.api.model.GoogleApiRequest;
import com.daffodil.assignment.googlemaps.api.model.PlaceModel;
import com.daffodil.assignment.utilities.CustomSharedPreferences;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class LocationRepositiory implements ILocationRepository {

    public static class ProfilePreference {
        public static final String PROFILE_PREFERENCES = "profile";
        public static final String RECENT_SEARCHES = "recent_search";
        public static final String FAVORITE_SEARCHES = "favorite_search";
    }

    CustomSharedPreferences profilePreferences;

    public LocationRepositiory(Context context) {
        profilePreferences = new CustomSharedPreferences(context, ProfilePreference.PROFILE_PREFERENCES);
    }

    @Override
    public void saveRecentPlacesList(HashMap<String, PlaceModel> placeModelList) {
        ObjectMapper objectMapper = new ObjectMapper();
        String placeList = null;
        try {
            placeList = objectMapper.writeValueAsString(placeModelList);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        profilePreferences.edit().putString(ProfilePreference.RECENT_SEARCHES, placeList).apply();
    }

    @Override
    public void saveFavoritePlacesList(HashMap<String, PlaceModel> placeModelList) {
        ObjectMapper objectMapper = new ObjectMapper();
        String placeList = null;
        try {
            placeList = objectMapper.writeValueAsString(placeModelList);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        profilePreferences.edit().putString(ProfilePreference.FAVORITE_SEARCHES, placeList).apply();
    }


    @Override
    public HashMap<String, PlaceModel> readRecentPlacesList() {
        String userSessionJsonData = profilePreferences.getString(ProfilePreference.RECENT_SEARCHES, "");
        if (userSessionJsonData.isEmpty()) {
            return new HashMap<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(userSessionJsonData, new TypeReference<HashMap<String, PlaceModel>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public HashMap<String, PlaceModel> readFavoritePlacesList() {
        String userSessionJsonData = profilePreferences.getString(ProfilePreference.FAVORITE_SEARCHES, "");
        if (userSessionJsonData.isEmpty()) {
            return new HashMap<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(userSessionJsonData, new TypeReference<HashMap<String, PlaceModel>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }

    }

    @Override
    public void autoCompletePlacesApiCall(Context context, String input, String location, GoogleMapServiceManager.IMapServiceListener listener) {

        IGoogleApiRequestManager requestManager = new GoogleMapServiceManager();
        GoogleApiRequest apiRequest = new GoogleApiRequest();
        apiRequest.setInput(input);
        apiRequest.setLocation(location);
        apiRequest.setKey(context.getString( R.string.google_maps_key));
        apiRequest.setSessiontoken("");
        requestManager.getAutoCompletePlaces(apiRequest, listener);
    }

    @Override
    public void getPlaceDetails(Context context, String placeId, GoogleMapServiceManager.IMapServiceListener listener) {
        IGoogleApiRequestManager requestManager = new GoogleMapServiceManager();
        GoogleApiRequest apiRequest = new GoogleApiRequest();
        apiRequest.setPlaceId(placeId);
        apiRequest.setFields("geometry");
        apiRequest.setKey(context.getString(R.string.google_maps_key));
        requestManager.getPlacesDetails(apiRequest, listener);
    }

}
