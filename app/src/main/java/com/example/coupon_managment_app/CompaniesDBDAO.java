package com.example.coupon_managment_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.Instant;
import java.util.ArrayList;

public class CompaniesDBDAO implements CompaniesDAO {
    private ArrayList<Company> companies = new ArrayList<>();
    private static CompaniesDBDAO compInstance;
    private final Db_Manager db_manager;

    // private constructor
    private CompaniesDBDAO(Context context) throws CompanyException {
        db_manager = Db_Manager.getInstance(context);
        companies=getAllCompanies();
    }


    // returns the same object - singelton deisign pattern
    public static CompaniesDBDAO getInstance(Context context)  {
        if(compInstance == null) {
            try {
                compInstance = new CompaniesDBDAO(context);
            } catch (CompanyException e) {
                String errorMessage = e.getMessage();
                System.out.println("Error: " + errorMessage);
            }
        }
        return compInstance;
    }



    // Check if a company with the specified email and password exists in the database.
    @Override
    public Boolean isCompanyExists(String email, String Password) {
        for(Company company:companies)
        {
            if(company.getEmail().equals(email)&&company.getPassword().equals(Password))
                return true;
        }
        return false;
    }



    // Add a new company to the database.
    @Override
    public Void addCompany(Company company) throws CompanyException {
        if(company==null)
            throw new CompanyException("The company you are trieng to add is Null");
        if(!isCompanyExists(company.getEmail(),company.getPassword()))
        {
            try{
            //add to DB
            SQLiteDatabase db = db_manager.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Db_Manager.COMPANY_NAME,company.getName());
            cv.put(Db_Manager.COMPANY_EMAIL,company.getEmail());
            cv.put(Db_Manager.COMPANY_PASSWORD,company.getPassword());

            int newCompanyId=(int) db.insert(Db_Manager.TABLE_COMPANIES,null,cv);
            company.setId(newCompanyId);
            db.close();
            companies.add(company);}
            catch (Exception e){
                throw new CompanyException("Error while Adding a company to the database!");
            }
        }
        return null;
    }


    // Update an existing company in the companies database.
    @Override
    public Void updateCompany(Company company) throws CompanyException {
        if(company==null)
            throw new CompanyException("The company that you are trying to update is Null");
        Company tobeupdated=getCompanyByName(company.getName());
            if(tobeupdated!=null)
            {
                try {
                    tobeupdated.setName(company.getName());
                    tobeupdated.setEmail(company.getEmail());
                    tobeupdated.setPassword(company.getPassword());
                    //update DB
                    SQLiteDatabase db = db_manager.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(Db_Manager.COMPANY_NAME, tobeupdated.getName());
                    cv.put(Db_Manager.COMPANY_EMAIL, company.getEmail());
                    cv.put(Db_Manager.COMPANY_PASSWORD, company.getPassword());
                    db.update(Db_Manager.TABLE_COMPANIES, cv, Db_Manager.COMPANY_ID + "=?", new String[]{String.valueOf(tobeupdated.getId())});
                    db.close();
                } catch (Exception e){
                    throw new CompanyException("Error while updating a company in the database!");
                }

            }
        return null;
    }


    // Get a company by its email address.
    public Company getCompanyByEmail(String email) throws CompanyException {
        if(companies==null || email==null)
            throw new CompanyException("can't get a comapny by email! Email might be Null!");
        for(Company company:companies)
        {
            if(company.getEmail().equals(email))
                return company;
        }
        return null;
    }

    @Override
    public boolean isCompanyNameExists(String name) {
        for(Company company:companies)
        {
            if(company.getName().equals(name))
                return true;
        }
        return false;
    }

    // Update an existing company in the companies database.
    public Company getCompanyByName(String name) throws CompanyException {
        if(companies==null || name==null)
            throw new CompanyException("can't get a comapny by name! name might be Null!");
        for(Company company:companies)
        {
            if(company.getName().equals(name))
                return company;
        }
        return null;
    }


    // Delete an existing company from the database.
    @Override
    public Void deleteCompany(Company company) throws CompanyException {
        if(company==null)
            throw new CompanyException("the company you are trying to delete is Null!");
        Company tobedeleted=getOneCompany(company.getId());
        if(tobedeleted!=null)
        {
            companies.remove(tobedeleted);
            try {
                //delete from DB
                db_manager.getWritableDatabase().delete(Db_Manager.TABLE_COMPANIES, Db_Manager.COMPANY_ID + "=?", new String[]{String.valueOf(tobedeleted.getId())});
            } catch (Exception e){
                throw  new CompanyException("Error while Deleting a company from the database!");
            }
        }
        return null;    }


    // Get a list of all companies in the database.
    @Override
    public ArrayList<Company> getAllCompanies() throws CompanyException {
        ArrayList<Company> companies = new ArrayList<>();
        String[] fields = {Db_Manager.COMPANY_ID,Db_Manager.COMPANY_NAME ,Db_Manager.COMPANY_EMAIL,Db_Manager.COMPANY_PASSWORD };
        String companyName, companyEmail, companyPassword;
        int companyID;
        try {
            Cursor cr = db_manager.getCursor(Db_Manager.TABLE_COMPANIES, fields, null);
            if (cr.moveToFirst())
                do {
                    companyID =(int) cr.getLong(0); // get the id (primary key)
                    companyName = cr.getString(1);
                    companyEmail = cr.getString(2);
                    companyPassword = cr.getString(3);
                    Company company= new Company(companyName, companyEmail, companyPassword);
                    // set id of customer
                    company.setId(companyID);
                    companies.add(company);
                } while (cr.moveToNext());
            return companies;
        }catch(Exception e){
            throw  new CompanyException("Error while getting all companies from the database!");
        }    }


    // Get a single company by its unique CompanyID.
    @Override
    public Company getOneCompany(int CompanyID) throws CompanyException {
        if(companies==null || CompanyID<0)
            throw new CompanyException("can't get a comapny by Id! id might be invalid!");
        for(Company company:companies)
        {
            if(company.getId()==CompanyID)
                return company;
        }
        return null;
    }

    public void clearAllCompanies() throws CompanyException {
        companies.clear();
        try {
            db_manager.getWritableDatabase().delete(Db_Manager.TABLE_COMPANIES, null, null);
        } catch (Exception e){
            throw  new CompanyException("Error while Deleting all companies from the database!");
        }
    }
}
