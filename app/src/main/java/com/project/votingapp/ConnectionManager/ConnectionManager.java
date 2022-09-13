package com.project.votingapp.ConnectionManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {

    //Checking Internet Connections
    public static boolean isNetworkConnectionAvailable(Context context){

        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return  networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException ex){
            return false;
        }

    }

}
