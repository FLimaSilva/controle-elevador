package edu.tcc.controleelevador.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import edu.tcc.controleelevador.dados.Util;
import edu.tcc.controleelevador.model.CallElevator;
import edu.tcc.controleelevador.model.FrequencyUse;
import edu.tcc.controleelevador.view.adapter.CallsAdapter;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela criação e operação da tela
 * de Relatório de frequênica de uso do aplicativo
 */
public class ReportFrequencyActivity extends BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener {
    /**
     * Atributos da Classe
     */
    private TextView firstUse;
    private TextView lastUSe;
    private TextView quantityCall;
    private RadioButton radioButtonT1;
    private RadioButton radioButtonT2;
    private RadioButton radioButton12;
    private RadioButton radioButtonAll;
    private List<CallElevator> listCalls;
    private RadioButton radioButton1T;
    private RadioButton radioButton2T;
    private RadioButton radioButton21;
    private RelativeLayout backgroundProgressBar;
    private ProgressBar progressBar;
    private  RecyclerView rvUsers;
    private DatabaseReference databaseReference;

    /**
     * Método onde é criada a tela do relatório de frequÊncia de
     * uso do elevador
     * @param savedInstanceState
     */
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
        radioButton1T = (RadioButton) findViewById(R.id.radioButton1T);
        setFilter(radioButton1T);
        radioButton2T = (RadioButton) findViewById(R.id.radioButton2T);
        setFilter(radioButton2T);
        radioButton21 = (RadioButton) findViewById(R.id.radioButton21);
        setFilter(radioButton21);
        databaseReference = Util.getFirebase();
        getListCalls();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_frequency));

        final FrequencyUse frequencyUseContext = new FrequencyUse();
        frequencyUseContext.dataFrequencyUse( this );
        databaseReference.child("frequency_use");
        init();
    }

    /**
     * Método que coloca as setas de voltar na tela
     * @param item
     * @return
     */
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
                if (isChecked){
                    String textRadioButton = radioButton.getText().toString();
                    List<CallElevator> listCallsFilter = new ArrayList<CallElevator>();
                    if (textRadioButton.equals("T-1")||textRadioButton.equals("T-2")||textRadioButton.equals("1-2")||textRadioButton.equals("todos")){
                        radioButton1T.setChecked(false);
                        radioButton2T.setChecked(false);
                        radioButton21.setChecked(false);
                    }
                    else {
                        radioButtonT1.setChecked(false);
                        radioButtonT2.setChecked(false);
                        radioButton12.setChecked(false);
                        radioButtonAll.setChecked(false);
                    }
                    for (int i = 0; i<listCalls.size(); i++){
                        if (textRadioButton.equalsIgnoreCase("todos")){
                            listCallsFilter.add(listCalls.get(i));
                        }
                        else if (listCalls.get(i).getRoute().replace("0","T").equalsIgnoreCase(radioButton.getText().toString())){
                            listCallsFilter.add(listCalls.get(i));
                        }
                    }
                    CallsAdapter callsAdapterFilter = new CallsAdapter(listCallsFilter, getApplicationContext());
                    rvUsers.setAdapter(callsAdapterFilter);
                }
            }
        });
    }

    public void getListCalls(){
        databaseReference.child("frequency_use").child("calls").addListenerForSingleValueEvent(new ValueEventListener() {

            //@RequiresApi(api = Build.VERSION_CODES.M)
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
                    //CallsAdapter callsAdapter = new CallsAdapter(sortListCall(listCalls), getApplicationContext());

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

    public List<CallElevator> sortListCall(List<CallElevator> listCallsDb){
        List<CallElevator> listCallsSorted = new ArrayList<>();
        CallElevator callSortTemp = new CallElevator();

        for (int i = 0; i < listCallsDb.size(); i++)  {
            long dateHour = Long.parseLong(listCallsDb.get(i).getDate().replace("-","").substring(4,8)+listCallsDb.get(i).getDate().replace("-","").substring(2,4)+listCallsDb.get(i).getDate().replace("-","").substring(0,2)+listCallsDb.get(i).getHour().replace(":",""));
            if (listCallsSorted.isEmpty()){
                listCallsSorted.add(listCallsDb.get(i));
            }
            for(int j=0;j<listCallsSorted.size(); j++){
                callSortTemp = listCallsDb.get(j);
                long dateHourJ = Long.parseLong(callSortTemp
                        .getDate().replace("-","").substring(4,8)+
                        callSortTemp.getDate().replace("-","")
                                .substring(2,4)+callSortTemp
                        .getDate().replace("-","").substring(0,2)
                        +callSortTemp.getHour().replace(":",""));

                if (dateHour>dateHourJ){
                    callSortTemp = listCallsDb.get(j-1);
                    listCallsSorted.add(j-1, listCallsDb.get(j));
                    listCallsSorted.add(callSortTemp);
                }
                else {
                    listCallsSorted.add(j, listCallsDb.get(j));
                }

            }
        }
        return listCallsSorted;
    }
}
