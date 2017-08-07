package com.br.uellisson.controleelevador.view;

import android.app.Application;

import com.firebase.client.Firebase;


public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(getApplicationContext());
    }
}