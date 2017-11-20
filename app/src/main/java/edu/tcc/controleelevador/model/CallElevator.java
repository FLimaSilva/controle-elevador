package edu.tcc.controleelevador.model;

import android.content.Context;

import edu.tcc.controleelevador.dados.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by uellisson on 31/08/2017.
 *
 * Casse do Modelo do objeto que representa
 * o relatório de frequência de uso do elevador
 */
public class CallElevator {
    /**
     * Atributos da classe
     */
    private String route;
    private String userName;
    private String date;
    private String hour;

    /**
     * Métodos usados para capturar informações dos atributos (gets)
     * e modificalos (sets)
     */
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

    public void setRoute(String route) {
        this.route = route;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * Método que busca todos os dados da frequência de uso do aplicativo.
     * @param context
     */
    public void dataFrequencyUse( Context context ){
        DatabaseReference firebase = Util.getFirebase().child("frequency_use").child("calls");

        firebase.addValueEventListener( (ValueEventListener) context );
    }

    /**
     * Método que registra a chamada do elevador no banco de dados.
     * @param id
     * @param completionListener
     */
    public void saveCall( String id, DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("frequency_use").child("calls").child( id );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }
}
