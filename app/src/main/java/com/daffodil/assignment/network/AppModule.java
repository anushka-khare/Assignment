package com.daffodil.assignment.network;

import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.googlemaps.api.GoogleApiService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppModule {
    private static ApiService apiService;
    private static GoogleApiService sGoogleApiService;

    /**
     * Get api retrofit object
     * @return Instance of api service
     */
    public  GoogleApiService getGoogleApiService() {
        if (sGoogleApiService == null) {
            final String baseUrl = "https://maps.googleapis.com/";
            sGoogleApiService = new Retrofit.Builder()
                    .client(provideHttpClient(provideInterceptor()))
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .build()
                    .create(GoogleApiService.class);
        }

        return sGoogleApiService;
    }

    public ApiService provideRetrofit() {
        if (apiService == null) {
            apiService = new Retrofit.Builder()
                    .client(provideHttpClient(provideInterceptor()))
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .build().create(ApiService.class);
        }
        return apiService;
    }


    private OkHttpClient provideHttpClient(ApiInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }


    private Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private ApiInterceptor provideInterceptor() {
        return new ApiInterceptor();
    }
}
