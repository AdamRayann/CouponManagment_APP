package com.example.coupon_managment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etUserName, etPassword;
    RadioGroup radioGroup;
    RadioButton rbAdmin, rbUser, rbCompany;
    Button btnLogin;
    ClientType clientType = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = (EditText) findViewById(R.id.main_etUserName);
        etPassword = (EditText) findViewById(R.id.main_etPassword);
        btnLogin = (Button) findViewById(R.id.main_btnLogin);
        textView=(TextView) findViewById(R.id.textViewWelcoming);
        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);






        LoginManager loginManager = LoginManager.getInstance(MainActivity.this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etUserName.getText().toString();
                String pass = etPassword.getText().toString();

                if(email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Fill All Fields!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isValidEmail(email)){
                    Toast.makeText(MainActivity.this, "Please Insert a Valid Email", Toast.LENGTH_LONG).show();
                    return;
                }
                /**
                 * Here we know login is valid, we grab the login object and its info
                 */
                ClientFacade clientFacade = loginManager.login(email, pass, clientType);//maybe make signIn throw Exceptions accordingly?

                if (clientFacade == null) {
                    Toast.makeText(MainActivity.this, "Error in UserName Or Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (clientFacade instanceof AdminFacade) {
                    OpenAdminActivity();
                }
                else if (clientFacade instanceof CustomerFacade) {

                    Customer customer = null;
                    try {
                        customer = ((CustomerFacade) clientFacade).getCustomerDetails();
                    } catch (CustomerException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    OpenCustomerActivity((Customer) customer);
                }
                else if (clientFacade instanceof CompanyFacade) {
                    Company company = ((CompanyFacade) clientFacade).getCompanyDetails();
                    OpenCompanyActivity((Company) company);

                }
            }
        });
        /******************************************************************************************/


    }

    /*****************************~ Open Activities Functions ~************************************/
    private void OpenAdminActivity() {
        Intent intent=new Intent( MainActivity.this, AdminMainActivity.class);
        startActivity(intent);

    }

    private void OpenCustomerActivity(Customer obj) {
        Intent intent = new Intent(this, CustomerActivity.class);
        intent.putExtra("Customer", obj);
        startActivity(intent);
    }

    private void OpenCompanyActivity(Company obj) {
        Intent intent = new Intent(this, CompanyActivity.class);
        intent.putExtra("Company", obj);
        startActivity(intent);
    }

    /*******************************~ Helping Functions ~******************************************/
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text=adapterView.getItemAtPosition(i).toString();
        if (text.equals("Admin")){
            textView.setText("Admin Login");
            clientType = ClientType.Administrator;

        } else if (text.equals("Customer")) {
            textView.setText("Customer Login");
            clientType = ClientType.Customer;

        }else if (text.equals("Company")) {
            textView.setText("Company Login");
            clientType = ClientType.Company;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "No User Type Selected", Toast.LENGTH_SHORT).show();

    }
}