package com.example.coupon_managment_app;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewCompanyActivityForCompanyFrag extends AppCompatActivity {

    private ImageButton btnSave, btnCancel;
    private EditText etName , etCompEmail, etCompPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_company_for_company_fragment);

        btnSave = findViewById(R.id.newComp_btnSave);
        btnCancel= findViewById(R.id.newComp_btnCancel);

        etName = findViewById(R.id.newComp_etName);
        etCompEmail = findViewById(R.id.newComp_etEmail);
        etCompPassword = findViewById(R.id.newComp_etPassword);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);

    }

    class ButtonsClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.getId() == btnSave.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewCompanyActivityForCompanyFrag.this);
                builder.setTitle("Save Confirmation");
                builder.setMessage("Are you sure you want to save the new company ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (etName.getText().toString().isEmpty() || etCompEmail.getText().toString().isEmpty() || etCompPassword.getText().toString().isEmpty()) {
                                    if (etName.getText().toString().isEmpty()) {
                                        etName.setError("Enter your company name");
                                    }
                                    if (etCompEmail.getText().toString().isEmpty()) {
                                        etCompEmail.setError("Enter company Email");
                                    }
                                    if (etCompPassword.getText().toString().isEmpty()) {
                                        etCompPassword.setError("Enter company Password");
                                    }
                                    return;
                                }
                                 if(!isValidEmail(etCompEmail.getText().toString())){
                    etCompEmail.setError("Enter valid Email");
                    return;
                }
                // check if email already exits
                AdminFacade admin=new AdminFacade(NewCompanyActivityForCompanyFrag.this);
                Customer newcust=null;
                Company newcompany=null;
                admin= (AdminFacade) LoginManager.getInstance(NewCompanyActivityForCompanyFrag.this).login("admin@admin.com","admin",ClientType.Administrator);
                try {
                    newcust=admin.customersDAO.getCustomerByEmail(etCompEmail.getText().toString());
                } catch (CustomerException e) {
                    throw new RuntimeException(e);
                }
                try {
                    newcompany=admin.companiesDAO.getCompanyByEmail(etCompEmail.getText().toString());
                } catch (CompanyException e) {
                    throw new RuntimeException(e);
                }
                if(newcust!=null)
                {
                    etCompEmail.setError("Email already exists");
                    return;
                }
                if(newcompany!=null)
                {
                    etCompEmail.setError("Email already exists");
                    return;
                }
                // check if company name already exits
               if(admin.companiesDAO.isCompanyNameExists(etName.getText().toString()))
               {
                   etName.setError("Company name already exists");
                   return;
               }
                Intent intent = getIntent();
                Company company = new Company( // name,email,pass
                        etName.getText().toString(),
                        etCompEmail.getText().toString(),
                        etCompPassword.getText().toString());

                intent.putExtra("company",company);
                intent.putExtra("requestCode", 3);
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
            if(v.getId() == btnCancel.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewCompanyActivityForCompanyFrag.this);
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