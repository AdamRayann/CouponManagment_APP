package com.example.coupon_managment_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 */
public class CustomersFragment extends Fragment {
    Context context;
    ImageButton btnNew, btnEdit, btnDelete;
    ListView customersListView;
    /************************* ListView Adapter related Variables *********************************/
    ArrayList<Customer> customerArrayList;
    FragCustomerLVAdapter adapter;
    AdminFacade adminFacade = new AdminFacade(context);
    /***************************~ ListView Selected line Management  ~*****************************/
    private int lvSelectedRow = -1;
    String key;
    private ActivityResultLauncher<Intent> customerFraglauncher;
    private ConstraintLayout bgLayout;
    private int bgLineColor;
    /**********************************************************************************************/
    public void setLvCustomersSelectedRow(int selectedRow) {
        this.lvSelectedRow = selectedRow;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_customers, container, false);
        if (this.getArguments()!=null){
            Bundle bundle=this.getArguments();
            key=bundle.getString("key");
            this.setArguments(new Bundle());}
        btnNew = fragView.findViewById(R.id.fragmentCustomers_btnNew);
        btnEdit = fragView.findViewById(R.id.fragmentCustomers_btnEdit);
        btnDelete = fragView.findViewById(R.id.fragmentCustomers_btnDelete);
        customersListView = fragView.findViewById(R.id.fragmentCustomers_listView);

        /** Fill Customers ArrayList **/
        try {
            customerArrayList=adminFacade.customersDAO.getAllCustomers();
        } catch (CustomerException e) {
            String errorMessage = e.getMessage();
            System.out.println("Error: " + errorMessage);
        }
        /*****************************/
        adapter = new FragCustomerLVAdapter(context, R.layout.fragment_customers_listview_line, customerArrayList);
        customersListView.setAdapter(adapter);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnNew.setOnClickListener(buttonsClick);
        btnEdit.setOnClickListener(buttonsClick);
        btnDelete.setOnClickListener(buttonsClick);

        customerFraglauncher=startCustomerFraglauncher();
        /***************************~ Set BG Color To Clicked Line  ~******************************/
        customersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lvSelectedRow != -1)//old
                {
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                lvSelectedRow = position; //update selected row to point to the new selection
                customersListView.setSelection(lvSelectedRow); //set selection
                bgLayout = view.findViewById(R.id.fragCust_lvLine_constraintLayout); //get Layout of row
                bgLineColor = bgLayout.getSolidColor(); // save  default color
                bgLayout.setBackgroundColor(getResources().getColor(R.color.bblue)); //set clicked line color
            }
        });

        if (key!=null && key.equals("1"))
            openNewCustomerFrag();
        /******************************************************************************************/
        return fragView;
    }

    private void openNewCustomerFrag() {
        Intent intent = new Intent(context, NewUserActivityForCustomerFrag.class);
        intent.putExtra("requestCode", 1);
        try{
            customerFraglauncher.launch(intent);
        }
        catch (Exception ex) {
            Toast.makeText(context,"NEW CUSTOMER Activity Launcher Failed!", Toast.LENGTH_LONG).show();
        }
    }

    /***************************~ Buttons Click Listener Class  ~**********************************/
    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId() == btnNew.getId()) {
                Intent intent = new Intent(context, NewUserActivityForCustomerFrag.class);
                intent.putExtra("requestCode", 1);
                try{
                    customerFraglauncher.launch(intent);
                }
                catch (Exception ex) {
                    Toast.makeText(context,"NEW CUSTOMER Activity Launcher Failed!", Toast.LENGTH_LONG).show();
                }
            }
            if(v.getId() == btnEdit.getId()) {
                if(adapter == null || adapter.getCount() == 0){
                    Toast.makeText(context, "Customers List is Empty!", Toast.LENGTH_LONG).show();
                    return;
                } else if(adapter != null) {
                    if (lvSelectedRow >= 0 && lvSelectedRow < adapter.getCount()) {
                        Intent intent = new Intent(context, EditUserActivityForCustomerFrag.class);
                        intent.putExtra("customer", adapter.getItem(lvSelectedRow));
                        intent.putExtra("requestCode", 2);
                        try {
                            customerFraglauncher.launch(intent);
                        } catch (Exception ex) {
                            Toast.makeText(context, "EDIT Customer Activity Launcher Failed!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(context, "Please Select A Customer To Edit!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            if(v.getId() == btnDelete.getId()) {
                if(adapter == null || adapter.getCount() == 0){
                    Toast.makeText(context, "Customers List is Empty!", Toast.LENGTH_LONG).show();
                    return;
                } else if(adapter != null){
                    if (lvSelectedRow >= 0 && lvSelectedRow < adapter.getCount()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Confirmation");
                        builder.setMessage("Are you sure you want to Delete the selected customer ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                        try {
                            adminFacade.customersDAO.deleteCustomer(adapter.getItem(lvSelectedRow));
                            adapter.refreshAllCustomers(adminFacade.customersDAO.getAllCustomers());
                        } catch (Exception ex) {
                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(context, "Please Select A Customer To Delete!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    /*********************************~ LAUNCHER  ~************************************************/
    private ActivityResultLauncher<Intent> startCustomerFraglauncher() {
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            int requestCode = intent.getIntExtra("requestCode", 0);
                            if (requestCode == 1) {   //new user
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Customer customer = (Customer) intent.getSerializableExtra("customer");
                                    if (customer != null) {
                                        try {
                                            //CustomersDBDAO.getInstance(context).addCustomer(customer);
                                            adminFacade.addCustomer(customer);
                                            adapter.refreshCustomerAdded(customer);
                                        } catch (Exception ex) { // suitable Exception to be added!
                                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                            else if(requestCode==2){ //Edit user
                                if (result.getResultCode() == Activity.RESULT_OK){
                                    Customer customer = (Customer) intent.getSerializableExtra("customer");
                                    if (customer != null) {
                                        try {
                                            //CustomersDBDAO customersDBDAO=CustomersDBDAO.getInstance(context);
                                            adminFacade.updateCustomer(customer);
                                            adapter.refreshAllCustomers(adminFacade.customersDAO.getAllCustomers());
                                        } catch (Exception ex) { // suitable Exception to be added!
                                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                            else {
                                Toast.makeText(context, "requestCode not recognized", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
        return launcher;
    }
}