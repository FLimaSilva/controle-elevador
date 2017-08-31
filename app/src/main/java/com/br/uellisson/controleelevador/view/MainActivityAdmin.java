package com.br.uellisson.controleelevador.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.br.uellisson.controleelevador.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityAdmin extends BaseActivity {

    private Button btRegisterUser;
    private Button btExit;

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

    }

    public void deleteUser(View view){

    }

    public void clickReportLsLd(View view){
        Intent intent = new Intent(getApplicationContext(), ReportLsLd.class);
        startActivity(intent);
    }

    public void clickReportFrequency(View view){
        Intent intent = new Intent(getApplicationContext(), ReportFrequency.class);
        startActivity(intent);
    }

    public void clickNotification(View view){
        Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
        startActivity(intent);
    }

    public void exitApp(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
