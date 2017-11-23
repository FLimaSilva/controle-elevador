package edu.tcc.controleelevador.model;

import android.content.Context;

import edu.tcc.controleelevador.dados.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by uellisson on 07/09/2017.
 *
 * Classe de modelo do objeto das notificações
 */
public class Notify {
    /**
     * Atributos da classe
     */
    private String notify;
    private String dateHour;

    public Notify() {}

    public Notify(String notify, String dateHour) {
        this.notify = notify;
        this.dateHour = dateHour;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getDateHour() {
        return dateHour;
    }

    public void dataNotifyUpdated( Context context ){
        DatabaseReference firebase = Util.getFirebase().child("notifications");

        firebase.addValueEventListener( (ValueEventListener) context );
    }

    public void saveNotify(String id, DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("notifications").child(id);

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }
}
