package com.example.coupon_managment_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class FragCustomerLVAdapter extends ArrayAdapter<Customer> {
    Context context;
    ArrayList<Customer> customersArrayList;
    int line;

    public FragCustomerLVAdapter(@NonNull Context context, int line, @NonNull ArrayList<Customer> customers) {
        super(context, line, customers);
        this.context = context;
        this.customersArrayList = customers;
        this.line = line;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /**Creating Custom View*/
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(line, parent, false);

        Customer customer = customersArrayList.get(position);
        TextView tvId, tvEmail, tvFirstName, tvLastName, tvPassword;
        tvId = myView.findViewById(R.id.fragCust_LVline_id);
        tvEmail = myView.findViewById(R.id.fragCust_LVline_email);
        tvFirstName = myView.findViewById(R.id.fragCust_LVline_FirstName);
        tvLastName = myView.findViewById(R.id.fragCust_LVline_LastName);
        tvPassword = myView.findViewById(R.id.fragCust_LVline_Password);

        tvId.setText(customer.getId()+"");
        tvEmail.setText(customer.getEmail());
        tvFirstName.setText(customer.getFirstName());
        tvLastName.setText(customer.getLastName());
        tvPassword.setText(customer.getPassword());

        return myView;
    }

    public void refreshAllCustomers(ArrayList<Customer> customers) {
        clear();
        addAll(customers);
        notifyDataSetChanged();
    }

    public void refreshCustomerAdded(Customer customer) {
        add(customer);
        notifyDataSetChanged();
    }

}
