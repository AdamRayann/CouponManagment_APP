package com.example.coupon_managment_app;

public class InSufficientBalance extends Exception{
    public InSufficientBalance() {
        super("Not Enough Funds In Your Account Balance!");
    }
}
