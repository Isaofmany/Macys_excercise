package com.randyr.macysscanner.mvvm;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by Isa on 3/28/18.
 */

public class Model {

    private View view;
    private String text;
    private ArrayList<String> data;

    public Model(View view, String text, ArrayList<String> data) {
        this.view = view;
        this.text = text;
        this.data = data;
    }

    public View getView() {
        return view;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public String getText() {
        return text;
    }
}
