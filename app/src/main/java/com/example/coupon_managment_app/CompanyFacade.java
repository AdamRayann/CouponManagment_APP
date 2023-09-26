package com.example.coupon_managment_app;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyFacade extends ClientFacade implements Serializable {
    private Company loggedInCompany;

    public CompanyFacade(Context context) {
        super(context);
    }

    @Override
    public boolean login(String email,String password) {

        boolean loginSuccessful = companiesDAO.isCompanyExists(email, password);
        if (loginSuccessful) {
            // If login is successful, retrieve and store the company details
            try {
                loggedInCompany = companiesDAO.getCompanyByEmail(email);
            } catch (CompanyException e) {
                String errorMessage = e.getMessage();
                System.out.println("Error: " + errorMessage);
            }
        }
        return loginSuccessful;
    }

    public void addCoupon(Coupon coupon) throws CouponException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        coupons= getCompanyCoupons();
        for (Coupon c : coupons) {
            if(c.getTitle().toString().equals(coupon.getTitle().toString()))
            {
                Toast.makeText(context, "Coupon title is already exists", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        couponsDAO.addCoupon(coupon);

    }
    public void updateCoupon(Coupon coupon) throws CouponException {
        couponsDAO.updateCoupon(coupon);

    }

    public void deleteCoupon(int couponId) throws CouponException {
        couponsDAO.deleteCouponPurchase(loggedInCompany.getId(),couponId);
        couponsDAO.deleteCoupon(couponId);

    }
    public ArrayList<Coupon> getCompanyCoupons() throws CouponException {
        ArrayList<Coupon> companyCoupons = new ArrayList<>();

        for (Coupon c : couponsDAO.getAllCoupons()) {
            if (c.getCompanyId() == loggedInCompany.getId())
            {
                companyCoupons.add(c);
            }
        }
        return companyCoupons;
    }
    public ArrayList<Coupon> getCompanyCoupons(Category category) throws CouponException {
        ArrayList<Coupon> companyCoupons = new ArrayList<>();

        for (Coupon c:couponsDAO.getAllCoupons()) {
            if (c.getCompanyId() == loggedInCompany.getId() && c.getCategory() == category)
            {
                companyCoupons.add(c);
            }

        }
        return companyCoupons;
    }
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws CouponException {
        ArrayList<Coupon> companyCoupons = new ArrayList<>();

        for (Coupon c:couponsDAO.getAllCoupons()) {
            if (c.getCompanyId() == loggedInCompany.getId() && c.getPrice() <= maxPrice)
            {
                companyCoupons.add(c);
            }

        }
        return companyCoupons;
    }
    public Company getCompanyDetails()
    {
        return loggedInCompany;
    }



}
