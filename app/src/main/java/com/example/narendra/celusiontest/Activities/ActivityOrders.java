package com.example.narendra.celusiontest.Activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.narendra.celusiontest.Adapters.AdapterOrder;
import com.example.narendra.celusiontest.R;
import com.example.narendra.celusiontest.db.CustomerOrder;
import com.example.narendra.celusiontest.db.DaoMaster;
import com.example.narendra.celusiontest.db.DaoSession;
import com.example.narendra.celusiontest.db.OrderDao;

import java.util.ArrayList;


/**
 * Created by Narendra on 2/7/2016.
 */
public class ActivityOrders extends AppCompatActivity {

    private ListView listView;
    private SQLiteDatabase db;
    private ArrayList<CustomerOrder> customerOrders = new ArrayList<>();
    private AdapterOrder adapterOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Orders");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView = (ListView)findViewById(R.id.listView);

        adapterOrder = new AdapterOrder(this, customerOrders);
        listView.setAdapter(adapterOrder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "celusion-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        OrderDao orderDao = daoSession.getOrderDao();
        customerOrders.clear();
        customerOrders.addAll(orderDao.loadAll());
        adapterOrder.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }
}
