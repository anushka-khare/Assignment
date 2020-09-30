package com.daffodil.assignment.googlemaps.api;

import com.daffodil.assignment.googlemaps.api.model.GoogleApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApiService {

    @GET("maps/api/place/autocomplete/json")
    Call<GoogleApiResponse> getAutoCompletePlaces(@Query("input") String input,
                                                  @Query("radius") String radius,
                                                  @Query("location") String location,
                                                  @Query("key") String key,
                                                  @Query("sessiontoken") String sessiontoken,
                                                  @Query("components") String components);

    @GET("maps/api/place/details/json?")
    Call<GoogleApiResponse> getPlacesDetails(@Query("placeid") String placeId,
                                             @Query("fields") String fields,
                                             @Query("key") String key);

    /**
     * google web api will return us route
     */
    @GET("maps/api/directions/json?")
    Call<GoogleApiResponse> getDirections(@Query("origin") String origin,
                                          @Query("destination") String destination,
                                          @Query("key") String key,
                                          @Query("mode") String mode,
                                          @Query("departure_time") String departure_time);

    /**
     * google web api will return us route with way point
     */
    @GET("maps/api/directions/json")
    Call<GoogleApiResponse> getDirectionsWithWayPoint(@Query("origin") String origin,
                                                      @Query("destination") String destination,
                                                      @Query("waypoints") String wayPoint,
                                                      @Query("key") String key,
                                                      @Query("departure_time") String now);

}