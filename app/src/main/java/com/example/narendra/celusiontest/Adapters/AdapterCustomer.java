package com.example.narendra.celusiontest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.narendra.celusiontest.R;
import com.example.narendra.celusiontest.db.Customer;

import java.util.ArrayList;

/**
 * Created by Narendra on 2/5/2016.
 */
public class AdapterCustomer extends BaseAdapter {

    private LayoutInflater inflator;
    private ArrayList<Customer> customers;
    private Context context;

    public AdapterCustomer(Context context, ArrayList<Customer> customers) {
        this.context = context;
        this.customers = customers;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Customer getItem(int position) {
        return customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomerViewHolder customerViewHolder;

        if(convertView == null){
            convertView = inflator.inflate(R.layout.item_customers_list, parent, false);
            customerViewHolder = new CustomerViewHolder();
            customerViewHolder.contactNameTextView = (TextView) convertView.findViewById(R.id.contact_name_text_view);
            customerViewHolder.companyNameTextView = (TextView) convertView.findViewById(R.id.company_name_text_view);
            customerViewHolder.countryTextView = (TextView) convertView.findViewById(R.id.country_text_view);
            customerViewHolder.postalCodeTextView = (TextView) convertView.findViewById(R.id.postal_code_text_view);
            customerViewHolder.phoneTextView = (TextView) convertView.findViewById(R.id.phone_no_text_view);
            convertView.setTag(customerViewHolder);
        }else{
            customerViewHolder = (CustomerViewHolder) convertView.getTag();
        }

        customerViewHolder.contactNameTextView.setText(customers.get(position).getContactName());
        customerViewHolder.companyNameTextView.setText(customers.get(position).getCompanyName());
        customerViewHolder.countryTextView.setText(customers.get(position).getCountry());
        customerViewHolder.postalCodeTextView.setText(customers.get(position).getPostalCode()+"");
        customerViewHolder.phoneTextView.setText(customers.get(position).getPhone());

        return convertView;
    }

    class CustomerViewHolder{
        TextView contactNameTextView, companyNameTextView, countryTextView, postalCodeTextView, phoneTextView;
    }
}
