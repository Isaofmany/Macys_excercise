package com.randyr.macysscanner.mvvm;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.randyr.macysscanner.MainActivity;
import com.randyr.macysscanner.R;
import com.randyr.macysscanner.databinding.AverageBinding;
import com.randyr.macysscanner.databinding.FrequentBinding;
import com.randyr.macysscanner.databinding.LargestBinding;

import java.util.ArrayList;

/**
 * Created by Isa on 9/16/16.
 */
public class FragmentBuilder {

    public static View build(Context context, String type, ArrayList<String> data, String text, View view) {

        switch (type) {
            case MainActivity.DATALRG:
                view = LayoutInflater.from(context).inflate(R.layout.largest, null);
                ViewModel viewModel = new ViewModel(context, data, null, view.findViewById(R.id.root));
                LargestBinding largestBinding = LargestBinding.bind(view);
                largestBinding.setItem(viewModel);
                return view;
            case MainActivity.DATAAVG:
                view = LayoutInflater.from(context).inflate(R.layout.average, null);
                viewModel = new ViewModel(context, null, text, view.findViewById(R.id.root));
                AverageBinding fragAverageBinding = AverageBinding.bind(view);
                fragAverageBinding.setItem(viewModel);
                return view;
            case MainActivity.DATAFREQ:
                view = LayoutInflater.from(context).inflate(R.layout.average, null);
                viewModel = new ViewModel(context, null, null, view.findViewById(R.id.root));
                FrequentBinding fragFrequentBinding = FrequentBinding.bind(view);
                fragFrequentBinding.setItem(viewModel);
                return view;
        }
        return null;
    }
}
