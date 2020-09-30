package com.daffodil.assignment.googlemaps.api;


import com.daffodil.assignment.googlemaps.api.model.GoogleApiRequest;

public interface IGoogleApiRequestManager {

    void getAutoCompletePlaces(GoogleApiRequest googleApiRequest, GoogleMapServiceManager.IMapServiceListener listener);

    void getPlacesDetails(GoogleApiRequest googleApiRequest, GoogleMapServiceManager.IMapServiceListener listener);
}
