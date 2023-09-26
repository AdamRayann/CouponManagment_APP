package com.example.coupon_managment_app;

import android.content.Context;

import java.io.Serializable;

public abstract class ClientFacade{
    protected final Context context;
    protected final CompaniesDAO companiesDAO;
    protected final CustomersDAO customersDAO;
    protected final CouponsDAO couponsDAO;

    public ClientFacade(Context context) {
        this.context = context;
        this.companiesDAO = CompaniesDBDAO.getInstance(context);
        this.customersDAO = CustomersDBDAO.getInstance(context);
        this.couponsDAO = CouponsDBDAO.getInstance(context);
    }

    public abstract boolean login(String email, String password);
}
