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


public class FragCompanyLVAdapter extends ArrayAdapter<Company> {
    Context context;
    ArrayList<Company> companiesArrayList;
    int line;

    public FragCompanyLVAdapter(@NonNull Context context, int line, @NonNull ArrayList<Company> companies) {
        super(context, line, companies);
        this.context = context;
        this.companiesArrayList = companies;
        this.line = line;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /**Creating Custom View*/
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(line, parent, false);

        Company company = companiesArrayList.get(position);
        TextView tvId, tvEmail, tvName, tvPassword;
        tvId = myView.findViewById(R.id.fragComp_LVline_id);
        tvEmail = myView.findViewById(R.id.fragComp_LVline_email);
        tvName = myView.findViewById(R.id.fragComp_LVline_Name);
        tvPassword = myView.findViewById(R.id.fragComp_LVline_Password);

        tvId.setText(company.getId()+"");
        tvEmail.setText(company.getEmail());
        tvName.setText(company.getName());
        tvPassword.setText(company.getPassword());

        return myView;
    }

    public void refreshAllCompanies(ArrayList<Company> companies) {
        clear();
        addAll(companies);
        notifyDataSetChanged();
    }

    public void refreshCompanyAdded(Company comapny) {
        add(comapny);
        notifyDataSetChanged();
    }
}
