package com.br.uellisson.controleelevador.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Constants;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.User;
import com.facebook.CallbackManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Classe responsável pela criação e operação da tela
 * de login do aplicativo
 */
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{
    /**
     * Atributos da Classe
     */
    private User user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;

    /**
     * Método onde é criada a tela de Login do aplicativo
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = getFirebaseAuthResultHandler();
        initViews();
        initUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthListener != null ){
            mAuth.removeAuthStateListener( mAuthListener );
        }
    }

    protected void initViews(){
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.login));
    }

    protected void initUser(){
        user = new User(email.getText().toString(),password.getText().toString());
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler(){
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

                if( userFirebase == null ){
                    return;
                }

                if( user.getId() == null
                        && isNameOk( user, userFirebase ) ){

                    user.setId( userFirebase.getUid() );
                    user.setNameIfNull( userFirebase.getDisplayName() );
                    user.setEmailIfNull( userFirebase.getEmail() );
                    user.saveDB();
                }
                callMainActivity(user.getEmail());
            }
        };

        return( callback );
    }

    private boolean isNameOk( User user, FirebaseUser firebaseUser ){
        return(
                user.getName() != null
                        || firebaseUser.getDisplayName() != null
        );
    }

    public void callSignUp(View view){
        Intent intent = new Intent( this, LoginActivity.class );
        startActivity(intent);
    }

    public void callReset(View view){
        Intent intent = new Intent( this, ResetActivity.class );
        startActivity(intent);
    }

    public void sendLoginData( View view ){
       if (user!=null){
            initUser();
            if (user.getEmail().equals("")||user.getPassword().equals("")){
                Toast.makeText(this, "Preencha todos as informações", Toast.LENGTH_LONG).show();
            }
            else{
                FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
                openProgressBar();
                verifyLogin();
            }
        }
        else {
            Toast.makeText(this, "Preencha todos as informações", Toast.LENGTH_LONG).show();
        }
    }

    private void callMainActivity(String email){
        if (email.equals(Constants.EMAIL_ADMIN)){
            Intent intent = new Intent( this, MainActivityAdmin.class );
            startActivity(intent);
        }
        else {
            Intent intent = new Intent( this, CallElevatorActivity.class );
            startActivity(intent);
        }
        finish();
    }

    private void verifyLogged(){
        if( mAuth.getCurrentUser() != null ){
            callMainActivity(mAuth.getCurrentUser().getEmail());
        }
        else{
            mAuth.addAuthStateListener( mAuthListener );
        }
    }

    private void verifyLogin(){

        FirebaseCrash.log("LoginActivity:verifyLogin()");
        user.saveProviderSP( LoginActivity.this, "" );
        mAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if( !task.isSuccessful() ){
                            showSnackbar("Login falhou");
                            closeProgressBar();
                            return;
                        }
                        else {
                            Util.saveSP(getApplicationContext(), Constants.USER_MAIL, mAuth.getCurrentUser().getEmail());
                            Util.saveSP(getApplicationContext(), Constants.USER_ID, mAuth.getCurrentUser().getUid());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        FirebaseCrash
                .report(
                        new Exception(
                                connectionResult.getErrorCode()+": "+connectionResult.getErrorMessage()
                        )
                );
        showSnackbar( connectionResult.getErrorMessage() );
    }
}
