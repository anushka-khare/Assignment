package com.daffodil.assignment.googlemaps.api;

import android.util.Log;

import com.daffodil.assignment.App;
import com.daffodil.assignment.googlemaps.api.model.GoogleApiRequest;
import com.daffodil.assignment.googlemaps.api.model.GoogleApiResponse;
import com.daffodil.assignment.network.AppModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleMapServiceManager implements IGoogleApiRequestManager{

    private static final String TAG = GoogleMapServiceManager.class.getName();
    private Call<GoogleApiResponse> mRouteCall;

    @Override
    public void getAutoCompletePlaces(GoogleApiRequest googleApiRequest, IMapServiceListener listener) {
        mRouteCall = App.getAppModule().getGoogleApiService().getAutoCompletePlaces(
                googleApiRequest.getInput(),
                "5000",
                googleApiRequest.getLocation(),
                googleApiRequest.getKey(),
                googleApiRequest.getSessiontoken(),
                "country:in");
        mRouteCall.enqueue(new Callback<GoogleApiResponse>() {
            @Override
            public void onResponse(Call<GoogleApiResponse> call, Response<GoogleApiResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                    assert response.body() != null;
                }
            }

            @Override
            public void onFailure(Call<GoogleApiResponse> call, Throwable t) {
                Log.d("onFailure : ", "onFailure");
            }
        });
    }

    @Override
    public void getPlacesDetails(GoogleApiRequest googleApiRequest, IMapServiceListener listener) {
        mRouteCall = App.getAppModule().getGoogleApiService().getPlacesDetails(
                googleApiRequest.getPlaceId(),
                googleApiRequest.getFields(),
                googleApiRequest.getKey());
        mRouteCall.enqueue(new Callback<GoogleApiResponse>() {
            @Override
            public void onResponse(Call<GoogleApiResponse> call, Response<GoogleApiResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                    assert response.body() != null;
                }
            }

            @Override
            public void onFailure(Call<GoogleApiResponse> call, Throwable t) {
                Log.d("onFailure : ", "onFailure");
            }
        });
    }

    public void cancel(){
        if (mRouteCall != null && mRouteCall.isExecuted()){
            mRouteCall.cancel();
        }
        if (mRouteCall != null && mRouteCall.isExecuted()){
            mRouteCall.cancel();
        }
    }

    public interface IMapServiceListener {
        void onSuccess(GoogleApiResponse routes);

        void onFailure(String msg);
    }
}
