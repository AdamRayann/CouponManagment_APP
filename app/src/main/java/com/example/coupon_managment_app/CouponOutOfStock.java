package com.example.coupon_managment_app;

public class CouponOutOfStock extends Exception{
    public CouponOutOfStock() {
        super("All Coupons From This Category Has Been Purchased!");
    }
}
