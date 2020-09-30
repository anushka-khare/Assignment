package com.daffodil.assignment.googlemaps.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GoogleMapApiService {

    private static Retrofit sRetrofitGoogle;
    private static GoogleApiService sGoogleApiService;

    /**
     * Initialize retrofit client with base url
     * @return Instance if retrofit
     */
    private static Retrofit getRetrofit() {
        if (sRetrofitGoogle == null) {

            final String baseUrl = "https://maps.googleapis.com/";

            final OkHttpClient client = new OkHttpClient.Builder()
                    .followRedirects(true)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            sRetrofitGoogle = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }

        return sRetrofitGoogle;
    }

    /**
     * Get api retrofit object
     * @return Instance of api service
     */
    public static GoogleApiService getApiService() {
        if (sGoogleApiService == null) {
            sGoogleApiService = getRetrofit().create(GoogleApiService.class);
        }

        return sGoogleApiService;
    }

}
