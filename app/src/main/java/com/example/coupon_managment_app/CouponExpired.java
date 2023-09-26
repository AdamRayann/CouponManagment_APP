package com.example.coupon_managment_app;

public class CouponExpired extends Exception{
    public CouponExpired() {
        super("Coupon Date Already Expired!");
    }
}
