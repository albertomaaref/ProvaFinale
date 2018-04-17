package it.dsgroup.provafinale.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import it.dsgroup.provafinale.RootActivity;

/**
 * Created by alim on 22-Jan-18.
 */

public class Session {

    public static void logout (Context context){
        Intent i = new Intent(context,RootActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("utenteAttivo","");
        editor.commit();
        context.startActivity(i);
    }

    public static void killApp (Context context){

    }
}
