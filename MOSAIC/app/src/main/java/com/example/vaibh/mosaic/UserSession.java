package com.example.vaibh.mosaic;


import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    Context context;
    SharedPreferences sharedPreferences;

    private static final String PREF_NAME="MOSAIC_PREF";

    private static final String USER_EMAIL="USER_EMAIL";
    private static final String IS_LOGIN="IS_LOGIN";
    private static final String EMP_ID="EMP_ID";
    private static final String REST_ID="REST_ID";

    public UserSession(Context context)
    {

        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);

    }

    public void removeUser()    {
        sharedPreferences.edit().clear().apply();
    }

    public String getEmailId() {
        return sharedPreferences.getString(USER_EMAIL,"");
    }

    public void setEmailId(String emailId) {
        sharedPreferences.edit().putString(USER_EMAIL,emailId).apply();

    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public void setIsLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(IS_LOGIN,isLogin).apply();
    }

    public int getEmpId() {
        return sharedPreferences.getInt(EMP_ID,-1);
    }

    public void setEmpId(int emailId) {
        sharedPreferences.edit().putInt(EMP_ID,emailId).apply();

    }

    public int getRestId() {
        return sharedPreferences.getInt(REST_ID, -1);
    }

    public void setRestId(int emailId) {
        sharedPreferences.edit().putInt(REST_ID,emailId).apply();
    }


}
