package com.br.uellisson.controleelevador.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.CallElevator;
import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.br.uellisson.controleelevador.model.Notify;
import com.br.uellisson.controleelevador.view.adapter.NotificationsRecyclerView;
import com.br.uellisson.controleelevador.view.adapter.NotificationsViewHolder;
import com.br.uellisson.controleelevador.view.adapter.ReportRecyclerView;
import com.br.uellisson.controleelevador.view.adapter.ReportViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
/**
 * Classe responsável pela criação e operação da tela
 * de Notificações do aplicativo
 */
public class NotificationsActivity extends BaseActivity implements ValueEventListener {

    /**
     * Atributo da Classe
     */
    private DatabaseReference databaseReference;

    /**
     * Método onde é criada a tela de Notificações do aplicativo
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notifications));

        databaseReference = Util.getFirebase();
        final Notify notifyContext = new Notify();
        notifyContext.dataNotifyUpdated( this );
        databaseReference.child("notifications");
        init();
    }

    private void init(){
        RecyclerView rvNotifications = (RecyclerView) findViewById(R.id.rv_notifications);
        rvNotifications.setHasFixedSize( true );
        rvNotifications.setLayoutManager( new LinearLayoutManager(this));
        NotificationsRecyclerView adapter = new NotificationsRecyclerView(
                Notify.class,
                R.layout.item_notifications,
                NotificationsViewHolder.class,
                databaseReference.child("notifications"));

        rvNotifications.setAdapter(adapter);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
