package edu.tcc.controleelevador.dados;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by uellisson on 07/09/2017.
 * Está classe tem alguns métodos úteis, que são chamada em
 * vários locais do aplicativo.
 */
public final class Util {
    public static String PREF = "com.br.uellisson.controleelevador.PREF";
    private static DatabaseReference firebase;

    /**
     * Este método pega a referêcia base do firebase, que é usada
     * para interagir com o banco de dados.
     *
     * @return
     */
    public static DatabaseReference getFirebase(){
        if( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }

        return( firebase );
    }

    /**
     * Este método salva algumas preferêcias do aplicativo,
     * que são usadas recorrentemente, como o e-mail do usuário.
     * @param context
     * @param key
     * @param value
     */
    static public void saveSP(Context context, String key, String value ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * Este método busca as preferências salvas pelo método saveSP.
     * @param context
     * @param key
     * @return
     */
    static public String getSP(Context context, String key ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return( token );
    }
}