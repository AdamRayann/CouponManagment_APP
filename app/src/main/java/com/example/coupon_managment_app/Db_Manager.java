package com.example.coupon_managment_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db_Manager extends SQLiteOpenHelper {

    // Database Constants
    private static final String DB_NAME = "CouponDB";
    private static final int DB_VERSION = 1;

    // Table Names
    public static final String TABLE_COMPANIES = "COMPANIES";
    public static final String TABLE_CUSTOMERS = "CUSTOMERS";
    public static final String TABLE_CATEGORIES = "CATEGORIES";
    public static final String TABLE_COUPONS = "COUPONS";
    public static final String TABLE_CUSTOMERS_VS_COUPONS = "CUSTOMERS_VS_COUPONS";


    // Names for the company table colums
    public static final String COMPANY_ID = "ID";
    public static final String COMPANY_NAME = "NAME";
    public static final String COMPANY_EMAIL = "EMAIL";
    public static final String COMPANY_PASSWORD = "PASSWORD";

    //  Names for the CUSTOMERS table colums
    public static final String CUSTOMERS_ID = "ID";
    public static final String CUSTOMERS_FNAME = "FIRST_NAME";
    public static final String CUSTOMERS_LNAME = "LAST_NAME";
    public static final String CUSTOMERS_EMAIL = "EMAIL";
    public static final String CUSTOMERS_PASSWORD = "PASSWORD";


    // Columns for CATEGORIES table
    public static final String CATEGORY_ID = "ID";
    public static final String CATEGORY_NAME = "NAME";


    // Columns for COUPONS table
    public static final String COUPONS_ID = "ID";
    public static final String KEY_COMPANY_ID_FK = "COMPANY_ID";
    public static final String KEY_CATEGORY_ID_FK = "CATEGORY_ID";
    public static final String COUPONS_TITLE = "TITLE";
    public static final String COUPONS_DESCRIPTION = "DESCRIPTION";
    public static final String COUPONS_START_DATE = "START_DATE";
    public static final String COUPONS_END_DATE = "END_DATE";
    public static final String COUPONS_AMOUNT = "AMOUNT";
    public static final String COUPONS_PRICE = "PRICE";
    public static final String COUPONS_IMAGE = "IMAGE";


    // Columns for CUSTOMERS_VS_COUPONS table
    public static final String KEY_CUSTOMER_ID_FK = "CUSTOMER_ID";
    public static final String KEY_COUPON_ID_FK = "COUPON_ID";

    // Singleton instance
    private static Db_Manager instance;


    //    private constructor-singelton class
    private Db_Manager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static Db_Manager getInstance(Context context) {
        if (instance == null) {
            instance = new Db_Manager(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            // Create COMPANIES table
            String CREATE_TABLE_COMPANIES = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPANIES + "(" +
                    COMPANY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COMPANY_NAME + " TEXT," +
                    COMPANY_EMAIL + " TEXT," +
                    COMPANY_PASSWORD + " TEXT)";
            db.execSQL(CREATE_TABLE_COMPANIES);

            // Create CUSTOMERS table
            String CREATE_TABLE_CUSTOMERS = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS + "(" +
                    CUSTOMERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CUSTOMERS_FNAME + " TEXT," +
                    CUSTOMERS_LNAME + " TEXT," +
                    CUSTOMERS_EMAIL + " TEXT," +
                    CUSTOMERS_PASSWORD + " TEXT)";
            db.execSQL(CREATE_TABLE_CUSTOMERS);

            // Create CATEGORIES table
            String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" +
                    CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CATEGORY_NAME + " TEXT)";
            db.execSQL(CREATE_TABLE_CATEGORIES);


            // Create COUPONS table
            String CREATE_TABLE_COUPONS = "CREATE TABLE IF NOT EXISTS " + TABLE_COUPONS + "(" +
                    COUPONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_COMPANY_ID_FK + " INTEGER," +
                    KEY_CATEGORY_ID_FK + " INTEGER," +
                    COUPONS_TITLE + " TEXT," +
                    COUPONS_DESCRIPTION + " TEXT," +
                    COUPONS_START_DATE + " DATE," +
                    COUPONS_END_DATE + " DATE," +
                    COUPONS_AMOUNT + " INTEGER," +
                    COUPONS_PRICE + " REAL," +
                    COUPONS_IMAGE + " TEXT," +
                    "FOREIGN KEY(" + KEY_COMPANY_ID_FK + ") REFERENCES " + TABLE_COMPANIES + "(" + COMPANY_ID + ")," +
                    "FOREIGN KEY(" + KEY_CATEGORY_ID_FK + ") REFERENCES " + TABLE_CATEGORIES + "(" + CATEGORY_ID + "))";
            db.execSQL(CREATE_TABLE_COUPONS);


            // Create CUSTOMERS_VS_COUPONS table with foreign key constraints
            String CREATE_TABLE_CUSTOMERS_VS_COUPONS = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS_VS_COUPONS + "(" +
                    KEY_CUSTOMER_ID_FK + " INTEGER," +
                    KEY_COUPON_ID_FK + " INTEGER," +
                    "PRIMARY KEY (" + KEY_CUSTOMER_ID_FK + ", " + KEY_COUPON_ID_FK + ")," +
                    "FOREIGN KEY(" + KEY_CUSTOMER_ID_FK + ") REFERENCES " + TABLE_CUSTOMERS + "(" + CUSTOMERS_ID + ")," +
                    "FOREIGN KEY(" + KEY_COUPON_ID_FK + ") REFERENCES " + TABLE_COUPONS + "(" + COUPONS_ID + "))";
            db.execSQL(CREATE_TABLE_CUSTOMERS_VS_COUPONS);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        //        Empty for now!

    }

    //    here we fill the category table with the values in our Enum - we call this function when we create the table.
    public void insertCategoriesFromEnum() {
        // Loop through the Category enum values
        SQLiteDatabase db = this.getReadableDatabase();
        for (Category category : Category.values()) {
            ContentValues values = new ContentValues();
            values.put(CATEGORY_NAME, category.name());

            try {
                // Insert the category into the CATEGORIES table
                db.insert(TABLE_CATEGORIES, null, values);
            }
            catch (SQLException e){
                throw e;
            }
        }

        db.close();
    }


    /******************************************************************************************/
    /*******************~ getters for table/column names if needed   ~*************************/
    public static String getCouponsIdColumn() {
        return COUPONS_ID;
    }

    public static String getKeyCompanyIdFkColumn() {
        return KEY_COMPANY_ID_FK;
    }

    public static String getKeyCategoryIdFkColumn() {
        return KEY_CATEGORY_ID_FK;
    }

    public static String getCouponsTitleColumn() {
        return COUPONS_TITLE;
    }

    public static String getCouponsDescriptionColumn() {
        return COUPONS_DESCRIPTION;
    }

    public static String getCouponsStartDateColumn() {
        return COUPONS_START_DATE;
    }

    public static String getCouponsEndDateColumn() {
        return COUPONS_END_DATE;
    }

    public static String getCouponsAmountColumn() {
        return COUPONS_AMOUNT;
    }

    public static String getCouponsPriceColumn() {
        return COUPONS_PRICE;
    }

    public static String getCouponsImageColumn() {
        return COUPONS_IMAGE;
    }

    public static String getTableNameCoupons() {
        return TABLE_COUPONS;
    }



    /******************************************************************************************/
    /******************* this method is used for emptying any table ~*************************/

    public void resetAutoIncrement(String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL("DELETE FROM " + tableName); // Delete all rows from the table
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + tableName + "'"); // Reset the auto-increment counter
        }
        catch (SQLException e){
            throw e;
        }
        finally {
            db.close();
        }

    }


    /******************************************************************************************/
    /******************* this method is used for emptying any table ~*************************/

    public Cursor getCursor(String tableName, String[] fields, String where) {
        String strQry = "SELECT ";
        for (int i = 0; i < fields.length; i++) {
            strQry += fields[i] + " ";
            if (i < fields.length - 1)
                strQry += ",";
        }
        strQry += " FROM " + tableName;
        if (where != null && !where.isEmpty())
            strQry += " " + where;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cr = db.rawQuery(strQry, null);
            return cr;
        } catch (Exception e) {
            throw e;
        }
    }
}