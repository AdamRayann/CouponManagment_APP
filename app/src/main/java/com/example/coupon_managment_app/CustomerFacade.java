package com.example.coupon_managment_app;
import android.content.Context;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CustomerFacade extends ClientFacade implements Serializable {

    // this parameter is used to save the id of the customer who logged in succefully
    private int customerID;

    public CustomerFacade(Context context) {
        super(context);
    }

    public boolean login(String email, String password) {
        // first we check if the customer exists
        boolean customerExists = customersDAO.isCustomerExists(email, password);
        if (customerExists) {
            Customer customer = null;
            try {
                customer = customersDAO.getCustomerByEmail(email);
            } catch (CustomerException e) {
                // add soemthing
            }
            if (customer != null) {
                customerID = customer.getId();
            }
            return true; // Return true if login is successful
        } else {
            return false;
        }
    }




    // this function returns true if the purchase is done correctly, else it returns false of throw an exception
    public boolean purchaseCoupon(Coupon coupon) throws CustomerException, CouponException {
        // Check if the coupon is already in the customer's list of coupons
        Customer customer = null;
        try {
            customer = customersDAO.getOneCustomer(customerID);
        } catch (CustomerException e) {
            throw e;
        }
        // this line might throw an exception but we will handle it when we call this function ;)
        ArrayList<Coupon> customerCoupons = getCustomerCoupons();
        if(customerCoupons!=null) {
            for (Coupon customerCoupon : customerCoupons) {
                if (customerCoupon.getId() == coupon.getId()) {
                    // The customer has already purchased this coupon
                    // You can choose to throw an exception or handle it as needed
                    throw new CouponException("cant buy the same coupon twice!");
                }
            }
        }

        // Check if the coupons quantity is greater than zero
        if (coupon.getAmount() <= 0) {
            return false;
        }
        // Check if the coupon's expiration date has not passed
        Date currentDate = new Date();
        if (coupon.getEndDate().before(currentDate)) {
            return false;
        }
        // Reduce the quantity of the coupon by 1
        coupon.setAmount(coupon.getAmount() - 1);
        // Add the purchased coupon to the customer's list of coupons
        customerCoupons.add(coupon);
        customer.setCoupons(customerCoupons);
        // Update the customer and coupon in the database
        customersDAO.updateCustomer(customer);
        try {
            couponsDAO.updateCoupon(coupon);
            couponsDAO.addCouponPurchase(customerID, coupon.getId());
        }
        catch (Exception e){
            throw e;
        }
        return true; // Purchase successful
    }




    // Function to return all coupons purchased by the customer
    public ArrayList<Coupon> getCustomerCoupons() throws CustomerException, CouponException {
        // Retrieve the customer's coupons from the database using the customer ID
        Customer customer = null;
        try {
            customer = customersDAO.getOneCustomer(customerID);
        } catch (CustomerException e) {
            throw e;
        }
        ArrayList<Coupon> allCoupons=couponsDAO.getCustomerCoupons(customer);
        // Create a list to store the customer's coupons
        ArrayList<Coupon> customerCoupons = new ArrayList<>();
        // Loop through all coupons and add those that belong to the current customer
        for (Coupon coupon : allCoupons) {
            customerCoupons.add(coupon);

        }
        return customerCoupons;
    }





    // Function to return all coupons from a specific category purchased by the customer
    public ArrayList<Coupon> getCustomerCoupons(Category category) throws CustomerException, CouponException {
        // get the customer's coupons from the database using the customer ID
        Customer customer = null;
        try {
            customer = customersDAO.getOneCustomer(customerID);
        } catch (CustomerException e) {
            throw e;
        }
        if (customer == null) {
            return null; // Customer not found, return null or handle accordingly
        }
        // Get the list of coupons who belong to the customer with the customer
        ArrayList<Coupon> customerCoupons = couponsDAO.getCustomerCoupons(customer);

        // Create a new list to store coupons that match the choosed category
        ArrayList<Coupon> couponsInCategory = new ArrayList<>();
        // loop through the customer's coupons and add those in the choosed category
        for (Coupon coupon : customerCoupons) {
            if (coupon.getCategory() == category) {
                couponsInCategory.add(coupon);
            }
        }
        return couponsInCategory;
    }



    // Function to return all coupons up to the maximum price purchased by the customer
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CustomerException, CouponException {
        // Get the customer's coupons from the database using the customer ID
        Customer customer = customersDAO.getOneCustomer(customerID);
        if (customer == null) {
            return null; // Customer not found, return null or throw exception
        }
        ArrayList<Coupon> customerCoupons = couponsDAO.getCustomerCoupons(customer);
        ArrayList<Coupon> couponsWithinPrice = new ArrayList<>();
        for (Coupon coupon : customerCoupons) {
            if (coupon.getPrice() <= maxPrice) {
                couponsWithinPrice.add(coupon);
            }
        }

        return couponsWithinPrice;
    }



    // Function to return customer details
    public Customer getCustomerDetails() throws CustomerException {
        return customersDAO.getOneCustomer(customerID);
    }
}
