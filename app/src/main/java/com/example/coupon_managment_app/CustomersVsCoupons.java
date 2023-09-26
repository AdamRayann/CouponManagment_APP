package com.example.coupon_managment_app;

import java.io.Serializable;

public class CustomersVsCoupons implements Serializable {
    private int customerId;
    private int couponId;

    public CustomersVsCoupons(int customerId, int couponId) {
        this.customerId = customerId;
        this.couponId = couponId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
