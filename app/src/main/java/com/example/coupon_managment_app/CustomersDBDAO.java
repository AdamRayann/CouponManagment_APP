package com.example.coupon_managment_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;
import java.util.ArrayList;


public class CustomersDBDAO implements CustomersDAO {
    private ArrayList<Customer> customers = new ArrayList<>();
    private static CustomersDBDAO custInstance;
    private Db_Manager db_manager;
    Context contextmod;


    // private constructor cause we want a singelton class
    private  CustomersDBDAO(Context context) {
        db_manager = Db_Manager.getInstance(context);
        this.contextmod=context;
        try {
            customers=getAllCustomers();
        } catch (CustomerException e) {
            String errorMessage = e.getMessage();
            System.out.println("Error: " + errorMessage);
            Toast.makeText(contextmod, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    }


    // returns the same object - singelton
    public static CustomersDBDAO getInstance(Context context) {
        if(custInstance == null) {
            custInstance = new CustomersDBDAO(context);
        }
        return custInstance;
    }




    // return True if a customer with the provided email and password exists, false otherwise.
    @Override
    public Boolean isCustomerExists(String email, String Password) {
        for(Customer customer:customers)
        {
            if(customer.getEmail().equals(email)&&customer.getPassword().equals(Password))
                return true;
        }
        return false;
    }



    //  Adds a new customer to the customer list and the customer table.
    @Override
    public Void addCustomer(Customer customer) throws CustomerException {
        if(customer==null)
            throw new CustomerException("Customer is null");
        if(!isCustomerExists(customer.getEmail(),customer.getPassword()))
        {
            try{
                ContentValues cv = new ContentValues();
                cv.put(db_manager.CUSTOMERS_FNAME,customer.getFirstName());
                cv.put(db_manager.CUSTOMERS_LNAME,customer.getLastName());
                cv.put(db_manager.CUSTOMERS_EMAIL,customer.getEmail());
                cv.put(db_manager.CUSTOMERS_PASSWORD,customer.getPassword());

                int newcustomerid=(int) db_manager.getWritableDatabase().insert(db_manager.TABLE_CUSTOMERS,null,cv);
                customer.setId(newcustomerid);
                customers.add(customer);
            } catch (Exception e) {
                throw new CustomerException("Failed to add customer to the database");
            }
        }
        return null;
    }



    // this function Updates an existing customer's information.
    @Override
    public Void updateCustomer(Customer customer) throws CustomerException{
        if(customer==null)
            throw new CustomerException("Customer is null");
        Customer tobeupdated=getOneCustomer(customer.getId());
        if(tobeupdated!=null) {
            tobeupdated.setFirstName(customer.getFirstName());
            tobeupdated.setLastName(customer.getLastName());
            tobeupdated.setEmail(customer.getEmail());
            tobeupdated.setPassword(customer.getPassword());
            //update DB
            ContentValues cv = new ContentValues();
            cv.put(db_manager.CUSTOMERS_FNAME, customer.getFirstName());
            cv.put(db_manager.CUSTOMERS_LNAME, customer.getLastName());
            cv.put(db_manager.CUSTOMERS_EMAIL, customer.getEmail());
            cv.put(db_manager.CUSTOMERS_PASSWORD, customer.getPassword());
            try {
                db_manager.getWritableDatabase().update(db_manager.TABLE_CUSTOMERS, cv, db_manager.CUSTOMERS_ID + "=?", new String[]{String.valueOf(tobeupdated.getId())});
            } catch (Exception e) {
                throw new CustomerException("Failed to update customer in the database");
            }
        }
        return null;
    }



    // this function Deletes a customer from the customer list and the customer table.
    @Override
    public Void deleteCustomer(Customer customer)throws CustomerException {
        if(customer==null)
            throw new CustomerException("Customer is null");
        Customer tobedeleted=getOneCustomer(customer.getId());
        if(tobedeleted!=null) {
            customers.remove(tobedeleted);
            //delete from DB
            try {
                db_manager.getWritableDatabase().delete(db_manager.TABLE_CUSTOMERS, db_manager.CUSTOMERS_ID + "=?", new String[]{String.valueOf(tobedeleted.getId())});
            } catch (Exception e) {
                throw new CustomerException("Failed to delete customer from the database");
            }
        }
        return null;
    }



    // this function Retrieves a list of all customers.
    @Override
    public ArrayList<Customer> getAllCustomers() throws CustomerException {
        ArrayList<Customer> customers = new ArrayList<>();
        String[] fields = {db_manager.CUSTOMERS_ID,db_manager.CUSTOMERS_FNAME ,db_manager.CUSTOMERS_LNAME,db_manager.CUSTOMERS_EMAIL ,db_manager.CUSTOMERS_PASSWORD};
        String customerfName, customerlName, customerEmail, customerPassword;
        int customerID;
        try {
            Cursor cr = db_manager.getCursor(db_manager.TABLE_CUSTOMERS, fields, null);
            if (cr.moveToFirst())
                do {
                    customerID =(int) cr.getLong(0); // get the id (primary key
                    customerfName = cr.getString(1);
                    customerlName = cr.getString(2);
                    customerEmail = cr.getString(3);
                    customerPassword = cr.getString(4);
                    Customer customer= new Customer( customerfName, customerlName, customerEmail, customerPassword);
                    // set id of customer
                    customer.setId(customerID);
                    customers.add(customer);
                } while (cr.moveToNext());
            return customers;
        }catch (Exception e) {
            throw new CustomerException("Failed to retrieve customers from the database");
        }
    }



    //Retrieves a customer by their unique CustomerID.
    @Override
    public Customer getOneCustomer(int CustomerID) throws CustomerException{
        if(customers==null || CustomerID<0)
            throw new CustomerException("Invalid customer ID");
        for(Customer customer:customers)
        {
            if(customer.getId()==CustomerID)
                return customer;
        }
        return null;
    }



    //Retrieves a customer by their email address
    public Customer getCustomerByEmail(String email) throws CustomerException
    {
        if(customers==null || email==null)
            throw new CustomerException("Invalid email");
        for(Customer customer:customers)
        {
            if(customer.getEmail().equals(email))
                return customer;
        }
        return null;
    }



    //Clears all customer data (removes all customers).
    public void clearAllCustomers() throws CustomerException {
        try {
            customers.clear();
            db_manager.getWritableDatabase().delete(db_manager.TABLE_CUSTOMERS, null, null);
        }
        catch (Exception e){
            throw new CustomerException("Failed to clear customers!");
        }
    }
}
