package com.creat.motiv.Utils;

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

    public void setAlarm(boolean state){
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Alarm",state);
        editor.commit();
    }

    public boolean nightmodestate(){
        Boolean state = mySharedPreferences.getBoolean("NightMode",false);
        return state;
    }

    public boolean alarm(){
        Boolean alarm =mySharedPreferences.getBoolean("Alarm",true);
        return alarm;
    }


}
