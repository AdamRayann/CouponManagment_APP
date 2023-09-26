package com.example.coupon_managment_app;

import java.util.ArrayList;

public interface CouponsDAO {

    // a function Add a new coupon to the list and to the coupons table
    void addCoupon(Coupon coupon) throws CouponException;

    // a function to Update an existing coupon in the list and in the coupons table
    void updateCoupon(Coupon coupon) throws CouponException;

    // a function to Delete a coupon by its ID
    void deleteCoupon(int couponId) throws CouponException;

    // return a list of all coupons in our table
    ArrayList<Coupon> getAllCoupons() throws CouponException;

    // Retrieve a specific coupon by its ID
    Coupon getOneCoupon(int couponId) throws CouponException;

    // Add a new coupon purchase (to the customers vs coupons table)
    void addCouponPurchase(int customerId, int couponId) throws CouponException;

    // Delete a coupon purchase by its ID ( from the customers vs coupons table)
    void deleteCouponPurchase(int customerId, int couponId) throws CouponException;

    // function we added to get all the coupons of the given customer
    ArrayList<Coupon> getCustomerCoupons(Customer customer) throws CouponException;

}
