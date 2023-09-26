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
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCompanyActivityForCompanyFrag extends AppCompatActivity {

    private ImageButton btnSave, btnCancel;
    private EditText  etEmail, etPassword;
    private TextView tvID, tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_for_company_fragment);


        btnSave = findViewById(R.id.compEdit_btnSave);
        btnCancel = findViewById(R.id.compEdit_btnCancel);
        tvName = findViewById(R.id.compEdit_tvName);
        tvID = findViewById(R.id.compEdit_tvNumber);
        etEmail = findViewById(R.id.compEdit_etEmail);
        etPassword = findViewById(R.id.compEdit_etPassword);

        Intent intent = getIntent();
        Company company = (Company) intent.getSerializableExtra("company");
        if(company == null) //nothing to be done
            return;
        String compID = company.getId() + "";
        tvID.setText(compID);
        String compName = company.getName();
        tvName.setText(compName);
        etEmail.setText(company.getEmail());
        etPassword.setText(company.getPassword());


        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);

    }

    class ButtonsClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            String newPassword = etPassword.getText().toString();

            if(v.getId() == btnSave.getId()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCompanyActivityForCompanyFrag.this);
                builder.setTitle("Update Confirmation");
                builder.setMessage("Are you sure you want save your changes ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (etEmail.getText().toString().isEmpty() || newPassword.isEmpty()) {
                                    if (etEmail.getText().toString().isEmpty()) {
                                        etEmail.setError("Enter company Email");
                                    }
                                    if (newPassword.isEmpty()) {
                                        etPassword.setError("Enter company Password");
                                    }
                                    return;
                                }
                                if (!isValidEmail(etEmail.getText().toString())) {
                                    etEmail.setError("Enter valid Email");
                                    return;
                                }


                                // check if email already exits
                                AdminFacade admin = new AdminFacade(EditCompanyActivityForCompanyFrag.this);
                                Customer newcust = null;
                                Company newcompany = null;
                                admin = (AdminFacade) LoginManager.getInstance(EditCompanyActivityForCompanyFrag.this).login("admin@admin.com", "admin", ClientType.Administrator);
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
                                    if (newcompany.getId() != parseInt(tvID.getText().toString())) {
                                        etEmail.setError("Email already exists");
                                        return;
                                    }
                                }


                                Intent intent = getIntent();
                                Company company = new Company(// Name,email,pass // name shouldn't be changed??
                                        tvName.getText().toString(),
                                        etEmail.getText().toString(),
                                        newPassword);

                                intent.putExtra("company", company);
                                intent.putExtra("requestCode", 4);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCompanyActivityForCompanyFrag.this);
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