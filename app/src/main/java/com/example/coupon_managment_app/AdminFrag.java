package com.example.coupon_managment_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminFrag extends Fragment {



    ImageButton btnCustomers, btnCompanies;
    private Animation rotateOpen = null;
    private Animation rotateClose = null;




    CustomersFragment customersFragment = new CustomersFragment();
    CompaniesFragment companiesFragment = new CompaniesFragment();
    Fragment currentFragment = null;
    int Silver= Color.parseColor("#C0C0C0");
    int Blue=Color.parseColor("#45D3FE");
    //int BlueUnClicked=Color.parseColor("#44BEE3");
    int ClickedBlue=Color.parseColor("#17A7DC");

    FloatingActionButton addFAB, ReturnFAB, addPersonFAB,addCompanyFAB;
    TextView tvReturn, tvAddPerson,tvAddCompany;
    Boolean isAllFabsVisible;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_frag, container, false);

        btnCustomers = view.findViewById(R.id.adminActivity_btnCustomers);
        btnCompanies = view.findViewById(R.id.adminActivity_btnCompanies);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnCustomers.setOnClickListener(buttonsClick);
        btnCompanies.setOnClickListener(buttonsClick);

//FloatingActionButton

        addFAB = view.findViewById(R.id.add_fab);
        ReturnFAB = view.findViewById(R.id.return_fab);
        addPersonFAB = view.findViewById(R.id.add_fab_person);
        addCompanyFAB = view.findViewById(R.id.add_fab_company);
        tvReturn = view.findViewById(R.id.add_fab_alarm_text);
        tvAddPerson = view.findViewById(R.id.add_fab_person_text);
        tvAddCompany=view.findViewById(R.id.add_fab_company_text);
        addFAB.setOnClickListener(buttonsClick);
        ReturnFAB.setOnClickListener(buttonsClick);
        addPersonFAB.setOnClickListener(buttonsClick);
        addCompanyFAB.setOnClickListener(buttonsClick);
        ReturnFAB.setVisibility(View.GONE);
        addPersonFAB.setVisibility(View.GONE);
        addCompanyFAB.setVisibility(View.GONE);
        tvAddCompany.setVisibility(View.GONE);
        tvReturn.setVisibility(View.GONE);
        tvAddPerson.setVisibility(View.GONE);
        isAllFabsVisible = false;
//FloatingActionButton/>

        return view;    }
    /******************************~ Buttons Click class ~*****************************************/
    private class ButtonsClick implements View.OnClickListener {
        public void onClick(View v)
        {
            if (v.getId() == btnCustomers.getId()) {
                btnCustomers.setBackgroundColor(ClickedBlue);
                btnCompanies.setBackgroundColor(Blue);

                if (companiesFragment != null) {
                    companiesFragment.setLvCompaniesSelectedRow(-1);
                }
                customersFragment.setContext(getActivity());
                replaceFragment(customersFragment);
                currentFragment = customersFragment;
            } else if (v.getId() == btnCompanies.getId()) {
                btnCompanies.setBackgroundColor(ClickedBlue);
                btnCustomers.setBackgroundColor(Blue);

                if (customersFragment != null) {
                    customersFragment.setLvCustomersSelectedRow(-1);
                }
                companiesFragment.setContext(getActivity());
                replaceFragment(companiesFragment);
                currentFragment = companiesFragment;
            }//FloatingActionButton
            else if(v.getId() == addFAB.getId()){
                btnGroupAutoClose();
                setAnimation();
            }
            else if(v.getId() == ReturnFAB.getId()){
                btnCustomers.setBackgroundColor(Blue);
                btnCompanies.setBackgroundColor(Blue);
                if (currentFragment == null) {
                    requireActivity().finish(); // Use requireActivity() to get the hosting activity
                }

                // Check if the current fragment is not null
                if (currentFragment != null) {
                    // Remove the current fragment
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = null;
                }
                btnGroupAutoClose();
                setAnimation();


            }
            else if(v.getId() == addPersonFAB.getId()){
                btnGroupAutoClose();
                setAnimation();
                btnCustomers.setBackgroundColor(ClickedBlue);
                btnCompanies.setBackgroundColor(Blue);

                if (companiesFragment != null) {
                    companiesFragment.setLvCompaniesSelectedRow(-1);
                }
                if (currentFragment!=customersFragment)
                {
                    customersFragment.setContext(getActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString("key", "1");
                    customersFragment.setArguments(bundle);
                    replaceFragment(customersFragment);
                    currentFragment = customersFragment;
                }


            }else if(v.getId() == addCompanyFAB.getId()){
                btnGroupAutoClose();
                setAnimation();
                btnCompanies.setBackgroundColor(ClickedBlue);
                btnCustomers.setBackgroundColor(Blue);


                if (customersFragment != null) {
                    customersFragment.setLvCustomersSelectedRow(-1);
                }
                if (currentFragment!=companiesFragment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("key","3");
                    companiesFragment.setArguments(bundle);
                    companiesFragment.setContext(getActivity());
                    replaceFragment(companiesFragment);
                    currentFragment = companiesFragment;
                }


            }//FloatingActionButton/>

        }

        private void replaceFragment(Fragment fragment) {
            if (isAllFabsVisible)
                btnGroupAutoClose();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.adminActivity_FragContainer, fragment);
            transaction.commit();
        }





    }
    private Animation getRotateOpenAnimation() {
        if (rotateOpen == null) {
            rotateOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim);
        }
        return rotateOpen;
    }

    private Animation getRotateCloseAnimation() {
        if (rotateClose == null) {
            rotateClose = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim);
        }
        return rotateClose;
    }


    private void setAnimation() {
        if (isAllFabsVisible)
        {
            addFAB.setAnimation(getRotateOpenAnimation());
        }else
        {
            addFAB.setAnimation(getRotateCloseAnimation());
        }
    }

    private void btnGroupAutoClose() {
        if(isAllFabsVisible){
            ReturnFAB.hide();
            addPersonFAB.hide();
            addCompanyFAB.hide();
            tvReturn.setVisibility(View.GONE);
            tvAddPerson.setVisibility(View.GONE);
            tvAddCompany.setVisibility(View.GONE);
        }else{
            ReturnFAB.show();
            addPersonFAB.show();
            addCompanyFAB.show();
            tvReturn.setVisibility(View.VISIBLE);
            tvAddPerson.setVisibility(View.VISIBLE);
            tvAddCompany.setVisibility(View.VISIBLE);

        }
        isAllFabsVisible = !isAllFabsVisible;

    }
}