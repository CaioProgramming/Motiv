package com.creat.motiv.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.creat.motiv.R;

public class Pref {
    SharedPreferences mySharedPreferences;
    Context context;
    public Pref(Context context){
        mySharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
        this.context = context;
    }
    public void setNight(boolean state){

        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            context.setTheme(R.style.AppTheme_Night);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean("NightMode", state);
            editor.commit();

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            context.setTheme(R.style.AppTheme);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean("NightMode", state);
            editor.commit();
        }
    }

    public boolean nightmodestate() {

        return mySharedPreferences.getBoolean("NightMode", false);

    }

    public void setAgree(boolean state) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Terms", state);
        editor.commit();
    }

    public boolean agreestate() {
        return mySharedPreferences.getBoolean("Terms", false);
    }


    public void setHomeTutorial(boolean state) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Home", state);
        editor.commit();
    }

    public boolean hometutorialstate() {
        return mySharedPreferences.getBoolean("Home", false);
    }

    public void setWriteTutorial(boolean state) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Write", state);
        editor.commit();
    }

    public boolean writetutorialstate() {
        return mySharedPreferences.getBoolean("Write", false);
    }

    public void setProfileTutorial(boolean state) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Profile", state);
        editor.commit();
    }

    public boolean profiletutorialstate() {
        return mySharedPreferences.getBoolean("Profile", false);
    }

    public void setAlarm(boolean state){
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("Alarm",state);
        editor.commit();
    }


    public boolean alarm(){
        Boolean alarm =mySharedPreferences.getBoolean("Alarm",true);
        return alarm;
    }


}
