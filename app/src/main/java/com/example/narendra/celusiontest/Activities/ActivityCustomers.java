package com.example.narendra.celusiontest.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.narendra.celusiontest.Adapters.AdapterCustomer;
import com.example.narendra.celusiontest.Application.CelusionAplication;
import com.example.narendra.celusiontest.R;
import com.example.narendra.celusiontest.Utilities.Utility;
import com.example.narendra.celusiontest.db.Customer;
import com.example.narendra.celusiontest.db.CustomerDao;
import com.example.narendra.celusiontest.db.CustomerOrder;
import com.example.narendra.celusiontest.db.DaoMaster;
import com.example.narendra.celusiontest.db.DaoSession;
import com.example.narendra.celusiontest.db.OrderDao;
import com.example.narendra.celusiontest.network.VolleyRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

public class ActivityCustomers extends AppCompatActivity {

    private static String URL;
    private ArrayList<Customer> customers = new ArrayList<>();

    private View circularProgressbar;
    private View networkErrorLayout;
    private TextView errorTextView;
    private ListView listView;
    private SQLiteDatabase db;
    private AdapterCustomer adapterCustomer;
    private String selectedCutomer;
    private int which;
    private Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            if (which == 100) {
                loadCustomerData(response);
            } else if (which == 200) {
                loadOrderData(response);
            }
            circularProgressbar.setVisibility(View.GONE);
        }
    };

    private void loadCustomerData(JSONArray response) {
        try {
            //  Log.d("Narendra", response.get(0).toString());
            DaoMaster daoMaster = new DaoMaster(db);
            DaoSession daoSession = daoMaster.newSession();

            CustomerDao customerDao = daoSession.getCustomerDao();
            Gson gson = new Gson();
            for (int i = 0; i <= response.length(); i++) {
                Customer customer = gson.fromJson(response.get(i).toString(), Customer.class);
             //   Log.d("Narendra", i + "  " + response.get(i).toString());
                customerDao.insertOrReplace(customer);
            }
            customers.addAll(customerDao.loadAll());
            adapterCustomer.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d("Narendra", e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadOrderData(JSONArray response) {
        try {
            //  Log.d("Narendra", response.get(0).toString());
            DaoMaster daoMaster = new DaoMaster(db);
            DaoSession daoSession = daoMaster.newSession();

            OrderDao orderDao = daoSession.getOrderDao();
            Gson gson = new Gson();
            for (int i = 0; i <= response.length(); i++) {
                CustomerOrder customerOrder = gson.fromJson(response.get(i).toString(), CustomerOrder.class);
             //   Log.d("Narendra", i + "  " + response.get(i).toString());
                orderDao.insertOrReplace(customerOrder);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivityOrders();
    }

    private void startActivityOrders() {
        Intent intent = new Intent(ActivityCustomers.this, ActivityOrders.class);
        startActivity(intent);
    }

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Narendra", error.toString());
            circularProgressbar.setVisibility(View.GONE);
            Toast.makeText(ActivityCustomers.this, error.getMessage(), Toast.LENGTH_LONG);
        }
    };


    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DaoMaster daoMaster = new DaoMaster(db);
            DaoSession daoSession = daoMaster.newSession();

            OrderDao orderDao = daoSession.getOrderDao();
            selectedCutomer = adapterCustomer.getItem(position).getCustomerID();
            List<CustomerOrder> orders = orderDao.loadAll();
            long count = orderDao.queryBuilder().where(new WhereCondition.StringCondition("CUSTOMER_ID = ?", selectedCutomer)).count();

            Log.d("Narendra order count", count+"");

            if (count > 0) {
                startActivityOrders();
            } else {
                which = 200;
                performNetwokCall(null);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Customers");
        }

        circularProgressbar = findViewById(R.id.circular_progressbar);
        networkErrorLayout = findViewById(R.id.network_error_layout);
        errorTextView = (TextView) findViewById(R.id.error_textView);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(listItemClickListener);
        adapterCustomer = new AdapterCustomer(this, customers);
        listView.setAdapter(adapterCustomer);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "celusion-db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        CustomerDao customerDao = daoSession.getCustomerDao();
        long count = customerDao.count();
        if (count <= 0) {
            which = 100;
            performNetwokCall(null);
        } else {
            customers.clear();
            customers.addAll(customerDao.loadAll());
            adapterCustomer.notifyDataSetChanged();
        }
        Log.d("Narendra", count + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    public void performNetwokCall(View v) {
        if (Utility.isConnectedToInternet(ActivityCustomers.this)) {
            circularProgressbar.setVisibility(View.VISIBLE);
            networkErrorLayout.setVisibility(View.GONE);
            JSONObject jsonObject = new JSONObject();
            if (which == 100) {
                URL = "http://188.40.74.207:8888/api/customer";
            } else if(which == 200) {
                URL = "http://188.40.74.207:8888/api/order/" + selectedCutomer;
            }
            VolleyRequest volleyRequest = new VolleyRequest(Request.Method.GET, URL, jsonObject, successListener, errorListener);
            CelusionAplication.getInstance().addToRequestQueue(volleyRequest);
        } else {
            showNetworkCallError(true);
        }
    }

    public void showNetworkCallError(boolean isNetworkConnectionError) {
        if (isNetworkConnectionError) {
            errorTextView.setText("Seems you don't have a network connection. Please connect to a network and try again");
        } else {
            errorTextView.setText("Unable to process request. \n Try after some time");
        }
        circularProgressbar.setVisibility(View.GONE);
        networkErrorLayout.setVisibility(View.VISIBLE);
    }
}
