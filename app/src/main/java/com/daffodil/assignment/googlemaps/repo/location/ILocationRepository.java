package com.daffodil.assignment.googlemaps.repo.location;

import android.content.Context;

import com.daffodil.assignment.googlemaps.api.GoogleMapServiceManager;
import com.daffodil.assignment.googlemaps.api.model.PlaceModel;

import java.util.HashMap;

public interface ILocationRepository {

    void saveRecentPlacesList(HashMap<String, PlaceModel> placeModelList);

    void saveFavoritePlacesList(HashMap<String, PlaceModel> placeModelList);


    HashMap<String, PlaceModel> readRecentPlacesList();

    HashMap<String, PlaceModel> readFavoritePlacesList();

    void autoCompletePlacesApiCall(Context context, String input, String location, GoogleMapServiceManager.IMapServiceListener listener);

    void getPlaceDetails(Context context, String placeId, GoogleMapServiceManager.IMapServiceListener listener);
}
