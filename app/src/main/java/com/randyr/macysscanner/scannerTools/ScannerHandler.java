package com.randyr.macysscanner.scannerTools;

import android.content.Context;
import android.content.Intent;

import com.randyr.macysscanner.MainActivity;

/**
 * Created by Isa on 9/8/16.
 * <p>
 * Class purpose is to relay commands from the Activity to the Service
 */
public class ScannerHandler {
    private Context mContext;
    private static ScannerHandler instance;

    public static ScannerHandler getInstance(Context context, MainActivity.Comm comm) {
        if (instance == null) {
            instance = new ScannerHandler();
            instance.mContext = context;
        }
        return instance;
    }


    public void beginScan() {
        Intent intent = new Intent(mContext, ScannerService.class);
        mContext.startService(intent);
    }

    public void stopScan() {
        Intent intent = new Intent(mContext, ScannerService.class);
        mContext.stopService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(mContext, ScannerService.class);
        mContext.startService(intent);
    }
}
