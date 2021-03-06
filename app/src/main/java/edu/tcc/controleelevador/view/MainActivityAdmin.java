package edu.tcc.controleelevador.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.br.uellisson.controleelevador.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Classe responsável pela criação e operação da tela
 * de principal do administador do aplicativo
 */
public class MainActivityAdmin extends BaseActivity {

    /**
     * Atributos da Classe
     */
    private Button btRegisterUser;
    private Button btExit;

    /**
     * Método onde é criada a tela principal do
     * Administrador do aplicativo
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegisterUser = (Button)findViewById(R.id.bt_register_user);
        btExit = (Button) findViewById(R.id.bt_exit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.manager_elevator));

    }

    public void callElevator(View view){
        Intent intent = new Intent(getApplicationContext(), CallElevatorActivity.class);
        startActivity(intent);
        //finish();
    }

    public void registerUser(View view){
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }

    public void editUser(View view){
        Intent intent = new Intent(getApplicationContext(), UserEditActivity.class);
        startActivity(intent);
    }

    public void clickRegisterNetworks(View view){
        Intent intent = new Intent(getApplicationContext(), NetworksActivity.class);
        startActivity(intent);
    }

    public void clickReportLsLd(View view){
        Intent intent = new Intent(getApplicationContext(), ReportLsLd.class);
        startActivity(intent);
    }

    public void clickReportFrequency(View view){
        Intent intent = new Intent(getApplicationContext(), ReportFrequencyActivity.class);
        startActivity(intent);
    }

    public void clickNotification(View view){
        Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
        startActivity(intent);
    }

    public void clickRecordNfc(View view){
        Intent intent = new Intent(getApplicationContext(), RecordCardNfcActivity.class);
        startActivity(intent);
    }

    public void exitApp(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
