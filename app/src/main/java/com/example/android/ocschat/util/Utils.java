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
     * checks if email is in valid format
     * Got from "https://codereview.stackexchange.com/questions/33546/simple-code-to-check-format-of-user-inputted-email-address"
     */
    public static boolean isValidEmail(String email){
        return email.matches("[a-zA-Z0-9\\.]+@[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9]{3}");
    }

    /**
     * checks if password is valid(6 characters or more)
     */
    public static boolean isValidPassword(String password){
        return password.length() >= 6;
    }

    /**
     * Checks if name is valid(consists of only letters)
     */
    public static boolean isValidName(String name){
        for(int i = 0; i < name.length(); i++){
            if(!((name.charAt(i) >= 65 && name.charAt(i) <= 90) || ((name.charAt(i) >= 97 && name.charAt(i) <= 122))))
                return false;
        }
        return true;
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
