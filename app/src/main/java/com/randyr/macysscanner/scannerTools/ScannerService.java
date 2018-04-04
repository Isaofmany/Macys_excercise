package com.randyr.macysscanner.scannerTools;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.randyr.macysscanner.MacysApplication;
import com.randyr.macysscanner.MainActivity;
import com.randyr.macysscanner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Isa on 9/8/16.
 * <p>
 * * Class purpose is to handle all scanning processes, due to the fact that we need active and in-active scanning
 */
public class ScannerService extends IntentService {

    private final String EXTDIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    private String NOTIFTITLE = "Scan Complete";
    private String NOTIFMSG = "Click to Check Results Now";
    public static final String INTPURPOSE = "purpose";
    public static final String PURPOSERES = "push_results";

    private ArrayList<String> mFilesType;
    private ArrayList<String> filesCache, mFilesSized;

    private Notification.Builder notif;

    private int trk;

    public ScannerService() {
        super(ScannerService.class.getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                (Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()))) {
            scanAndBuild();
        }
//        else {
//
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void scanAndBuild() {

        filesCache = buildData(null, true);
        mFilesSized = orderBySize(new ArrayList(filesCache));
        orderByType(new ArrayList(filesCache));

        getDataDrop();
    }

    private ArrayList<String> buildData(File[] dir, boolean init) {

        File file;
        File[] list = null;
        ArrayList<String> files = new ArrayList<>();
        ArrayList<String> filesList = new ArrayList<String>();

        if (init) {
            String sd_card = Environment.getExternalStorageDirectory().getAbsolutePath();
            file = new File(sd_card);
            list = file.listFiles();

            for (int i = 0; i < list.length; i++) {
                filesList.add(list[i].getName());
            }
        } else {
            for (int i = 0; i < dir.length; i++) {
                filesList.add(dir[i].getName());
            }
        }

        files.addAll(filesList);

        if(list != null) {
            for (File temp : list) {
                if (temp.isDirectory()) {
                    files.addAll(buildData(temp.listFiles(), false));
                } else {
                    files.add(temp.getAbsolutePath());
                }
            }
        }
        return files;
    }

    private ArrayList<String> orderBySize(ArrayList<String> oldList) {
        if (oldList == null) {
            return new ArrayList<>();
        }

        ArrayList<File> newList = new ArrayList<>();
        File current;
        int tracker = 0;


        while (oldList.size() != 0) {
            current = new File(oldList.get(0));

            for (int x = 0; x < oldList.size(); x++) {
                if (!(current.length() <= oldList.get(x).length())) {
                    current = new File(oldList.get(x));
                    tracker = x;
                }
            }

            newList.add(current);
            oldList.remove(tracker);
        }

        ArrayList<String> ordered = new ArrayList();

        File file;

        if (newList.size() > 0) {
            for (int x = 0; x < 10; x++) {
                file = newList.get(x);
                ordered.add(file.getName() + " length = " + String.valueOf(file.length()));
            }
        }

        return ordered;
    }

    private void orderByType(ArrayList<String> oldList) {
        mFilesType = new ArrayList<>();
        if (oldList != null) {
            mFilesType = new ArrayList<>();
            String[] current;
            String compare;
            int tracker = -1;
            boolean multiple = false;

            while (oldList.size() != 0) {
                if (tracker == -1) {
                    mFilesType.add(oldList.get(0).substring(oldList.get(0).lastIndexOf(".") + 1) + "_" + String.valueOf(1));
                    oldList.remove(0);
                }

                int x = oldList.size() - 1;
                while (x > 0) {
                    for (int y = 0; y < mFilesType.size(); y++) {
                        compare = oldList.get(x).substring(oldList.get(x).lastIndexOf(".") + 1);

                        if (mFilesType.get(y).contains(compare)) {
                            current = new String[]{oldList.get(x).substring(oldList.get(x).lastIndexOf(".") + 1),
                                    mFilesType.get(y).substring(mFilesType.get(y).lastIndexOf("_") + 1)};
                            oldList.remove(x);
                            mFilesType.remove(y);
                            mFilesType.add(current[0] + "_" + String.valueOf(Integer.valueOf(current[1]).intValue() + 1));
                            if (x == 0) {
                                break;
                            } else {
                                x--;
                            }
                        } else {
                            mFilesType.add(oldList.get(x).substring(oldList.get(x).lastIndexOf(".") + 1) + "_" + String.valueOf(1));
                            oldList.remove(x);
                            if (x == 0) {
                                break;
                            } else {
                                x--;
                            }
                        }
                    }
                }
            }
        }
    }

    protected void getDataDrop() {

        Intent intent;
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

        if(MacysApplication.isVisible()) {
            intent = new Intent(MainActivity.class.getSimpleName());
            intent.putExtra(INTPURPOSE, PURPOSERES);
            intent.putExtra(PURPOSERES, bundleData());
            getApplicationContext().sendBroadcast(intent);
        }
        else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(INTPURPOSE, PURPOSERES);
            intent.putExtra(PURPOSERES, bundleData());
            PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            notif = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.notifcation_icon).
                    setContentTitle(NOTIFTITLE).setContentText(NOTIFTITLE).setContentIntent(pendInt);
//            notif.build();
        }
    }

    private Bundle bundleData() {
        Bundle bundle = new Bundle();
        try {
            bundle.putStringArrayList(MainActivity.DATALRG, mFilesSized);
            bundle.putFloat(MainActivity.DATAAVG, getAverage());
            bundle.putStringArrayList(MainActivity.DATAFREQ, getTopFive());
        } catch (Exception e) {
            //ToDo
            //Handle E
            Log.d("ScannerService", "Error 1");
        }
        return bundle;
    }

    private float getAverage() {
        float avg = 0;

        ArrayList<File> files = new ArrayList<>();
        filesCache = buildData(getApplicationContext().getFilesDir().listFiles(), true);

        for (String str : filesCache) {
            files.add(new File(str));
        }
        for (File file : files) {
            avg += file.length();
        }

        return (avg / files.size());
    }

    private ArrayList<String> getTopFive() {
        ArrayList<String> topFive = new ArrayList<>();
        ArrayList<String> oldList = new ArrayList<>();
        oldList.addAll(mFilesType);
        String current;
        trk = 0;

        while (topFive.size() != 5) {
            current = oldList.get(0);

            for (int x = 0; x < oldList.size(); x++) {
                if (Integer.valueOf(oldList.get(x).substring(oldList.get(x).lastIndexOf("_") + 1)).intValue() > Integer.valueOf(current.substring(current.lastIndexOf("_") + 1)).intValue()) {
                    current = mFilesType.get(x);
                    trk = x;
                }
            }
            topFive.add(current);
            oldList.remove(trk);
        }
        return topFive;
    }
}