package com.br.uellisson.controleelevador.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.CallElevator;
import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.br.uellisson.controleelevador.model.User;
import com.br.uellisson.controleelevador.view.adapter.CallsAdapter;
import com.br.uellisson.controleelevador.view.adapter.ReportRecyclerView;
import com.br.uellisson.controleelevador.view.adapter.ReportViewHolder;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportFrequencyActivity extends BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener {
    private TextView firstUse;
    private TextView lastUSe;
    private TextView quantityCall;
    private RadioButton radioButtonT1;
    private RadioButton radioButtonT2;
    private RadioButton radioButton12;
    private RadioButton radioButtonAll;
    private List<CallElevator> listCalls;
    private RelativeLayout backgroundProgressBar;
    private ProgressBar progressBar;
    private  RecyclerView rvUsers;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_frequency);


        backgroundProgressBar = (RelativeLayout) findViewById(R.id.background_progress_bar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        firstUse = (TextView)findViewById(R.id.first_use);
        lastUSe = (TextView)findViewById(R.id.last_use);
        quantityCall = (TextView)findViewById(R.id.quantity_call);
        radioButtonT1 = (RadioButton) findViewById(R.id.radioButtonT1);
        setFilter(radioButtonT1);
        radioButtonT2 = (RadioButton) findViewById(R.id.radioButtonT2);
        setFilter(radioButtonT2);
        radioButton12 = (RadioButton) findViewById(R.id.radioButton12);
        setFilter(radioButton12);
        radioButtonAll = (RadioButton) findViewById(R.id.radioButtonAll);
        setFilter(radioButtonAll);
        databaseReference = Util.getFirebase();
        getListCalls();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_frequency));

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
        callElevator.dataFrequencyUse( this );
        rvUsers = (RecyclerView) findViewById(R.id.rv_frequency);
        rvUsers.setHasFixedSize( true );
        rvUsers.setLayoutManager( new LinearLayoutManager(this));
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
        if (frequencyUseDate!=null){
            firstUse.setText(frequencyUseDate.getFirstUse());
            lastUSe.setText(frequencyUseDate.getLastUse());
            quantityCall.setText(String.valueOf(frequencyUseDate.getQuantityCall()));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setFilter(final RadioButton radioButton){

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<CallElevator> listCallsFilter = new ArrayList<CallElevator>();
                for (int i = 0; i<listCalls.size(); i++){
                    if (radioButton.getText().toString().equalsIgnoreCase("todos")){
                        listCallsFilter.add(listCalls.get(i));
                    }
                    else if (listCalls.get(i).getRoute().replace("0","T").equalsIgnoreCase(radioButton.getText().toString())){
                        listCallsFilter.add(listCalls.get(i));
                    }
                }
                CallsAdapter callsAdapterFilter = new CallsAdapter(listCallsFilter, getApplicationContext());
                rvUsers.setAdapter(callsAdapterFilter);
            }
        });
    }

    public void getListCalls(){
        databaseReference.child("frequency_use").child("calls").addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCalls = new ArrayList<CallElevator>();
                try{
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        listCalls.add(child.getValue(CallElevator.class));
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
}
