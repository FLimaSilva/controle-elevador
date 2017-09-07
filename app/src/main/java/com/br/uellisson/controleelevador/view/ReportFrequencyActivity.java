package com.br.uellisson.controleelevador.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.CallElevator;
import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.br.uellisson.controleelevador.model.User;
import com.br.uellisson.controleelevador.view.adapter.ReportRecyclerView;
import com.br.uellisson.controleelevador.view.adapter.ReportViewHolder;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReportFrequencyActivity extends BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener {
    private TextView firstUse;
    private TextView lastUSe;
    private TextView quantityCall;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_frequency);

        firstUse = (TextView)findViewById(R.id.first_use);
        lastUSe = (TextView)findViewById(R.id.last_use);
        quantityCall = (TextView)findViewById(R.id.quantity_call);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_frequency));

        databaseReference = Util.getFirebase();

        final FrequencyUse frequencyUseContext = new FrequencyUse();
        frequencyUseContext.dataFrequencyUse( this );
        databaseReference.child("frequency_use");
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
    private void init(){
        final CallElevator callElevator = new CallElevator();
        callElevator.dataCallElevatorUpdated( this );
        RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rv_frequency);
        rvUsers.setHasFixedSize( true );
        rvUsers.setLayoutManager( new LinearLayoutManager(this));
        ReportRecyclerView adapter = new ReportRecyclerView(
                CallElevator.class,
                R.layout.item_report,
                ReportViewHolder.class,
                databaseReference.child("frequency_use").child("calls") );

        rvUsers.setAdapter(adapter);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if( databaseError != null ){
            FirebaseCrash.report( databaseError.toException() );
            Toast.makeText( this, "Falhou: "+databaseError.getMessage(), Toast.LENGTH_LONG ).show();
        }
        else{
            Toast.makeText( this, "Atualização realizada com sucesso.", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FrequencyUse frequencyUseDate = dataSnapshot.getValue(FrequencyUse.class);
        firstUse.setText(frequencyUseDate.getFirstUse());
        lastUSe.setText(frequencyUseDate.getLastUse());
        quantityCall.setText(String.valueOf(frequencyUseDate.getQuantityCall()));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
