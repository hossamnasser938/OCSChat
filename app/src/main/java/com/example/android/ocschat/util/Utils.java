package com.example.android.ocschat.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    /**
     * checks if the device is connected to the internet or not
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * generates unique key each two friends using their guaranteed unique keys
     * @param currentUserId
     * @param friendId
     * @return
     */
    public static String generateMessageKey(String currentUserId, String friendId) throws OCSChatThrowable{
        int largestSize = (currentUserId.length() > friendId.length())? currentUserId.length() : friendId.length();
        int counter = 0;
        //while loop goes infinite if and only if there are tow users with the same key
        //which never happens with the aid of firebase unique keys
        while(true)
        if(currentUserId.charAt(counter) > friendId.charAt(counter)){
            return currentUserId+friendId;
        }
        else if(currentUserId.charAt(counter) < friendId.charAt(counter)){
            return friendId+currentUserId;
        }else{
            counter++;
            if(counter >= largestSize)
                throw new OCSChatThrowable(Constants.ERROR_GENERATING_MESSAGE_KEY);
        }
    }

}
