package com.daffodil.assignment.ui.input_user_details.repo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.daffodil.assignment.ui.input_user_details.repo.SaveDataRepo.SQL_CREATE_ENTRIES;
import static com.daffodil.assignment.ui.input_user_details.repo.SaveDataRepo.SQL_CREATE_IMAGE_TABLE;
import static com.daffodil.assignment.ui.input_user_details.repo.SaveDataRepo.SQL_DELETE_ENTRIES;

public class UserDataHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";

    private static UserDataHelper userDataHelper = null;
    public UserDataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static UserDataHelper getUserDataHelper(Context context){

        if(userDataHelper == null){
            userDataHelper = new UserDataHelper(context);
        }
        return userDataHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
