package com.julienhammer.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.julienhammer.go4lunch.R;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class SharedPrefsSingleton {

//        private SharedPreferences sharedPref;
//        private Context appContext;
//
//        private static SharedPrefsSingleton instance;
//
//        public static SharedPrefsSingleton getInstance(Context applicationContext){
//            if(instance == null)
//                instance = new SharedPrefsSingleton(applicationContext);
//            return instance;
//        }
//
//        private SharedPrefsSingleton(Context applicationContext) {
//            appContext = applicationContext;
//            sharedPref = appContext.getSharedPreferences(
//                    appContext.getString(R.string.key_pref), Context.MODE_PRIVATE );
//        }
//
//        public void writeData(float value) {
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putFloat(appContext.getString(R.string.key_data), value);
//            editor.apply();
//        }
//
//        public float readData() {
//            return sharedPref.getFloat(appContext.getString(R.string.key_data), 0);
//        }


}
