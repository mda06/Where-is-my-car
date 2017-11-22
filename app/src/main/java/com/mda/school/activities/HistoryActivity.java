package com.mda.school.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.mda.school.adapters.CarAdapter;
import com.mda.school.persistence.DBHelper;

public class HistoryActivity extends AppCompatActivity {

    private ListView mList;
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
        mList.setAdapter(new CarAdapter(this, db.getAllCars()));
    }

}
