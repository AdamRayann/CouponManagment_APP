package com.example.coupon_managment_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Company implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
   private ArrayList<Coupon> coupons;
  

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons=null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void addCoupon(Coupon coupon){
        if(coupon == null)
            return;
        coupons.add(coupon);
    }

}

