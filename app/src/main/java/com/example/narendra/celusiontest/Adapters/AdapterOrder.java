package com.example.narendra.celusiontest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narendra.celusiontest.R;
import com.example.narendra.celusiontest.db.CustomerOrder;

import java.util.ArrayList;

/**
 * Created by Narendra on 2/7/2016.
 */
public class AdapterOrder extends BaseAdapter {

    private LayoutInflater inflator;
    private Context context;
    private ArrayList<CustomerOrder> customerOrders;

    public AdapterOrder(Context context, ArrayList<CustomerOrder> customerOrders) {
        this.context = context;
        this.customerOrders = customerOrders;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return customerOrders.size();
    }

    @Override
    public CustomerOrder getItem(int position) {
        return customerOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHolder orderViewHolder;
        if(convertView == null){
            convertView = inflator.inflate(R.layout.item_orders_list, parent, false);
            orderViewHolder = new OrderViewHolder();
            orderViewHolder.orderIdTextView = (TextView)convertView.findViewById(R.id.order_id_text_view);
            orderViewHolder.requiredDateTextView = (TextView) convertView.findViewById(R.id.required_date_text_view);
            orderViewHolder.shipAddressTextView = (TextView) convertView.findViewById(R.id.ship_address_text_view);
            orderViewHolder.shipCountryTextView = (TextView) convertView.findViewById(R.id.ship_country_text_view);
            convertView.setTag(orderViewHolder);
        }else{
            orderViewHolder = (OrderViewHolder) convertView.getTag();
        }

        orderViewHolder.orderIdTextView.setText(customerOrders.get(position).getOrderID()+"");
        orderViewHolder.requiredDateTextView.setText(customerOrders.get(position).getRequiredDate());
        orderViewHolder.shipAddressTextView.setText(customerOrders.get(position).getShipAddress());
        orderViewHolder.shipCountryTextView.setText(customerOrders.get(position).getShipCountry());

        return convertView;
    }

    class OrderViewHolder{
        TextView orderIdTextView, requiredDateTextView, shipAddressTextView, shipCountryTextView;
    }
}
