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
 */

public class User {
    public static String PROVIDER = "com.br.uellisson.controleelevador.model.User.PROVIDER";

    private String id;
    private String name;
    private String email;
    private String password;
    private String newPassword;
    private String floorsAllowed;

    public User() {
    }

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
     * Construtor Editar usuario
     * @param id
     * @param name
     * @param email
     * @param password
     * @param newPassword
     * @param floorsAllowed
     */
    public User(String id, String name, String email, String password, String newPassword, String floorsAllowed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.floorsAllowed = floorsAllowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setNameInMap( Map<String, Object> map ) {
        if( getName() != null ){
            map.put( "name", getName() );
        }
    }

    public void setNameIfNull(String name) {
        if( this.name == null ){
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    private void setEmailInMap( Map<String, Object> map ) {
        if( getEmail() != null ){
            map.put( "email", getEmail() );
        }
    }

    public void setEmailIfNull(String email) {
        if( this.email == null ){
            this.email = email;
        }

    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getFloorsAllowed() {
        return floorsAllowed;
    }

    public void setFloorsAllowedIfNull(String email) {
        if( this.floorsAllowed == null ){
            this.floorsAllowed = floorsAllowed;
        }

    }

    public void saveDB(DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("users").child( getId() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void saveProviderSP(Context context, String token ){
        Util.saveSP( context, PROVIDER, token );
    }
    public String getProviderSP(Context context ){
        return( Util.getSP( context, PROVIDER) );
    }

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
