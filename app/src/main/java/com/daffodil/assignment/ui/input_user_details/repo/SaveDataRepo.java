package com.daffodil.assignment.ui.input_user_details.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.ui.input_user_details.model.UserDetail;

public class SaveDataRepo {

    /* Inner class that defines the table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MOBILE = "mobile";
        public static final String COLUMN_EMAIL = "email";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME + " TEXT," +
                    UserEntry.COLUMN_MOBILE + " TEXT," +
                    UserEntry.COLUMN_EMAIL + " TEXT )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


    private UserDataHelper userDataHelper;
    private static SQLiteDatabase database;

    public SaveDataRepo(UserDataHelper userDataHelper) {
        this.userDataHelper = userDataHelper;
        database = userDataHelper.getWritableDatabase();
    }

    public long insertUserData(String name, String mobile, String email) {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, name);
        values.put(UserEntry.COLUMN_MOBILE, mobile);
        values.put(UserEntry.COLUMN_EMAIL, email);

        // Insert the new row, returning the primary key value of the new row
        return database.insert(UserEntry.TABLE_NAME, null, values);
    }

    public void fetchUser(long id, BaseViewModelImp.ViewModelCallback<UserDetail> callback) {

        UserDetail userDetail = new UserDetail();

        String rawQuery = "select * from " + UserEntry.TABLE_NAME + " where " + UserEntry._ID + " = " + id;

        Cursor cursor = database.query(UserEntry.TABLE_NAME, null, UserEntry._ID+"=?", new String[]{String.valueOf(id)}, null, null, UserEntry.COLUMN_NAME);

        if (cursor != null) {

            try {
                cursor.moveToFirst();
                userDetail.setId(cursor.getInt(cursor.getColumnIndex(UserEntry._ID)));
                userDetail.setName(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME)));
                userDetail.setMobile(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_MOBILE)));
                userDetail.setEmail(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_EMAIL)));
                callback.onSuccess(userDetail);

            }catch (SQLException e){
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setMessage(e.getMessage());
                callback.onError(errorResponse);
            }
            finally {
                cursor.close();

            }
        }

    }
}
