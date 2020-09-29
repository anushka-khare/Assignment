package com.daffodil.assignment.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Session Manager
 */

public class SessionManager implements ISessionManager {

    private static final String DEFAULT_USER_DATA = null;
    private final String PREFERENCE_FILE_NAME = "user_session_file";
    private final String PREFERENCE_FIREBASE_FILE = "preference_firebase_file";
    private final String USER_PREFERENCE_KEY = "USER_PREFERENCE_KEY";
    private final String USER_AUTH_TOKEN = "USER_AUTH_TOKEN";
    private final String USER_FIREBASE_AUTH_TOKEN = "USER_FIREBASE_AUTH_TOKEN";
    private final String COMMENT_DRAFT = "comment_draft";
    private final String COMMENT_DRAFT_PRODUCT_IDS = "comment_draft_product_ids";
    private final String NEW_CHAT_MESSAGE = "new_chat_message";
    private final String LANGUAGE = "language";
    private final String LOGIN_DETAIL_ID = "login_detail_id";

    private SharedPreferences mSharedPreference;
    private SharedPreferences fireBaseSharedPreference;
    private static ISessionManager sInstance;

    public synchronized static ISessionManager getNewInstance() {
        if (sInstance == null) {

            sInstance = new SessionManager();
        }
        return sInstance;
    }

    @Override
    public void initialize(Context context) {
        mSharedPreference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        fireBaseSharedPreference = context.getSharedPreferences(PREFERENCE_FIREBASE_FILE, Context.MODE_PRIVATE);

    }



    @Override
    public void saveAuthToken(String token) {
        checkForInitialization();
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(USER_AUTH_TOKEN, token).apply();

    }



    @Override
    public String readAuthToken() throws IllegalStateException {
        checkForInitialization();
        String auth = mSharedPreference.getString(USER_AUTH_TOKEN, DEFAULT_USER_DATA);
        return auth;
    }




    /**
     * Throws IllegalStateException if initialization has not been performed.
     */
    protected void checkForInitialization() {
        if (mSharedPreference == null) {
            throw new IllegalStateException("Initialization is not performed yet.");
        }
    }




    public void saveNewUnreadMessage(boolean unreadMessage) {
        checkForInitialization();
        mSharedPreference.edit().putBoolean(NEW_CHAT_MESSAGE, unreadMessage).apply();
    }

    public boolean hasNewUnreadMessage() {
        checkForInitialization();
        return mSharedPreference.getBoolean(NEW_CHAT_MESSAGE, false);
    }


}
