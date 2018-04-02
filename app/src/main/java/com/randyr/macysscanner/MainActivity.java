package com.randyr.macysscanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.randyr.macysscanner.scannerTools.ScannerHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            resultsBund = getIntent().getExtras();
            dataDropped = true;
        } catch (NullPointerException e) {
            Log.d("Main", "Error 1");
        }


        int pc0 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && pc0 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        initUi();
        initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scannerHandler.stopService();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        if (dataDropped) {
            //Make Visible
        }
        startScan = (TextView) findViewById(R.id.main_footer_start);
        stopScan = (TextView) findViewById(R.id.main_footer_stop);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_footer_start:

                if (!scanPressed) {
                    scannerHandler.beginScan();
                    scanPressed = true;
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
}


