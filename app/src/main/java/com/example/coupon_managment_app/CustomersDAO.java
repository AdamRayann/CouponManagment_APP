package com.example.coupon_managment_app;

import java.util.ArrayList;


/****** This interface defines the Data Access Object (DAO) design pattern for the Customer entity.
 It provides methods for performing CRUD (Create, Read, Update, Delete) operations on customer data. **********/

public interface CustomersDAO {



    // return True if a customer with the provided email and password exists, false otherwise.
    Boolean isCustomerExists(String email , String Password);

    //  Adds a new customer to the customer list and the customer table.
    Void addCustomer(Customer customer) throws CustomerException;

    // this function Updates an existing customer's information.
    Void updateCustomer(Customer customer) throws CustomerException;

    // this function Deletes a customer from the customer list and the customer table.
    Void deleteCustomer(Customer customer) throws CustomerException;

    // this function Retrieves a list of all customers.
    ArrayList<Customer> getAllCustomers() throws CustomerException;

    //Retrieves a customer by their unique CustomerID.
    Customer getOneCustomer(int CustomerID) throws CustomerException;

    //Retrieves a customer by their email address
    Customer getCustomerByEmail(String email) throws CustomerException;

    //Clears all customer data (removes all customers).
    void clearAllCustomers() throws CustomerException;
}
