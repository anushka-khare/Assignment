package com.daffodil.assignment.network;

import androidx.annotation.NonNull;

import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.preferences.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class ApiInterceptor implements Interceptor {

    /**
     * Interceptor that modify/add header for outgoing request
     *
     * @param chain Request chain
     * @return Modified header request
     * @throws IOException Throws IOException
     */
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Request originalRequest
                = chain.request();
        final Request requestWithNonAuthHeaders = modifyNonAuthHeaders(originalRequest);

        final Request requestWithAuthAndNonAuthHeaders = modifyAuthHeaders(requestWithNonAuthHeaders);

        return chain.proceed(requestWithAuthAndNonAuthHeaders);
    }

    /**
     * Modify header which want to authorization
     *
     * @param request Request
     * @return Return builder
     */
    private Request modifyAuthHeaders(Request request) {
        if (request != null) {
            Request.Builder builder = request.newBuilder();
//            String authToken = SessionManager.getNewInstance().readAuthToken();

            String authToken = AppConstants.authToken;
            if (authToken != null)
                builder.header(AppConstants.Authorization, authToken);
            return builder.build();
        }
        return null;
    }

    /**
     * Modify public header
     *
     * @param request Request
     * @return Return builder
     */
    private Request modifyNonAuthHeaders(Request request) {
        if (request != null) {
            Request.Builder builder = request.newBuilder();
//            builder.header("Content-Type", "application/json");
//            builder.header("api_key", BuildConfig.API_KEY);
            return builder.build();
        }
        return null;
    }
}
