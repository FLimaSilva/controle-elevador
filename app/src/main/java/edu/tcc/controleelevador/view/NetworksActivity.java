package edu.tcc.controleelevador.view;

import android.net.Network;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.tcc.controleelevador.dados.Util;
import edu.tcc.controleelevador.model.MyNetwork;
import edu.tcc.controleelevador.model.User;

public class NetworksActivity extends BaseActivity implements DatabaseReference.CompletionListener {

    private ProgressBar progressBarNetwork;
    private EditText etNet1Name;
    private EditText etNet1password;
    private EditText etNet2Name;
    private EditText etNet2password;
    private EditText etNet3Name;
    private EditText etNet3password;
    private MyNetwork myNetwork;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks);

        progressBarNetwork = (ProgressBar) findViewById(R.id.progressBarNetwork);
        etNet1Name = (EditText)findViewById(R.id.et_name1);
        etNet1password = (EditText)findViewById(R.id.et_password1);
        etNet2Name = (EditText)findViewById(R.id.et_name2);
        etNet2password = (EditText)findViewById(R.id.et_password2);
        etNet3Name = (EditText)findViewById(R.id.et_name3);
        etNet3password = (EditText)findViewById(R.id.et_password3);
        databaseReference = Util.getFirebase();
        getNetworks();
    }

    public void initNetWork(){
        myNetwork = new MyNetwork(etNet1Name.getText().toString(),
            etNet1password.getText().toString(),
            etNet2Name.getText().toString(),
            etNet2password.getText().toString(),
            etNet3Name.getText().toString(),
            etNet3password.getText().toString());
    }

    public void clickSaveNetworks(View view){
        progressBarNetwork.setVisibility(View.VISIBLE);
        initNetWork();
        myNetwork.saveNetworks(this);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        Toast.makeText(this, getString(R.string.msg_register_networks), Toast.LENGTH_LONG).show();
        progressBarNetwork.setVisibility(View.GONE);
        finish();
    }

    public void getNetworks() {
        databaseReference.child("networks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myNetwork = new MyNetwork();
                try {
                    myNetwork = dataSnapshot.getValue(MyNetwork.class);
                    setValuesNetwork(myNetwork);
                    if (progressBarNetwork.getVisibility() == View.VISIBLE) {
                        progressBarNetwork.setVisibility(View.GONE);
                    }
                } catch (Throwable e) {
                    //Toast.makeText(getApplicationContext(), "Erro ao buscar dados da rede!", Toast.LENGTH_LONG).show();
                    progressBarNetwork.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void setValuesNetwork(MyNetwork myNetworkDb){
        etNet1Name.setText(myNetworkDb.getNet1Name());
        etNet1password.setText(myNetworkDb.getNet1password());
        etNet2Name.setText(myNetworkDb.getNet2Name());
        etNet2password.setText(myNetworkDb.getNet2password());
        etNet3Name.setText(myNetworkDb.getNet3Name());
        etNet3password.setText(myNetworkDb.getNet3password());
    }
}
