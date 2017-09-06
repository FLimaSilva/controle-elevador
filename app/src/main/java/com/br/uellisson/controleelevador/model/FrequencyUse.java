package com.br.uellisson.controleelevador.model;

import android.content.Context;

import com.br.uellisson.controleelevador.dados.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by uellisson on 31/08/2017.
 *
 * Modelo do objeto que representa
 * o relatório de frequência de uso do elevador
 */
public class FrequencyUse {
    private String route;
    private String userName;
    private String date;
    private String hour;

    /**
     * Contrutor do objeto
     */
    public FrequencyUse() {
    }

    public String getRoute() {
        return route;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    private void seRouteInMap( Map<String, Object> map ) {
        if( getUserName() != null ){
            map.put( "route", getRoute() );
        }
    }

    private void setUserNameInMap( Map<String, Object> map ) {
        if( getUserName() != null ){
            map.put( "user_name", getUserName() );
        }
    }

    private void setDateInMap( Map<String, Object> map ) {
        if( getDate() != null ){
            map.put("date", getDate());
        }
    }

    private void setHourInMap( Map<String, Object> map ) {
        if( getDate() != null ){
            map.put("hour", getDate());
        }
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = Util.getFirebase().child("frequency_use");

        Map<String, Object> map = new HashMap<>();
        setUserNameInMap(map);
        setDateInMap(map);

        if( map.isEmpty() ){
            return;
        }

        if( completionListener.length > 0 ){
            firebase.updateChildren(map, completionListener[0]);
        }
        else{
            firebase.updateChildren(map);
        }
    }

    public void removeDB( DatabaseReference.CompletionListener completionListener ){

        DatabaseReference firebase = Util.getFirebase().child("frequency_use");
        firebase.setValue(null, completionListener);
    }

    public void contextDataDB( Context context ){
        DatabaseReference firebase = Util.getFirebase().child("frequency_use");

        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
    }
}
