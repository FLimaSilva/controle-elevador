package edu.tcc.controleelevador.view;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Classe chamada quando o usuário abre o aplicativo
 */
public class CustomApplication extends Application {

    /**
     * Abre a tela pricipal do aplicativo e inicia a conexão
     * com o firebase
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(getApplicationContext());
    }
}