package com.behraz.fastermixer.batch.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;


import com.behraz.fastermixer.batch.R;

import java.util.List;

    public class MySimpleSpinnerAdapter extends ArrayAdapter<Object> {

    private Context context;
    private Typeface iranSansLight;

    public MySimpleSpinnerAdapter(@NonNull Context context, int resource, List<Object> items) {
        super(context, resource, items);
        this.context = context;
        iranSansLight = ResourcesCompat.getFont(context, R.font.iransans_light);
    }

    // Affects default (closed) state of the spinner
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, null, parent);
        view.setTypeface(iranSansLight);
        view.setTextSize(14);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(context.getResources().getColor(R.color.gray700));
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(iranSansLight);
        view.setTextSize(14);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(context.getResources().getColor(R.color.white));
        view.setTextColor(context.getResources().getColor(R.color.gray700));
        return view;
    }
}

