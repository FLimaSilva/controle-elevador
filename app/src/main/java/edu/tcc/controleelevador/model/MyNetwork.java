package edu.tcc.controleelevador.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.tcc.controleelevador.dados.Constants;
import edu.tcc.controleelevador.dados.Util;

/**
 * Created by uellisson on 26/11/2017.
 */

public class MyNetwork {
    private String net1Name;
    private String net1password;
    private String net2Name;
    private String net2password;
    private String net3Name;
    private String net3password;

    public String getNet1Name() {
        return net1Name;
    }

    public void setNet1Name(String net1Name) {
        this.net1Name = net1Name;
    }

    public String getNet1password() {
        return net1password;
    }

    public void setNet1password(String net1password) {
        this.net1password = net1password;
    }

    public String getNet2Name() {
        return net2Name;
    }

    public void setNet2Name(String net2Name) {
        this.net2Name = net2Name;
    }

    public String getNet2password() {
        return net2password;
    }

    public void setNet2password(String net2password) {
        this.net2password = net2password;
    }

    public String getNet3Name() {
        return net3Name;
    }

    public void setNet3Name(String net3Name) {
        this.net3Name = net3Name;
    }

    public String getNet3password() {
        return net3password;
    }

    public void setNet3password(String net3password) {
        this.net3password = net3password;
    }

    /**
     * Método que salva um usuário no banco de dados
     * @param completionListener
     */
    public void saveNetworkd(DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("networks");

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
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
