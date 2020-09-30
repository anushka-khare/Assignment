package com.daffodil.assignment.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class DeviceUtils {

    private static final String LOG_TAG = "DeviceUtils";

    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static boolean isAndroidEmulator() {
        String product = Build.PRODUCT;
        if (product == null)
            return false;
        Log.d(LOG_TAG, "product=" + product);
        return product.matches(".*_?sdk_?.*");
    }

    public static int getDeviceScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        Point size = new Point();
        if (display != null) {
            display.getSize(size);
        }
        return size.x;
    }

    public static int getDeviceScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        Point size = new Point();
        if (display != null) {
            display.getSize(size);
        }
        return size.y;
    }

    public static int getActionbarHeight(Context context) {
        int actionBarHeight = 0;
        try {
            final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            actionBarHeight = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
        } catch (Exception e) {
            return actionBarHeight;
        }

        return actionBarHeight;
    }


    public static Number getDeviceVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
