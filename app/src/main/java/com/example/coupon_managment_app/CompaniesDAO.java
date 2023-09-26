package com.example.coupon_managment_app;

import java.util.ArrayList;


/**
 * Interface defining the methods to interact with the companies table in the database.
 */

public interface CompaniesDAO {

    // Check if a company with the specified email and password exists in the database.
    Boolean isCompanyExists(String email , String Password);

    // Add a new company to the database.
    Void addCompany(Company company) throws CompanyException;

    // Update an existing company in the companies database.
    Void updateCompany(Company company) throws CompanyException;

    // Delete an existing company from the database.
    Void deleteCompany(Company company) throws CompanyException;

    // Get a list of all companies in the database.
    ArrayList<Company> getAllCompanies() throws CompanyException;

    // Get a single company by its unique CompanyID.
    Company getOneCompany(int CompanyID) throws CompanyException;

    // Get a company by its email address.
    Company getCompanyByEmail(String email) throws CompanyException;

    // check a company by its name.
    boolean isCompanyNameExists(String name);
}
