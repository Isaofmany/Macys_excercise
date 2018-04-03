package com.randyr.macysscanner.scannerTools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Isa on 4/2/18.
 */

public class PermitChecker {

    public static String[] checkPermit(Context context, String[] permits, boolean requestPermission) {

        boolean bool = false;
        ArrayList<String> permitList = new ArrayList<>((List<String>) Arrays.asList(permits));
        for(int x = 0; x < permitList.size(); x++) {
            int res = ContextCompat.checkSelfPermission(context, permitList.get(x));
            if (res == PackageManager.PERMISSION_GRANTED) {
                permitList.remove(x);
                x--;
            }
            Log.d("", String.valueOf(res));
        }

        if(permitList.size() == 0) {
            return null;
        }
        else {
            String[] perms = null;
            perms = new String[permitList.size()];
            permitList.toArray(perms);
            if (requestPermission) {
                ActivityCompat.requestPermissions(((Activity) context), perms, 0);
            }
            return perms;
        }
    }
}