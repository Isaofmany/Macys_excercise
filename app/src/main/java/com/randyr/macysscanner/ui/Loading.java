package com.randyr.macysscanner.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.randyr.macysscanner.R;

//import oasistechnologies.hoopmaps.R;

/**
 * Created by Isa on 7/25/17.
 */

public class Loading /**extends Fragment **/ {

    private final int MAX = 100;
    private View view;
    private ProgressBar progressBar;
    private Thread progThread;
    private int prog;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.loading, null);
//        return view;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        initUi();
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        progThread.interrupt();
//    }

    public View getView() {
        return view;
    }

//    private void initUi() {
//        progressBar = (ProgressBar) view.findViewById(R.id.summary_progress_bar);
//        beginAnim();
//    }

    public void setProg(int prog) {
        this.prog = prog;
    }

    private void beginAnim() {
        progThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (prog < MAX) {
                    progressBar.setProgress(prog);
                }
                progressBar.setProgress(prog);
            }
        });
        progThread.start();
    }
}
