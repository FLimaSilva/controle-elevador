package com.br.uellisson.controleelevador.model;

import android.content.Context;

import com.br.uellisson.controleelevador.dados.Util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uellisson on 07/09/2017.
 */
@IgnoreExtraProperties
public class FrequencyUse {

   // private CallElevator calls;
    private String firstUse;
    private String lastUse;
    private int quantityCall;

    public FrequencyUse() {
    }

    public String getFirstUse() {
        return firstUse;
    }

    private void setFirstUseInMap( Map<String, Object> map ) {
        if( getFirstUse() != null ){
            map.put( "firstUse", getFirstUse() );
        }
    }

    public String getLastUse() {
        return lastUse;
    }

    private void setLastUseInMap( Map<String, Object> map ) {
        if( getLastUse() != null ){
            map.put( "lastUse", getLastUse() );
        }
    }

    public int getQuantityCall() {
        return quantityCall;
    }

    private void setQuantityCallInMap( Map<String, Object> map ) {
        if( getQuantityCall() != 0 ){
            map.put( "quantityCall", getQuantityCall() );
        }
    }

    public void setFirstUse(String firstUse) {
        this.firstUse = firstUse;
    }

    public void setLastUse(String lastUse) {
        this.lastUse = lastUse;
    }

    public void setQuantityCall(int quantityCall) {
        this.quantityCall = quantityCall;
    }

    public void dataFrequencyUse(Context context ){
        DatabaseReference firebase = Util.getFirebase().child("frequency_use");
        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
    }

    public void dataFrequencyUseUpdated(Context context ){
        DatabaseReference firebase = Util.getFirebase().child("frequency_use");
        firebase.addValueEventListener( (ValueEventListener) context );
    }

    public void saveFrequencyCall(DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("frequency_use");

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void updateFrequencyCall( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = Util.getFirebase().child("frequency_use");

        Map<String, Object> map = new HashMap<>();
        setLastUseInMap(map);
        setQuantityCallInMap(map);
        setFirstUseInMap(map);

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
}
