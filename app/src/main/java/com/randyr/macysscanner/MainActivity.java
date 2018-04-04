package com.randyr.macysscanner;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.randyr.macysscanner.mvvm.ViewBuilder;
import com.randyr.macysscanner.mvvm.ViewModel;
import com.randyr.macysscanner.scannerTools.PermitChecker;
import com.randyr.macysscanner.scannerTools.ScannerHandler;
import com.randyr.macysscanner.scannerTools.ScannerService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] perms;

    public static final String PURPOSE = "purpose";
    public static final String DATALRG = "largest_files";
    public static final String DATAAVG = "average_size";
    public static final String DATAFREQ = "frequent_extensions";

    private ScannerHandler scannerHandler;
    private Comm comm;
    private boolean dataDropped, scanPressed;
    private Bundle resultsBund;
    private TextView startScan, stopScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(receiver, new IntentFilter(MainActivity.class.getSimpleName()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MacysApplication.setVisible(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            resultsBund = getIntent().getExtras();
            dataDropped = true;
        } catch (NullPointerException e) {
            Log.d("Main", "Error 1");
        }

        PackageInfo pmg = null;
        try {
            pmg = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);

            if((perms = PermitChecker.checkPermit(this, pmg.requestedPermissions, true)) == null) {
                initUi();
                initData();
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            //Error to handle
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scannerHandler.stopService();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MacysApplication.setVisible(false);
    }

    private void initData() {

        comm = new Comm() {
            @Override
            public void dataDrop(Bundle drop) {
                scanPressed = false;
                resultsBund = drop;
                dataDropped = true;
//                toggleCards(true);

                Bundle fragExt = new Bundle();

                //Handle Data

                if (resultsBund.containsKey(DATALRG)) {
                    fragExt.putStringArrayList(DATALRG, resultsBund.getStringArrayList(DATALRG));
                }

                fragExt.putString(PURPOSE, DATAAVG);
                fragExt.putFloat(DATAAVG, resultsBund.getFloat(DATAAVG));
                fragExt.clear();

                fragExt.putString(PURPOSE, DATAFREQ);
                fragExt.putStringArrayList(DATAFREQ, resultsBund.getStringArrayList(DATAFREQ));
                fragExt.clear();

                scannerHandler = ScannerHandler.getInstance(getApplicationContext(), comm);
            }
        };
        scannerHandler = ScannerHandler.getInstance(this, comm);
    }

    private void initUi() {
        startScan = (TextView) findViewById(R.id.main_footer_start);
        stopScan = (TextView) findViewById(R.id.main_footer_stop);
        startScan.setOnClickListener(this);
        stopScan.setOnClickListener(this);
    }

    private void initViews(Intent intent) {

        if(intent.hasExtra(ScannerService.PURPOSERES)) {
            Bundle extras = intent.getBundleExtra(ScannerService.PURPOSERES);

            if(extras.containsKey(DATALRG)) {
                ((RelativeLayout) findViewById(R.id.card_0)).getChildAt(0).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.card_0)).
                        addView(ViewBuilder.build(this, DATALRG, extras.getStringArrayList(DATALRG),null));
            }

            if(extras.containsKey(DATAAVG)) {
                ((RelativeLayout) findViewById(R.id.card_1)).getChildAt(0).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.card_1)).
                        addView(ViewBuilder.build(this, DATAAVG, null, String.valueOf(extras.getFloat(DATAAVG))));
            }

            if(extras.containsKey(DATAFREQ)) {
                ((RelativeLayout) findViewById(R.id.card_2)).getChildAt(0).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.card_2)).
                        addView(ViewBuilder.build(this, DATAFREQ, extras.getStringArrayList(DATAFREQ),null));
            }
        }
        scanPressed = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_footer_start:
                if (!scanPressed) {
                    ((RelativeLayout) findViewById(R.id.card_0)).getChildAt(0).setVisibility(View.VISIBLE);
                    ((RelativeLayout) findViewById(R.id.card_1)).getChildAt(0).setVisibility(View.VISIBLE);
                    ((RelativeLayout) findViewById(R.id.card_2)).getChildAt(0).setVisibility(View.VISIBLE);
                    scanPressed = true;
                    Thread loadDelay = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                scannerHandler.beginScan();
                            } catch (InterruptedException e) {
                            }
                        }
                    });
                    loadDelay.start();
                }
                if (dataDropped) {
                    dataDropped = false;
                }
                break;
            case R.id.main_footer_stop:

                if (scanPressed) {
                    scannerHandler.stopScan();
                    scanPressed = false;
                }

                if (!(dataDropped)) {
                    scanPressed = false;
                }
                break;
        }
    }

    public interface Comm {
        void dataDrop(Bundle drop);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initUi();
        initData();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initViews(intent);
        }
    };
}


