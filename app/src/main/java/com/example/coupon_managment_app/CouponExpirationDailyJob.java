package com.example.coupon_managment_app;

import android.content.Context;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CouponExpirationDailyJob implements java.lang.Runnable {
    private boolean isRunning = true; // Flag to control job execution
    private Context context;
    Db_Manager db_manager;
    private CouponsDBDAO couponsDBDAO; // Declare the DAO here

    public CouponExpirationDailyJob(Context context) {
        this.context = context;

    }

    @Override
    public void run() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return;
        }
        while (isRunning) {
            try {
                performCleanupAction();
            } catch (CouponException e) {
                throw new RuntimeException(e);
            }
            stopJob();
            try {
                Thread.sleep(60 * 60 * 1000); // Sleep for 1 hour
                isRunning = true;
            } catch (InterruptedException e) {
                // Handle interruption (e.g., when stopping the job)
                e.printStackTrace();
            }
        }
    }

    private void performCleanupAction() throws CouponException {
        db_manager=Db_Manager.getInstance(context);
        couponsDBDAO = CouponsDBDAO.getInstance(context); // Initialize the DAO here
        ArrayList<Coupon> coupons = couponsDBDAO.getAllCoupons();
        for (Coupon c : coupons) {
            if (checkCouponInvalidation(c)) {
                couponsDBDAO.deleteCoupon(c.getId());
            }
        }
    }

    private boolean checkCouponInvalidation(Coupon c) {
        Date currentDate = Calendar.getInstance().getTime();
        return c.getEndDate().before(currentDate);
    }

    public void stopJob() {
        // Method to stop the job gracefully
        isRunning = false;
    }
}
