package com.mda.school.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mda.school.activities.R;
import com.mda.school.model.Car;
import com.mda.school.util.Util;

import java.util.List;

/**
 * Created by michael on 22/11/17.
 */

public class CarAdapter extends ArrayAdapter<Car> {

    public CarAdapter(Context context, List<Car> array) {
        super(context, 0, array);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_listview, container, false);
        }

        TextView tvLocation = (TextView) convertView.findViewById(R.id.tv_item_location);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_item_date);

        Car c = getItem(position);
        tvLocation.setText(c.getAddress());
        tvDate.setText(Util.dateFormat(c.getDate()));

        return convertView;
    }

}
