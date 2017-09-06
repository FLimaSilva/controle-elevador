package com.br.uellisson.controleelevador.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.br.uellisson.controleelevador.view.adapter.ReportRecyclerView;
import com.br.uellisson.controleelevador.view.adapter.ReportViewHolder;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReportFrequencyActivity extends BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_frequency);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_frequency));

        databaseReference = Util.getFirebase();

        FrequencyUse frequencyUse = new FrequencyUse();
        frequencyUse.contextDataDB( this );
        frequencyUse.updateDB( ReportFrequencyActivity.this );
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
        RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rv_frequency);
        rvUsers.setHasFixedSize( true );
        rvUsers.setLayoutManager( new LinearLayoutManager(this));
        ReportRecyclerView adapter = new ReportRecyclerView(
                FrequencyUse.class,
                R.layout.item_report,
                ReportViewHolder.class,
                databaseReference.child("frequency_use") );

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

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
