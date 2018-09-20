package com.example.android.ocschat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.User;

import java.util.List;

public class Utils {

    private final static String TAG = "Utils";

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
     * checks if age is valid(larger than zero and not floating point value)
     * @param age
     */
    public static boolean isValidAge(Double age){
        if(age <= 0.0 || (age - age.intValue()) != 0.0) {
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

    /**
     * checks if list of users contains a specific user object
     * @param usersList
     * @param user
     * @return
     */
    public static boolean userExistsInList(List<User> usersList, User user){
        if(usersList.size() != 0){
            for(User listUser : usersList){
                if(listUser.getId().equals(user.getId()))
                    return true;
            }
        }
        return false;
    }

    public static void setDownloadFlag(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.DOWNLOAD_FLAG_KEY, true).apply();
        Log.d(TAG, "set download flag");
    }

    public static void clearDownloadFlag(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.DOWNLOAD_FLAG_KEY, false).apply();
        Log.d(TAG, "clear download flag");
    }

    public  static boolean isDownloadFlag(Context context) throws Exception{
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Constants.DOWNLOAD_FLAG_KEY)){
            return sharedPreferences.getBoolean(Constants.DOWNLOAD_FLAG_KEY, false);
        }
        else {
            throw new Exception(context.getString(R.string.shared_preferences_error));
        }

    }

}
