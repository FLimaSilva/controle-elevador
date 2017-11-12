package com.br.uellisson.controleelevador.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.br.uellisson.controleelevador.dados.Constants;
import com.br.uellisson.controleelevador.dados.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by uellisson on 05/08/2017.
 *
 * Classe modelo do objeto usuário
 */
public class User {
    /**
     * Atributos da classe
     */
    public static String PROVIDER = "com.br.uellisson.controleelevador.model.User.PROVIDER";
    private String id;
    private String name;
    private String email;
    private String password;
    private String newPassword;
    private String floorsAllowed;

    /**
     * Construtor vazio
     */
    public User() {}

    /**
     * Construtor login usuario
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /** Contrutor cadastro usuario
      * @param name
     * @param email
     * @param password
     * @param floorsAllowed
     */
    public User(String name, String email, String password, String floorsAllowed) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.floorsAllowed = floorsAllowed;
    }

    /**
     * Métodos usados para capturar informações dos atributos (gets)
     * e modificalos (sets)
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setNameIfNull(String name) {
        if( this.name == null ){
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmailIfNull(String email) {
        if( this.email == null ){
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public String getFloorsAllowed() {
        return floorsAllowed;
    }

    /**
     * Método que salva um usuário no banco de dados
     * @param completionListener
     */
    public void saveDB(DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("users").child( getId() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    /**
     * Méfod que salva o token do usuário logado
     * nas preferÊncias do aplicativo.
     * @param context
     * @param token
     */
    public void saveProviderSP(Context context, String token ){
        Util.saveSP( context, PROVIDER, token );
    }

    /**
     * Método que busca os dados do usuário no banco.
     * @param context
     */
    public void dataUser(Context context ){
        String idUser = Util.getSP(context, Constants.USER_ID);

        DatabaseReference firebase = Util.getFirebase().child("users").child(idUser);
        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
    }

    public void saveUserEdit(String userId, DatabaseReference.CompletionListener... completionListener){
        DatabaseReference firebase = Util.getFirebase().child("users").child(userId);

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }
}
