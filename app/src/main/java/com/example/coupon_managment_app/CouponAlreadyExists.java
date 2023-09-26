package com.example.coupon_managment_app;

public class CouponAlreadyExists extends Exception {
    public CouponAlreadyExists() {
        super("You Have Already Purchased A Coupon From This Category!");
    }
}
