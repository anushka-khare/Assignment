package com.daffodil.assignment.googlemaps.api;


import com.daffodil.assignment.googlemaps.api.model.GoogleApiResponse;

public interface GoogleApiResponseListener {
    void onSuccessAutoCompletePlaces(GoogleApiResponse apiResponse);

    void onSuccessPlaceDetails(GoogleApiResponse apiResponse);

    void onSuccessDirectionsResult(GoogleApiResponse apiResponse);

    void onError(String error);


}
