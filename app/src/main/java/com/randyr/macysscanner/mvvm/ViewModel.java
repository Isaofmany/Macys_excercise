package com.randyr.macysscanner.mvvm;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.randyr.macysscanner.R;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Isa on 3/28/18.
 */

public class ViewModel extends BaseObservable {

    private Context context ;
    private Model model;
    private int position;

    public ViewModel(Context context, ArrayList<String> data, String text, View view) {
        this.context = context;
        model = new Model(view, text, data);
    }

    public void build() {
        if(model.getData() != null) {
            MAdapter adapter = new MAdapter(context, model.getData());
            ((ListView) model.getView().findViewById(R.id.list)).setAdapter(adapter);
        }
    }

    @Bindable
    public String getText() {
        return model.getText();
    }

    private class MAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> data;

        public MAdapter(@NonNull Context context, ArrayList<String> data) {
            super(context, 0, data);
            this.data = data;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
                convertView.setTag(position);
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(model.getData().get(position));
            return convertView;
        }
    }
}
