package com.mda.school.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.mda.school.adapters.CarAdapter;
import com.mda.school.model.Car;
import com.mda.school.persistence.DBHelper;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView mList;
    private CarAdapter adapter;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Go to dashboard
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        db = new DBHelper(this);

        mList = (ListView)findViewById(R.id.lst_history);
        adapter = new CarAdapter(this, db.getAllCars());
        mList.setAdapter(adapter);
    }

    public void onClearDatabaseClicked(View view) {
        final List<Car> tmp = db.getAllCars();
        db.removeCars();
        adapter.clear();
        adapter.notifyDataSetChanged();
        final View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(parentLayout, getString(R.string.txt_database_deleted), Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbarUndo = Snackbar.make(parentLayout, getString(R.string.txt_database_restored), Snackbar.LENGTH_SHORT);
                        snackbarUndo.show();
                        for(Car c : tmp)
                            db.addCar(c);
                        adapter.addAll(tmp);
                        adapter.notifyDataSetChanged();
                    }
                });

        snackbar.show();
    }
}
