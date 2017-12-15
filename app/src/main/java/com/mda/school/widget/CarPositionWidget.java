package com.mda.school.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.mda.school.activities.R;
import com.mda.school.model.Car;
import com.mda.school.persistence.DBHelper;

/**
 * Implementation of App Widget functionality.
 */
public class CarPositionWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        DBHelper db = new DBHelper(context);
        Car car = db.getFirstCar();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.car_position_widget);
        if(car != null && car.getAddress() != null)
            views.setTextViewText(R.id.appwidget_text, car.getAddress());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

