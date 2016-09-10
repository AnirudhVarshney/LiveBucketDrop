package com.example.abhinav.bucketdrop;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.abhinav.bucketdrop.Adapter.Filter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ABHINAV on 21-04-2016.
 */
public class AppBucketDrop extends Application{ //this class contatins all the configurations of realms so we dont have to use it again and again
//u have to apply this in maifest bcoz we have change in application and will default present

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration configuration=new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);
    }
    public static void save (Context context,int filterOption){
    SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(context); //to be used in et item count to load data from shared prefrences
    SharedPreferences.Editor editor = pref.edit();
    editor.putInt("filter", filterOption);
    editor.apply();
}
    public   static int load(Context context){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        int filterOption=pref.getInt("filter", Filter.NONE);//filter.none is default
    return filterOption;
    }
    public static void setRalewayRegular(Context context, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");
        textView.setTypeface(typeface);
    }

    public static void setRalewayRegular(Context context, TextView... textViews) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");
        for (TextView textView : textViews) {
            textView.setTypeface(typeface);
        }
    }
}
