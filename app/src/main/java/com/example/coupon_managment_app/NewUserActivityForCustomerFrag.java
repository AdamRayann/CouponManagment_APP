package com.example.coupon_managment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewUserActivityForCustomerFrag extends AppCompatActivity {

    private ImageButton btnSave, btnCancel;
    private EditText  etFName, etLName, etEmail, etUserPassword;
    //TextInputEditText etUserPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_for_customer_fragment);
        btnSave = findViewById(R.id.newUser_btnSave);
        btnCancel = findViewById(R.id.newUser_btnCancel);
        etFName = findViewById(R.id.newUser_fName);
        etLName = findViewById(R.id.newUser_lName);
        etEmail = findViewById(R.id.newUser_etEmail);
        etUserPassword = findViewById(R.id.newUser_etPass);
        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);
    }

    class ButtonsClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.getId() == btnSave.getId())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewUserActivityForCustomerFrag.this);
                builder.setTitle("Savr Confirmation");
                builder.setMessage("Are you sure you want to add the new Customer ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (etFName.getText().toString().isEmpty() || etLName.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() ||
                                        etUserPassword.getText().toString().isEmpty()) {
                                    if (etFName.getText().toString().isEmpty()) {
                                        etFName.setError("Enter your first name");
                                    }
                                    if (etLName.getText().toString().isEmpty()) {
                                        etLName.setError("Enter your last name");
                                    }
                                    if (etEmail.getText().toString().isEmpty()) {
                                        etEmail.setError("Enter your Email");
                                    }
                                    if (etUserPassword.getText().toString().isEmpty()) {
                                        etUserPassword.setError("enter new password");
                                    }
                                    return;
                                }
                                if (!isValidEmail(etEmail.getText().toString())) {
                                    etEmail.setError("inset valid Email");
                                    return;
                                }
                                // check if the email doesn't exist
                                AdminFacade admin = new AdminFacade(NewUserActivityForCustomerFrag.this);
                                Customer newcust = null;
                                Company newcompany = null;
                                admin = (AdminFacade) LoginManager.getInstance(NewUserActivityForCustomerFrag.this).login("admin@admin.com", "admin", ClientType.Administrator);
                                try {
                                    newcust = admin.customersDAO.getCustomerByEmail(etEmail.getText().toString());
                                } catch (CustomerException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    newcompany = admin.companiesDAO.getCompanyByEmail(etEmail.getText().toString());
                                } catch (CompanyException e) {
                                    throw new RuntimeException(e);
                                }
                                if (newcust != null) {
                                    etEmail.setError("Email already exists");
                                    return;
                                }
                                if (newcompany != null) {
                                    etEmail.setError("Email already exists");
                                    return;
                                }
                                Intent intent = getIntent();
                                Customer customer = new Customer( ///first,last,email,pass
                                        etFName.getText().toString(),
                                        etLName.getText().toString(),
                                        etEmail.getText().toString(),
                                        etUserPassword.getText().toString());

                                intent.putExtra("customer", customer);
                                intent.putExtra("requestCode", 1);
                                setResult(RESULT_OK, intent);
                                finish();
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
            }
            if(v.getId() == btnCancel.getId())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewUserActivityForCustomerFrag.this);
                builder.setTitle("Cancel Confirmation");
                builder.setMessage("Are you sure you want to Cancel your current process ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
            }
        }
    }

    public static boolean isValidEmail(String email) {
        // Define a regex pattern to match a basic email format
        String emailRegex = "^.+@.+\\..+$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(emailRegex);

        // Match the email against the pattern
        Matcher matcher = pattern.matcher(email);

        // Return true if it matches the pattern, indicating a valid email format
        return matcher.matches();
    }
}