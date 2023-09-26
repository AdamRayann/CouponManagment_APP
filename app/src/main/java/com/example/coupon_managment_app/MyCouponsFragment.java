package com.example.coupon_managment_app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class MyCouponsFragment extends Fragment {

    private EditText etMaxPrice;
    private ListView listViewMyCoupons;
    private Context context;
    private MyCouponsLVAdapter myCouponsAdapter;
    private ArrayList<Coupon> couponsArrayList = new ArrayList<>();
    private CustomerFacade customerFacadethree;
    private Spinner categorySpinner;
    private ArrayList<Coupon> filtered;

    /***************************~ ListView Selected line Management  ~*****************************/
    private int selectedRow = -1; // Initialize with -1, indicating no selection
    private ConstraintLayout bgLayout;
    private int bgLineColor;
    /**********************************************************************************************/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_coupons, container, false);

        // Initialize UI elements
        etMaxPrice = view.findViewById(R.id.myCouponFrag_etMaxPrice);
        listViewMyCoupons = view.findViewById(R.id.myCouponFrag__lvMyCoupons);
        categorySpinner = view.findViewById(R.id.myCouponFrag_sbCouponCategory);

        // Initialize the adapter with the coupon data
        myCouponsAdapter = new MyCouponsLVAdapter(context,R.layout.fragment_my_coupons_listview_line, couponsArrayList);
        listViewMyCoupons.setAdapter(myCouponsAdapter);
        try {
            couponsArrayList = customerFacadethree.getCustomerCoupons();

        } catch (CustomerException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (CouponException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        /** fill spinner with catogories**/
        Category[] categories = Category.values();
        String[] categoryNames = new String[categories.length + 1];
        categoryNames[0] = "All";
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i + 1] = categories[i].toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try{
            categorySpinner.setAdapter(adapter);
        }
        catch (Exception e){
            Toast.makeText(context, "Error in setting adapter!", Toast.LENGTH_SHORT).show();
        }

        /** spinner lisnter **/
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = categoryNames[position];
                // Handle the selected option here
                try {
                switch (selectedOption) {
                    case "All":
                        // show all coupons
                        if (etMaxPrice.getText().length() > 0) {
                            filtered = filterByMaxPrice(customerFacadethree.getCustomerCoupons(), Double.parseDouble(etMaxPrice.getText().toString()));
                        } else {
                            filtered = customerFacadethree.getCustomerCoupons();
                        }
                        myCouponsAdapter.refreshAllMyCoupons(filtered);
                        break;
                    case "Food":
                        // filter by food
                        if (etMaxPrice.getText().length() > 0)
                        {
                            filtered=filterByMaxPrice(customerFacadethree.getCustomerCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=customerFacadethree.getCustomerCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Food);
                        myCouponsAdapter.refreshAllMyCoupons(filtered);
                        break;
                    case "Electricity":
                        // filter by electricity
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(customerFacadethree.getCustomerCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=customerFacadethree.getCustomerCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Electricity);
                        myCouponsAdapter.refreshAllMyCoupons(filtered);
                        break;
                    case "Restaurant":
                        // filter by restaurant
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(customerFacadethree.getCustomerCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=customerFacadethree.getCustomerCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Restaurant);
                        myCouponsAdapter.refreshAllMyCoupons(filtered);
                        break;
                    case "Vacation":
                        // filter by vacation
                        if(etMaxPrice.getText().length()>0)
                        {
                            filtered=filterByMaxPrice(customerFacadethree.getCustomerCoupons(),Double.parseDouble(etMaxPrice.getText().toString()));
                        }
                        else
                        {
                            filtered=customerFacadethree.getCustomerCoupons();
                        }
                        filtered=filterByCategory(filtered,Category.Vacation);
                        myCouponsAdapter.refreshAllMyCoupons(filtered);
                        break;
                    default:
                        // Handle unexpected category or add more cases as needed
                        throw new IllegalStateException("Unexpected value: " + selectedOption);
                }
                }
                catch (CustomerException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (CouponException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                try {
                    myCouponsAdapter.refreshAllMyCoupons(customerFacadethree.getCustomerCoupons());
                } catch (CustomerException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (CouponException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            filtered= filterByMaxPrice( customerFacadethree.getCustomerCoupons(),maxPrice);
                        }
                        else
                        {
                            filtered= filterByCategory(customerFacadethree.getCustomerCoupons(),Category.valueOf(categorySpinner.getSelectedItem().toString()));
                            filtered=filterByMaxPrice(filtered,maxPrice);
                        }
                        // Update the ListView with the filtered coupons
                        myCouponsAdapter.clear();
                        myCouponsAdapter.addAll(filtered);
                        myCouponsAdapter.notifyDataSetChanged();
                    } catch (NumberFormatException e) {
                        Toast.makeText(context.getApplicationContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    } catch (CustomerException e) {

                        Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (CouponException e) {
                        Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ArrayList<Coupon> filteredCoupons = new ArrayList<>();
                    try {
                        // If the user input is empty, show all coupons based on the selected category
                        String selectedCategory = categorySpinner.getSelectedItem().toString();
                        if (selectedCategory.equals("All")) {
                            filteredCoupons = customerFacadethree.getCustomerCoupons();
                        } else {
                            filteredCoupons = customerFacadethree.getCustomerCoupons(Category.valueOf(selectedCategory));
                        }
                    }  catch (CustomerException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (CouponException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // Update the ListView with the filtered coupons
                    myCouponsAdapter.clear();
                    myCouponsAdapter.addAll(filteredCoupons);
                    myCouponsAdapter.notifyDataSetChanged();
                }
            }
        });


        /*** ListView Item Click Listener ***/
        listViewMyCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow != -1) {
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                selectedRow = position;
                listViewMyCoupons.setSelection(selectedRow);
                bgLayout = (ConstraintLayout) view.findViewById(R.id.myCoupons_lvLine_constraintLayout);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(186, 208, 227));
            }
        });


        return view;
    }


    public void setFacadetwo(CustomerFacade customerFacade){
        customerFacadethree=customerFacade;

    }
    public void setContext(Context context) {this.context = context;}

    public void setLvMyCouponsSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
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