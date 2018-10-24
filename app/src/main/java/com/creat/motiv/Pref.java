package com.creat.motiv;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    SharedPreferences mySharedPreferences;

    public Pref(Context context){
        mySharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }
    public void setNight(boolean state){
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    public boolean nightmodestate(){
        Boolean state = mySharedPreferences.getBoolean("NightMode",false);
        return state;
    }


}
