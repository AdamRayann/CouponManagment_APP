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

public class CompaniesFragment extends Fragment {
    private Context context;
    private ImageButton btnNew, btnEdit, btnDelete;
    private ListView companiesListView;
    /************************* ListView Adapter related Variables *********************************/
    ArrayList<Company> companiesArrayList;
    FragCompanyLVAdapter adapter;
    AdminFacade adminFacade = new AdminFacade(context);
    /***************************~ ListView Selected line Management  ~*****************************/
    private int lvSelectedRow = -1;
    String key;
    private ActivityResultLauncher<Intent> companyFraglauncher;
    private ConstraintLayout bgLayout;
    private int bgLineColor;
    /**********************************************************************************************/
    public void setLvCompaniesSelectedRow(int selectedRow) {
        this.lvSelectedRow = selectedRow;
    }
    public void setContext(Context context)
    {
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_companies, container, false);
        if (this.getArguments()!=null){
            Bundle bundle=this.getArguments();
            key=bundle.getString("key");
            this.setArguments(new Bundle());}

        btnNew = fragView.findViewById(R.id.fragmentCompanies_btnNew);
        btnEdit = fragView.findViewById(R.id.fragmentCompanies_btnEdit);
        btnDelete = fragView.findViewById(R.id.fragmentCompanies_btnDelete);
        companiesListView = fragView.findViewById(R.id.fragmentCompanies_listView);
        /** Fill Companies ArrayList **/
        try {
            companiesArrayList=adminFacade.companiesDAO.getAllCompanies();
        } catch (CompanyException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        /*****************************/
        adapter = new FragCompanyLVAdapter(context, R.layout.fragment_companies_listview_line, companiesArrayList);
        companiesListView.setAdapter(adapter);
        /***************************~ Buttons Click Listener  ~**************************************/
        ButtonsClick buttonsClick = new ButtonsClick();
        btnNew.setOnClickListener(buttonsClick);
        btnEdit.setOnClickListener(buttonsClick);
        btnDelete.setOnClickListener(buttonsClick);
        companyFraglauncher=startCompanyFraglauncher();
        /***************************~ Set BG Color To Clicked Line  ~******************************/
        companiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lvSelectedRow != -1)
                {
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                lvSelectedRow = position;
                companiesListView.setSelection(lvSelectedRow);
                bgLayout = (ConstraintLayout) view.findViewById(R.id.fragComp_lvLine_constraintLayout);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(getResources().getColor(R.color.bblue)); //set clicked line color
            }
        });
        if (key!=null && key.equals("3"))
            openAddCompanyFrag();
        return fragView;
    }

    private void openAddCompanyFrag() {
        Intent intent = new Intent(context, NewCompanyActivityForCompanyFrag.class);
        intent.putExtra("requestCode", 3);
        try{
            companyFraglauncher.launch(intent);
        }
        catch (Exception ex) {
            Toast.makeText(context,"NEW COMPANY Activity Launcher Failed!", Toast.LENGTH_LONG).show();
        }
    }

    /***************************~ Buttons Click Listener Class  ~**********************************/
    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId() == btnNew.getId()) {
                Intent intent = new Intent(context, NewCompanyActivityForCompanyFrag.class);
                intent.putExtra("requestCode", 3);
                try{
                    companyFraglauncher.launch(intent);
                }
                catch (Exception ex) {
                    Toast.makeText(context,"NEW COMPANY Activity Launcher Failed!", Toast.LENGTH_LONG).show();
                }
            }

            if(v.getId() == btnEdit.getId()) {
                if(adapter == null || adapter.getCount() == 0){
                    Toast.makeText(context, "Companies List is Empty!", Toast.LENGTH_LONG).show();
                    return;
                } else if(adapter != null) {
                    if (lvSelectedRow >= 0 && lvSelectedRow < adapter.getCount()) {
                        Intent intent = new Intent(context, EditCompanyActivityForCompanyFrag.class);
                        intent.putExtra("company", adapter.getItem(lvSelectedRow));
                        intent.putExtra("requestCode", 4);
                        try {
                            companyFraglauncher.launch(intent);
                        } catch (Exception ex) {
                            Toast.makeText(context, "EDIT COMPANY Activity Launcher Failed!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(context, "Please Select A Company To Edit!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            if(v.getId() == btnDelete.getId()) {
                if(adapter == null || adapter.getCount() == 0){
                    Toast.makeText(context, "Companies List is Empty!", Toast.LENGTH_LONG).show();
                    return;
                } else if(adapter != null) {
                    if (lvSelectedRow >= 0 && lvSelectedRow < adapter.getCount()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Confirmation");
                        builder.setMessage("Are you sure you want to Delete the selected Company ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                        try {
                            adminFacade.companiesDAO.deleteCompany(adapter.getItem(lvSelectedRow));
                            adapter.refreshAllCompanies(adminFacade.companiesDAO.getAllCompanies());
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
                        Toast.makeText(context, "Please Select A Company To Delete!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
    /*********************************~ LAUNCHER  ~************************************************/
    private ActivityResultLauncher<Intent> startCompanyFraglauncher() {
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            int requestCode = intent.getIntExtra("requestCode", 0);
                            if (requestCode == 3) {   //new company
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Company company = (Company) intent.getSerializableExtra("company");
                                    if (company != null) {
                                        try {
                                            adminFacade.companiesDAO.addCompany(company);
                                            adapter.refreshCompanyAdded(company);
                                        } catch (Exception ex) {
                                            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                            else if (requestCode == 4) { //Edit company
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    Company company = (Company) intent.getSerializableExtra("company");
                                    if (company != null) {
                                        try {
                                            adminFacade.companiesDAO.updateCompany(company);
                                            adapter.refreshAllCompanies(adminFacade.companiesDAO.getAllCompanies());
                                        } catch (Exception ex) {
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