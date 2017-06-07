package com.singh.sudhanshu.epaytest.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.singh.sudhanshu.epaytest.R;


/**
 * Created by Sudhanshu on 07/06/17.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private String[] data;
    String tempValues = null;
    LayoutInflater inflater;

    public CustomSpinnerAdapter(Activity activitySpinner, int textViewResourceId, String[] objects) {
        super(activitySpinner, textViewResourceId, objects);
        activity = activitySpinner;
        data = objects;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.row_spinner, parent, false);
        tempValues = null;
        tempValues = (String) data[position];

        TextView label = (TextView) row.findViewById(R.id.spinner_title);

        label.setText(tempValues);

        return row;
    }
}