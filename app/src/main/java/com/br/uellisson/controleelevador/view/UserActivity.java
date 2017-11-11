package com.br.uellisson.controleelevador.view;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela criação e operação da tela
 * de Cadastro de usuário do aplicativo
 */
public class UserActivity extends BaseActivity implements DatabaseReference.CompletionListener{
    /**
     * Atributos da Classe
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User user;
    private EditText name;
    private Spinner spnUsers;
    private String floorsAllowed;
    private Integer[] arrayFloors = {0, 0};
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;

    /**
     * Método onde é criada a tela de cadatro e edição do usuário
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.register_user));

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(user!=null){
                    if( firebaseUser == null || user.getId() != null ){
                        return;
                    }

                    user.setId( firebaseUser.getUid() );
                    user.saveDB(UserActivity.this);
                }
            }
        };

        initViews();
        ArrayList<User> listUsers = new ArrayList<>();
        actionSpinner(listUsers);
        actionCheckBox();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthStateListener != null ){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    protected void initViews(){
        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
        spnUsers = (Spinner) findViewById(R.id.spn_users);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
    }

    protected void initUser(){
        floorsAllowed = arrayFloors[0].toString()+arrayFloors[1].toString();//+arrayFloors[2].toString();
        user = new User(name.getText().toString(), email.getText().toString(),password.getText().toString(), floorsAllowed);
    }

    public void sendSignUpData( View view ){
        initUser();
        if (user!=null){
            if (user.getEmail().equals("") || user.getPassword().equals("")){
                Toast.makeText(this, "Preencha todos as informações", Toast.LENGTH_LONG).show();
            }
            else {
                openProgressBar();
                saveUser();
            }
        }
        else {
            Toast.makeText(this, "Preencha todos as informações", Toast.LENGTH_LONG).show();
        }
    }

    private void saveUser(){

        mAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( !task.isSuccessful() ){
                    closeProgressBar();
                }
            }
        }
        ).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                showSnackbar(getString(R.string.error_mail_password));
            }
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mAuth.signOut();
        Toast.makeText(this,"Conta criada com sucesso!",Toast.LENGTH_LONG).show();
        closeProgressBar();
        finish();
    }

    public void actionSpinner(final ArrayList<User> listUsers) {
        spnUsers.setAdapter(populateAdapter(listUsers));
        spnUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                name.setText(listUsers.get(position).getName());
                email.setText(listUsers.get(position).getEmail());
                password.setText(listUsers.get(position).getPassword());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public ArrayAdapter<String> populateAdapter(ArrayList<User> listUsers){
        final ArrayList<String> listUserNames = new ArrayList<>();
        ArrayAdapter<String> adapter;
        for (int i = 0; i<listUsers.size(); i++){
            listUserNames.add(i, listUsers.get(i).getName());
        }

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, listUserNames);

        return adapter;
    }

    public void actionCheckBox(){
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    arrayFloors[0] = 1;
                }
                else {
                    arrayFloors[0] = 0;
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    arrayFloors[1] = 2;
                }
                else {
                    arrayFloors[1] = 0;
                }
            }
        });

        /**
         checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    arrayFloors[2] = 3;
                }
                else {
                    arrayFloors[2] = 0;
                }
            }
        });
     *
     */
        }
    }
