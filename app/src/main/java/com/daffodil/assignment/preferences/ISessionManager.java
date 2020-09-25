package com.daffodil.assignment.preferences;

import android.content.Context;



/**
 * Interface to be implemented by any session managing class.
 */
public interface ISessionManager {

    /**
     * Initialize session's shared preference.
     *
     * @param context context.
     */
    void initialize(Context context);


    void saveAuthToken(String token);

       /**
     * @return provider for auth token if initialized, else null.
     * @throws IllegalStateException if initialize has not be called before.
     */
    String readAuthToken() throws IllegalStateException;

}
