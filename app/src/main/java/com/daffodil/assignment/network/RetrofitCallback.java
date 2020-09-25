package com.daffodil.assignment.network;

import androidx.annotation.NonNull;

import com.daffodil.assignment.base.ErrorResponse;


/**
 * The Response callback invoked after the result is received and analysed.
 * @param <T> the type of the response.
 */
public interface RetrofitCallback<T> {

    /**
     * Invoked when we have the success result in the defined format from the APIs.
     * @param data the success response data.
     */
    void onSuccess(@NonNull T data);

    /**
     * Invoked when we encounter any isError from the APIs.
     * @param errorResponse the isError response with proper display message
     *                      and proper information for debugging.
     */
    void onFailure(@NonNull ErrorResponse errorResponse);

}
