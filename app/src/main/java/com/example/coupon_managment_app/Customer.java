package com.example.coupon_managment_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
   private ArrayList<Coupon> coupons;

  
    public Customer( String first_Name, String last_Name, String email, String password) {
        firstName = first_Name;
        lastName = last_Name;
        this.email = email;
        this.coupons = new ArrayList<>(); // Initialize the coupons ArrayList
        this.password = password;
    }

    public Customer( int id ,String first_Name, String last_Name, String email, String password) {
        firstName = first_Name;
        lastName = last_Name;
        this.email = email;
        this.password = password;
        this.coupons = new ArrayList<>(); // Initialize the coupons ArrayList
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

}
