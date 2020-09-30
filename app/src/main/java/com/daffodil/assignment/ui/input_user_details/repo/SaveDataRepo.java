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
        public static final String COLUMN_LAT_LNG = "latLng";
        public static final String COLUMN_PLACE = "place";

    }

    public static class UserImage implements BaseColumns {
        public static final String TABLE_IMAGE = "user_img";
        public static final String COLUMN_IMAGE_PATH = "imgPath";
        public static final String COLUMN_BACK_IMAGE_PATH = "backImgPath";
        public static final String COLUMN_ID_TYPE = "idType";
        public static final String COLUMN_USER_ID = "userId";
        public static final String IMG_ID = "IMG_ID";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME + " TEXT," +
                    UserEntry.COLUMN_MOBILE + " TEXT," +
                    UserEntry.COLUMN_EMAIL + " TEXT," +
                    UserEntry.COLUMN_LAT_LNG + " TEXT," +
                    UserEntry.COLUMN_PLACE + " TEXT )";

    public static final String SQL_CREATE_IMAGE_TABLE =
            "CREATE TABLE " + UserImage.TABLE_IMAGE + " (" +
                    UserImage.IMG_ID + " INTEGER PRIMARY KEY," +
                    UserImage.COLUMN_ID_TYPE + " TEXT ," +
                    UserImage.COLUMN_IMAGE_PATH + " TEXT ," +
                    UserImage.COLUMN_BACK_IMAGE_PATH + " TEXT ," +
                    UserImage.COLUMN_USER_ID + " INTEGER," +
                    " FOREIGN KEY (" + UserImage.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "));";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


    private static SQLiteDatabase database;

    public SaveDataRepo(UserDataHelper userDataHelper) {
        if (database == null) {
            database = userDataHelper.getWritableDatabase();
        }
    }

    public long insertUserData(String name, String mobile, String email, String latLng, String place) {
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, name);
        values.put(UserEntry.COLUMN_MOBILE, mobile);
        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(UserEntry.COLUMN_LAT_LNG, latLng);
        values.put(UserEntry.COLUMN_PLACE, place);

        // Insert the new row, returning the primary key value of the new row
        return database.insert(UserEntry.TABLE_NAME, null, values);
    }

    public void fetchUser(long id, BaseViewModelImp.ViewModelCallback<UserDetail> callback) {

        UserDetail userDetail = new UserDetail();


        Cursor cursor = database.query(UserEntry.TABLE_NAME, null, UserEntry._ID + "=?", new String[]{String.valueOf(id)}, null, null, UserEntry.COLUMN_NAME);

        if (cursor != null) {

            try {
                cursor.moveToFirst();
                userDetail.setId(cursor.getInt(cursor.getColumnIndex(UserEntry._ID)));
                userDetail.setName(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME)));
                userDetail.setMobile(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_MOBILE)));
                userDetail.setEmail(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_EMAIL)));
                callback.onSuccess(userDetail);

            } catch (SQLException e) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setDetail(e.getMessage());
                callback.onError(errorResponse);
            } finally {
                cursor.close();

            }
        }

    }

    public long insertUserImage(String userId, String imagePath, String backImgPath, String idType) {
        ContentValues values = new ContentValues();
        values.put(UserImage.COLUMN_USER_ID, userId);
        values.put(UserImage.COLUMN_IMAGE_PATH, imagePath);
        values.put(UserImage.COLUMN_BACK_IMAGE_PATH, backImgPath);
        values.put(UserImage.COLUMN_ID_TYPE, idType);
        return database.insert(UserImage.TABLE_IMAGE, null, values);
    }

    public void getUserDetailWithImg(String userId, BaseViewModelImp.ViewModelCallback<UserDetail> callback) {
        UserDetail userDetail = new UserDetail();
        String rawQuery = "SELECT * FROM " + UserEntry.TABLE_NAME + " inner join " + UserImage.TABLE_IMAGE + " ON " + UserEntry._ID + " = " + UserImage.COLUMN_USER_ID +
                " WHERE " + UserImage.COLUMN_USER_ID + " =?";

        Cursor cursor = database.rawQuery(rawQuery, new String[]{userId});
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                userDetail.setId(cursor.getInt(cursor.getColumnIndex(UserEntry._ID)));
                userDetail.setName(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME)));
                userDetail.setMobile(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_MOBILE)));
                userDetail.setEmail(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_EMAIL)));
                userDetail.setLatLng(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_LAT_LNG)));
                userDetail.setPlace(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PLACE)));
                userDetail.setImgPath(cursor.getString(cursor.getColumnIndex(UserImage.COLUMN_IMAGE_PATH)));
                userDetail.setBackImgPath(cursor.getString(cursor.getColumnIndex(UserImage.COLUMN_BACK_IMAGE_PATH)));
                userDetail.setIdType(cursor.getString(cursor.getColumnIndex(UserImage.COLUMN_ID_TYPE)));

                callback.onSuccess(userDetail);

            } catch (SQLException e) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setDetail(e.getMessage());
                callback.onError(errorResponse);
            } finally {
                cursor.close();

            }
        }
    }


}
