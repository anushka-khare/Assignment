package com.daffodil.assignment.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.daffodil.assignment.R;

public class Utility {


    private static ProgressDialog progressDialog;

    public static String getMimeTypeByExtension(String ext) {
        switch (ext) {
            case "jpeg":
                return "image/jpeg";
            case "jpg":
                return "image/jpg";
            case "png":
                return "image/png";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "xml":
                return "application/xml";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "mp4":
                return "video/mp4";
            case "zip":
                return "application/zip";
            case "mp3":
                return "audio/*";
            default:
                return "";
        }
    }

    public static void showLoader(Context context) {
         progressDialog = ProgressDialog.show(context, context.getString(R.string.loading), context.getString(R.string.wait));
         progressDialog.setCancelable(false);
    }

    public static void dismissLoader(){

        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermission(Activity activity, String[] permissions, int PERMISSION_REQUEST_CODE) {
        for (String permission : permissions) {
            if (!checkPermission(activity, permission)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission}, PERMISSION_REQUEST_CODE);

            }
        }

    }

    public static String getImagePath(Context context, Uri uri){
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};

        return getDataColumn(context, contentUri, selection,
                selectionArgs);
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }


}
