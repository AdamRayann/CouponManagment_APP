package com.example.coupon_managment_app;
import android.content.Context;
import java.util.ArrayList;

public class AdminFacade extends ClientFacade {

    public AdminFacade(Context context) {
        super(context);
    }


    // login function that Check if the provided email and password match the admin credentials.
    public boolean login(String email, String password) {
        if (email.equals("admin@admin.com") && password.equals("admin")) {
            return true;
        }
        return false;
    }


    // Add a new company to the system.
    public void addCompany(Company company) throws CompanyException {
        companiesDAO.addCompany(company);
    }


    //Update an existing company in the system.
    public void updateCompany(Company company) throws CompanyException {
        companiesDAO.updateCompany(company);
    }


    // a function to Delete company with the given id!
    public void deleteCompany(int companyId) throws CouponException, CompanyException, CustomerException {
        Company company= companiesDAO.getOneCompany(companyId);
        // clearing history of coupons purchased by customers from this company
        ArrayList<Coupon> coupons = null;
        try {
            coupons = couponsDAO.getAllCoupons();
        } catch (CouponException e) {
            throw e;
        }
        for (Coupon coupon : coupons) {
            if (coupon.getCompanyId() == companyId)
            {
                try {
                    for (Customer customer : customersDAO.getAllCustomers()) {
                        //remove coupon from customer's list of coupons
                        couponsDAO.deleteCouponPurchase(customer.getId(), coupon.getId());
                        // for each customer, remove the coupon from his list of coupons
                        if (customer.getCoupons() !=null) {
                            for (Coupon coupon1 : customer.getCoupons()) {
                                if (coupon1.getId() == coupon.getId()) {
                                    customer.getCoupons().remove(coupon1);
                                }
                            }
                        }
                    }
                } catch (CustomerException e) {
                    throw e;
                }
                //delete coupon of deleted company
                couponsDAO.deleteCoupon(coupon.getId());
            }
        }
        //delete company
        companiesDAO.deleteCompany(company);
    }



    // Get a list of all companies in the system.
    public ArrayList<Company> getAllCompanies() throws CompanyException {
        return companiesDAO.getAllCompanies();
    }



    // Get a single company by its unique CompanyID.
    public Company getOneCompany(int companyId) throws CompanyException {
        return companiesDAO.getOneCompany(companyId);
    }


    // adding one customer!
    public void addCustomer(Customer customer) throws CustomerException {
        try {
            customersDAO.addCustomer(customer);
        } catch (CustomerException e) {
            throw e;
        }
    }


    // updating a given customer in the database
    public void updateCustomer(Customer customer) throws CustomerException {
        try {
            customersDAO.updateCustomer(customer);
        } catch (CustomerException e) {
            throw e;
        }
    }



    // deleting a customer with the given id !
    public void deleteCustomer(int customerId) throws CouponException, CustomerException {
        Customer customer = null;
        try {
            customer = customersDAO.getOneCustomer(customerId);
        } catch (CustomerException e) {
            throw e;
        }
        // clearing history of coupons purchased by this customer
        if (customer.getCoupons()!=null){
            for(Coupon coupon : customer.getCoupons()) {
                couponsDAO.deleteCouponPurchase(customerId, coupon.getId());
            }}
        //delete customer
        try {
            customersDAO.deleteCustomer(customer);
        } catch (CustomerException e) {
            throw e;
        }
    }


    // getting all customers in the system!
    public ArrayList<Customer> getAllCustomers() throws CustomerException {
        return customersDAO.getAllCustomers();
    }


    // getting one customer by its id !
    public Customer getOneCustomer(int customerId) throws CustomerException {
        return customersDAO.getOneCustomer(customerId);
    }


}
