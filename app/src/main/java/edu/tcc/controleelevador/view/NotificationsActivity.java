package edu.tcc.controleelevador.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.br.uellisson.controleelevador.R;
import edu.tcc.controleelevador.dados.Util;
import edu.tcc.controleelevador.model.Notify;
import edu.tcc.controleelevador.view.adapter.NotificationsRecyclerView;
import edu.tcc.controleelevador.view.adapter.NotificationsViewHolder;

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

    /**
     *  public void getListNotifications(){
     databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
    listNotifications = new ArrayList<Notify>();
    try{
    for (DataSnapshot child: dataSnapshot.getChildren()) {
    listNotifications.add(child.getValue(Notify.class));
    }
    if (progressBar.getVisibility()==View.VISIBLE){
    progressBar.setVisibility(View.GONE);
    backgroundProgressBar.setBackground(null);
    }
    CallsAdapter callsAdapter = new CallsAdapter(listCalls, getApplicationContext());

    rvUsers.setAdapter(callsAdapter);
    }
    catch (Throwable e){
    Toast.makeText(getApplicationContext(), "Erro ao buscar chamadas!", Toast.LENGTH_LONG ).show();
    progressBar.setVisibility(View.GONE);
    }
    }
    @Override public void onCancelled(DatabaseError error) { }
    });
     }
     */
}
