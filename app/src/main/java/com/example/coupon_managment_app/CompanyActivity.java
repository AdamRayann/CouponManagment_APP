package com.example.coupon_managment_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class CompanyActivity extends AppCompatActivity {
    private ImageButton btnNew, btnEdit, btnDelete;
    private Button btnExit;
    private EditText etMaxPrice;
    private ListView couponsListView;
    private Spinner categorySpinner;
    private Company company;
    private ArrayList<Coupon> filtered;

    /************************* ListView Adapter related Variables *********************************/
    private ArrayList<Coupon> couponsArrayList = new ArrayList<>();
    private CouponsLVAdapter couponAdapter;
    private CompanyFacade companyFacade;
    /***************************~ ListView Selected line Management  ~*****************************/
    private int lvSelectedRow = -1;
    private ActivityResultLauncher<Intent> couponActivityLauncher;
    private ConstraintLayout bgLayout;
    private int bgLineColor;
    /**********************************************************************************************/

    public void setLvCouponsSelectedRow(int selectedRow) {
        this.lvSelectedRow = selectedRow;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //couponAdapter=null;
        setContentView(R.layout.activity_company);
        btnNew = findViewById(R.id.compActivity_btnAdd);
        btnEdit = findViewById(R.id.compActivity__btnEdit);
        btnDelete = findViewById(R.id.compActivity_btnDelete);
        etMaxPrice = findViewById(R.id.compActivity_etmaxPrice);
        categorySpinner = findViewById(R.id.compActivity_spCatogries);
        couponsListView = findViewById(R.id.compActivity_lvCoupons);
        btnExit = findViewById(R.id.companyActivity_btnExit);
        /** fill spinner with catogories**/
        Category[] categories = Category.values();
        String[] categoryNames = new String[categories.length + 1];
        categoryNames[0] = "All";
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i + 1] = categories[i].toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        /** Fill Companies ArrayList **/
        Intent intent = getIntent();
        company = (Company) intent.getSerializableExtra("Company");
        companyFacade = (CompanyFacade) LoginManager.getInstance(this).login(company.getEmail(),company.getPassword(),ClientType.Company);
        /***set list adapter*****/
        try {
            couponsArrayList=companyFacade.getCompanyCoupons();
        } catch (CouponException e) {
            Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

            couponAdapter = new CouponsLVAdapter(CompanyActivity.this, R.layout.companies_coupon_line,couponsArrayList);

        couponsListView.setAdapter(couponAdapter);
        /***************************~ Buttons Click Listener  ~************************************/
        ButtonsClick buttonsClick = new ButtonsClick();
        btnNew.setOnClickListener(buttonsClick);
        btnEdit.setOnClickListener(buttonsClick);
        btnDelete.setOnClickListener(buttonsClick);
        btnExit.setOnClickListener(buttonsClick);

        couponActivityLauncher = startCouponActivityLauncher();
        /******************************************************************************************/
        /** spinner listener **/
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = categoryNames[position];
                try {
                // Handle the selected option here
                switch (selectedOption) {
                    case "All":
                        // show all coupons
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(companyFacade.getCompanyCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                          filtered=companyFacade.getCompanyCoupons();
                        }
                        couponAdapter.refreshAllCoupons(filtered);
                        break;
                    case "Food":
                        // filter by food
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(companyFacade.getCompanyCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=companyFacade.getCompanyCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Food);
                        couponAdapter.refreshAllCoupons(filtered);
                        break;
                    case "Electricity":
                        // filter by electricity
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(companyFacade.getCompanyCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=companyFacade.getCompanyCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Electricity);
                        couponAdapter.refreshAllCoupons(filtered);
                        break;
                    case "Restaurant":
                        // filter by restaurant
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(companyFacade.getCompanyCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=companyFacade.getCompanyCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Restaurant);
                        couponAdapter.refreshAllCoupons(filtered);
                    case "Vacation":
                        // filter by vacation
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(companyFacade.getCompanyCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=companyFacade.getCompanyCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Vacation);
                        couponAdapter.refreshAllCoupons(filtered);
                        break;
                    default:
                        // Handle unexpected category or add more cases as needed
                        throw new IllegalStateException("Unexpected value: " + selectedOption);
                }
                }catch (CouponException e){
                    Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    couponAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                } catch (CouponException e) {
                    Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** filter to max price edit text listener **/
        // Attach a TextWatcher to etMaxPrice
        etMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Get the user's input from etMaxPrice
                String userInput = editable.toString();

                // Check if the user input is not empty
                if (!userInput.isEmpty()) {
                    try {
                        // Parse the user input as a double
                        double maxPrice = Double.parseDouble(userInput);

                        // Filter coupons based on the maxPrice and the selected category
                        if(categorySpinner.getSelectedItem().toString().equals("All"))
                        {
                            filtered=filterByMaxPrice(companyFacade.getCompanyCoupons(),maxPrice);
                        }
                        else
                        {
                            filtered= filterByCategory(companyFacade.getCompanyCoupons(),Category.valueOf(categorySpinner.getSelectedItem().toString()));
                            filtered=filterByMaxPrice(filtered,maxPrice);
                        }
                        // Update the ListView with the filtered coupons
                        couponAdapter.clear();
                        couponAdapter.addAll(filtered);
                        couponAdapter.notifyDataSetChanged();
                    } catch (NumberFormatException e) {
                        Toast.makeText(CompanyActivity.this, "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    } catch (CouponException e) {

                        Toast.makeText(CompanyActivity.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    ArrayList<Coupon> filteredCoupons = new ArrayList<>();
                    try {
                        // If the user input is empty, show all coupons based on the selected category
                        String selectedCategory = categorySpinner.getSelectedItem().toString();
                        if (selectedCategory.equals("All")) {
                            filteredCoupons = companyFacade.getCompanyCoupons();
                        } else {
                            filteredCoupons = companyFacade.getCompanyCoupons(Category.valueOf(selectedCategory));
                        }
                    }   catch (CouponException e) {
                        Toast.makeText(CompanyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // Update the ListView with the filtered coupons
                    couponAdapter.clear();
                    couponAdapter.addAll(filteredCoupons);
                    couponAdapter.notifyDataSetChanged();
                }
            }
        });

        /***************************~ Set BG Color To Clicked Line  ~******************************/
        couponsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lvSelectedRow != -1)
                {
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                lvSelectedRow = position;
                couponsListView.setSelection(lvSelectedRow);
                bgLayout = (ConstraintLayout) view.findViewById(R.id.coupons_lvLine_constraintLayout);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(100, 100, 100));
            }
        });
    }
    // buttons listener
    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == btnNew.getId()) {
                Intent intent = new Intent(CompanyActivity.this, NewCouponForCompanyActivity.class);
                intent.putExtra("companyId", company.getId());
                try {
                    couponActivityLauncher.launch(intent);
                } catch (Exception ex) {
                    Toast.makeText(CompanyActivity.this, "NEW COUPON Activity Launcher Failed!", Toast.LENGTH_LONG).show();
                }
            }
            if(v.getId() == btnEdit.getId()) {
                if(lvSelectedRow < 0)
                {
                    Toast.makeText(CompanyActivity.this, "Please Select A Coupon To Edit!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(couponAdapter == null || couponAdapter.getCount() == 0) { // adapter is null
                    Toast.makeText(CompanyActivity.this, "Coupons List is Empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(couponAdapter!=null) {
                    if (lvSelectedRow >= 0 && lvSelectedRow < couponAdapter.getCount()) {
                        Intent intent = new Intent(CompanyActivity.this, EditCouponForCompanyActivity.class);
                        intent.putExtra("companyId", company.getId());
                        intent.putExtra("coupon", couponAdapter.getItem(lvSelectedRow));
                        try {
                            couponActivityLauncher.launch(intent);
                        } catch (Exception ex) {
                            Toast.makeText(CompanyActivity.this, "EDIT COUPON Activity Launcher Failed!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CompanyActivity.this, "Please Select A Coupon To Edit!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            if(v.getId() == btnDelete.getId()) {
                if(lvSelectedRow < 0){
                    Toast.makeText(CompanyActivity.this, "Please Select A Coupon To Delete!", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
                    builder.setTitle("Delete Confirmation");
                    builder.setMessage("Are you sure you want to Delete the selected item?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (couponAdapter == null || couponAdapter.getCount() == 0) { // adapter is null
                                Toast.makeText(CompanyActivity.this, "Coupons List is Empty!", Toast.LENGTH_LONG).show();
                                return;
                            } else if (couponAdapter != null) {
                                if (lvSelectedRow >= 0 && lvSelectedRow < couponAdapter.getCount()) {
                                    try {
                                        companyFacade.deleteCoupon(couponAdapter.getItem(lvSelectedRow).getId());
                                        couponAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                                    } catch (CouponException ex) {
                                        Toast.makeText(CompanyActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(CompanyActivity.this, "Please Select A coupon To Delete!", Toast.LENGTH_LONG).show();
                                    return;
                                }
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
                }
            }
            if(v.getId() == btnExit.getId())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
                builder.setTitle("Exit Confirmation");
                builder.setMessage("Are you sure you want to Exist ?");

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

    private ActivityResultLauncher<Intent> startCouponActivityLauncher () {
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            int requestCode = intent.getIntExtra("requestCode", 0);
                            if (requestCode == 5) {   // new coupon
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Coupon coupon = (Coupon) intent.getSerializableExtra("coupon");
                                    if (coupon != null) {
                                        try {
                                            companyFacade.addCoupon(coupon);
                                            couponAdapter.refreshCouponAdded(coupon);
                                        } catch (Exception ex) {
                                            Toast.makeText(CompanyActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            } else if (requestCode == 6) { // Edit coupon
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Coupon coupon = (Coupon) intent.getSerializableExtra("coupon");
                                    if (coupon != null) {
                                        try {
                                            companyFacade.updateCoupon(coupon);
                                            couponAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                                        } catch (Exception ex) {
                                            Toast.makeText(CompanyActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(CompanyActivity.this, "requestCode not recognized", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
        return launcher;
    }

    public ArrayList<Coupon> filterByCategory(ArrayList<Coupon> all,Category category)
    {
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : all) {
            if (coupon.getCategory() == category) {
                filteredCoupons.add(coupon);
            }
        }
        return filteredCoupons;
    }

    public ArrayList<Coupon> filterByMaxPrice(ArrayList<Coupon> all, double maxPrice) {
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : all) {
            if (coupon.getPrice() <= maxPrice) {
                filteredCoupons.add(coupon);
            }
        }
        return filteredCoupons;
    }
}
