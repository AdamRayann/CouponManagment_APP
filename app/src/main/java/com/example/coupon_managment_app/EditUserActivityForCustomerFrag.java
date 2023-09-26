package com.example.coupon_managment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserActivityForCustomerFrag extends AppCompatActivity {

    private ImageButton btnSave, btnCancel;
    private TextView tvId;
    private EditText etfName, etlName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_for_customer_fragment);

        btnSave = findViewById(R.id.userEdit_btnSave);
        btnCancel = findViewById(R.id.userEdit_btnCancel);
        tvId = findViewById(R.id.userEdit_tvId);
        etfName = findViewById(R.id.userEdit_etfName);
        etlName = findViewById(R.id.userEdit_etlName);
        etEmail = findViewById(R.id.userEdit_etUserName);
        etPassword = findViewById(R.id.userEdit_etPassword);

        Intent intent = getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("customer");
        if(customer == null) // nothing to be done!
            return;
        String customerID = customer.getId() + "";
        tvId.setText(customerID);
        etfName.setText(customer.getFirstName());
        etlName.setText(customer.getLastName());
        etEmail.setText(customer.getEmail());
        etPassword.setText(customer.getPassword());

        String oldPassword = etPassword.getText().toString();

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);
    }

    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String newPassword = etPassword.getText().toString();

            if(v.getId() == btnSave.getId()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserActivityForCustomerFrag.this);
                builder.setTitle("Update Confirmation");
                builder.setMessage("Are you sure you want to save your changes ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etfName.getText().toString().isEmpty() || etlName.getText().toString().isEmpty() ||
                                etEmail.getText().toString().isEmpty() || newPassword.isEmpty()) {
                            if (etfName.getText().toString().isEmpty()) {
                                etfName.setError("Enter your first name");
                            }
                            if (etlName.getText().toString().isEmpty()) {
                                etlName.setError("Enter your last name");
                            }
                            if (etEmail.getText().toString().isEmpty()) {
                                etEmail.setError("Enter your Email");
                            }
                            if (etPassword.getText().toString().isEmpty()) {
                                etPassword.setError("Enter your Password");
                            }
                            return;
                        }
                        if (!isValidEmail(etEmail.getText().toString())) {
                            etEmail.setError("Enter valid Email");
                            return;
                        }
                        // check if the email doesn't exist
                        AdminFacade admin = new AdminFacade(EditUserActivityForCustomerFrag.this);
                        Customer newcust = null;
                        Company newcompany = null;
                        admin = (AdminFacade) LoginManager.getInstance(EditUserActivityForCustomerFrag.this).login("admin@admin.com", "admin", ClientType.Administrator);
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
                            if (newcust.getId() != Integer.parseInt(tvId.getText().toString())) {
                                etEmail.setError("Email already exists");
                                return;
                            }
                        }
                        if (newcompany != null) {
                            etEmail.setError("Email already exists");
                            return;
                        }

                        Intent intent = getIntent();
                        Customer customer = new Customer( //first,last,email,pass
                                Integer.parseInt(tvId.getText().toString()),
                                etfName.getText().toString(),
                                etlName.getText().toString(),
                                etEmail.getText().toString(),
                                newPassword);

                        intent.putExtra("customer", customer);
                        intent.putExtra("requestCode", 2);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserActivityForCustomerFrag.this);
                builder.setTitle("Cancel Confirmation");
                builder.setMessage("Are you sure you want to Cancel your current Editing process ?");

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