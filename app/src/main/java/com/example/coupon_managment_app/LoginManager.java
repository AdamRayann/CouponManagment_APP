package com.example.coupon_managment_app;

import android.content.Context;



/** This class is a Singleton class called LoginManager that contains a Login function that allows any
 *  of the three types of Clients to connect to the system.
 */
public class LoginManager {
    Context context;

    /**********************************************************************************************/
    /**********************************~ SINGLETON ~***********************************************/
    private static LoginManager instance = null;

    private LoginManager(Context context) {
        this.context = context;
    }

    public static LoginManager getInstance(Context context) {
        if (instance == null) {
            instance = new LoginManager(context);
        }
        return instance;
    }
    /**********************************************************************************************/

    public ClientFacade login(String email, String password, ClientType clientType) {
        switch (clientType) {
            case Administrator:
                AdminFacade adminfacade = new AdminFacade(context);
                if(adminfacade.login(email, password)) {
                    return adminfacade;
                }
                else {
                    return null;
                }
            case Company:
                CompanyFacade companyfacade = new CompanyFacade(context);
                if(companyfacade.login(email, password)) {
                    return companyfacade;
                }
                else {
                    return null;
                }
            case Customer:
                CustomerFacade customerfacade = new CustomerFacade(context);
                if(customerfacade.login(email, password)) {
                    return customerfacade;
                }
                else {
                    return null;
                }
        }
        return null;
    }
}
