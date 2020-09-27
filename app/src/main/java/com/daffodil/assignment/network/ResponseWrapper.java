package com.daffodil.assignment.network;

import android.util.Log;

import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.preferences.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseWrapper<T> implements Callback<T> {
    private final RetrofitCallback<T> mResponseCallback;

    public ResponseWrapper(RetrofitCallback<T> mResponseCallback) {
        this.mResponseCallback = mResponseCallback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {
        if (response.isSuccessful()) {
            String token = response.headers().get(AppConstants.Authorization);
            if (token != null) {
                saveTokenToSession(/*"Bearer "+*/token);
            }
            if (response.body() != null) {
                mResponseCallback.onSuccess(response.body());
            }
        } else {
            try {
                if (response.errorBody() != null
                    /*&& response.errorBody().string()!=null && response.errorBody().string().startsWith("{")*/)
                    mResponseCallback.onFailure(handleError(response.errorBody().string()));
                else {
                    ErrorResponse error = new ErrorResponse();
                    error.setCode("NETWORK_ERROR");
                    error.setDetail("");
                    mResponseCallback.onFailure(error);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ErrorResponse error = new ErrorResponse();
        if (t instanceof ConnectException || t instanceof UnknownHostException) {
                error.setDetail("Connection Lost");
        }
        else if(t instanceof IOException){
                error.setDetail("Request cancelled");
        }
        else {
            error.setDetail("Looks like the server is taking too long to respond");
        }
        mResponseCallback.onFailure(error);
    }

    /**
     * Parse isError
     *
     * @param err Error string
     * @return
     */
    ErrorResponse handleError(String err) {
        ErrorResponse response = new ErrorResponse();
        Gson gson = new Gson();

        try {
            response = gson.fromJson(err, ErrorResponse.class);

        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.w("error ", exception.toString());
        }

        return response;
    }

    private void saveTokenToSession(String token) {
        SessionManager.getNewInstance().saveAuthToken(token);
    }
}
