package com.example.coupon_managment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {

    private ConstraintLayout fragContainer;
    private AppCompatButton customerBtnExit;
    private ImageButton btnMyCoupons, btnBuyACoupon;
    private TextView textEmail,textNameCustomer;
    private MyCouponsFragment myCouponsFragment = new MyCouponsFragment();
    private BuyACouponFragment buyACouponFragment = new BuyACouponFragment();
    private Fragment currentFragment = null;
    private CustomerFacade customerFacade;

    int Silver= Color.parseColor("#C0C0C0");
    int Blue=Color.parseColor("#21BFFA");
    int ClickedBlue=Color.parseColor("#17A7DC");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        fragContainer = findViewById(R.id.customerActivity_FragContainer);
        btnMyCoupons = findViewById(R.id.customerActivity_btnMyCoupons);
        btnBuyACoupon = findViewById(R.id.customerActivity_btnBuyCoupon);
        customerBtnExit = findViewById(R.id.CustomerActivity_BtnExit);

//      getting the customer that logged in
        Intent intent = getIntent();
        Customer customer= (Customer) intent.getSerializableExtra("Customer");
        customerFacade = (CustomerFacade) LoginManager.getInstance(this).login(customer.getEmail(),customer.getPassword(),ClientType.Customer);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnMyCoupons.setOnClickListener(buttonsClick);
        btnBuyACoupon.setOnClickListener(buttonsClick);
        customerBtnExit.setOnClickListener(buttonsClick);


    }

    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == btnMyCoupons.getId()) {
                customerBtnExit.setText("Close");
                btnMyCoupons.setBackgroundColor(ClickedBlue);
                btnBuyACoupon.setBackgroundColor(Blue);
                if(buyACouponFragment != null)
                {
                    buyACouponFragment.setLvBuyACouponSelectedRow(-1);
                }
                myCouponsFragment.setFacadetwo(customerFacade);
                myCouponsFragment.setContext(CustomerActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.customerActivity_FragContainer, myCouponsFragment).commit();
                currentFragment = myCouponsFragment;
            }
            if (v.getId() == btnBuyACoupon.getId()) {
                customerBtnExit.setText("Close");
                btnMyCoupons.setBackgroundColor(Blue);
                btnBuyACoupon.setBackgroundColor(ClickedBlue);
                if(myCouponsFragment != null)
                {
                    myCouponsFragment.setLvMyCouponsSelectedRow(-1);
                }
                buyACouponFragment.setFacade(customerFacade);
                buyACouponFragment.setContext(CustomerActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.customerActivity_FragContainer, buyACouponFragment).commit();
                currentFragment = buyACouponFragment;
            }
            if (v.getId() == customerBtnExit.getId()) {
                btnMyCoupons.setBackgroundColor(Blue);
                btnBuyACoupon.setBackgroundColor(Blue);
                if (!("Close".equals(customerBtnExit.getText()))) {
                    // Create an AlertDialog to confirm the exit
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
                    builder.setTitle("Exit Confirmation");
                    builder.setMessage("Are you sure you want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btnMyCoupons.setBackgroundColor(Blue);
                            btnBuyACoupon.setBackgroundColor(Blue);
                            if (currentFragment == null) {
                                customerBtnExit.setText("Exit");
                                finish();
                            }
                            if (currentFragment != null) {
                                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                                currentFragment = null;
                                customerBtnExit.setText("Exit");
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked "No," so do nothing
                            dialog.dismiss(); // Dismiss the dialog
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    if (currentFragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                        currentFragment = null;
                        customerBtnExit.setText("Exit");
                    }
                }
            }
        }
    }
}