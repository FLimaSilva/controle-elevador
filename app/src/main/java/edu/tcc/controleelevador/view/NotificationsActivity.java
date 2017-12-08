package edu.tcc.controleelevador.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import edu.tcc.controleelevador.dados.Util;
import edu.tcc.controleelevador.model.CallElevator;
import edu.tcc.controleelevador.model.Notify;
import edu.tcc.controleelevador.view.adapter.NotificationsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela criação e operação da tela
 * de Notificações do aplicativo
 */
public class NotificationsActivity extends BaseActivity {

    /**
     * Atributo da Classe
     */
    private DatabaseReference databaseReference;
    private List<Notify> listNotifications;
    private ProgressBar progressBar;
    private RecyclerView rvNotifications;

    /**
     * Método onde é criada a tela de Notificações do aplicativo
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        rvNotifications = (RecyclerView) findViewById(R.id.rv_notifications);
        rvNotifications.setHasFixedSize( true );
        rvNotifications.setLayoutManager( new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notifications));

        databaseReference = Util.getFirebase();
        databaseReference.child("notifications");
        getListNotifications();
    }

    public void getListNotifications(){
        databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            listNotifications = new ArrayList<Notify>();
            try{
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    listNotifications.add(child.getValue(Notify.class));
                }
                NotificationsAdapter notificationsAdapter = new NotificationsAdapter(sortListNotificarions(listNotifications), getApplicationContext());
                rvNotifications.setAdapter(notificationsAdapter);
                if (progressBar.getVisibility()==View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
            }
            catch (Throwable e){
                Toast.makeText(getApplicationContext(), "Erro ao buscar chamadas!", Toast.LENGTH_LONG ).show();
                progressBar.setVisibility(View.GONE);
            }
        }
        @Override public void onCancelled(DatabaseError error) { }
        });
    }

    public List<Notify> sortListNotificarions(List<Notify> listNotificationsDb){
        List<Notify> listNotificationsSorted = new ArrayList<Notify>();

        for (int i = listNotificationsDb.size()-1; i >= 0; i--)  {
            listNotificationsSorted.add(listNotificationsDb.get(i));
        }
        return listNotificationsSorted;
    }
}
