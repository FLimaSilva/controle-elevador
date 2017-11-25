package edu.tcc.controleelevador.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;

import edu.tcc.controleelevador.dados.Constants;
import edu.tcc.controleelevador.dados.Util;
import edu.tcc.controleelevador.model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserEditActivity extends BaseActivity implements DatabaseReference.CompletionListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User user;
    private EditText name;
    private Spinner spnUsers;
    private String floorsAllowed;
    private int floorNfc;
    private Integer[] arrayFloors = {0, 0};
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private RadioButton radioButton1Nfc;
    private RadioButton radioButton2Nfc;
    private DatabaseReference databaseReference;
    ArrayList<User> listUsers;
    private String oldPassword;
    private String idUserSelect;
    Button saveEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.edit_user));

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (user != null) {
                    if (firebaseUser == null || user.getId() != null) {
                        return;
                    }

                    user.setId(firebaseUser.getUid());
                    user.saveDB(UserEditActivity.this);
                }
            }
        };

        initViews();
        actionCheckBox();
        databaseReference = Util.getFirebase();
        getListUsers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    protected void initViews() {
        spnUsers = (Spinner) findViewById(R.id.spn_users);
        name = (EditText) findViewById(R.id.et_name);
        name.setVisibility(View.GONE);
        email = (EditText) findViewById(R.id.et_email);
        email.setEnabled(false);
        password = (EditText) findViewById(R.id.et_password);
        //password.setEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
        progressBar.setVisibility(View.VISIBLE);

        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox1.requestFocus();
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        radioButton1Nfc = (RadioButton) findViewById(R.id.radioButton1Nfc);
        radioButton2Nfc = (RadioButton) findViewById(R.id.radioButton2Nfc);
        saveEdit = (Button) findViewById(R.id.bt_register_user);
        saveEdit.setText(R.string.edit_user);
    }

    protected void initUser() {
        floorsAllowed = arrayFloors[0].toString() + arrayFloors[1].toString();//+arrayFloors[2].toString();
        user = new User(name.getText().toString(), email.getText().toString(), password.getText().toString(), floorsAllowed);
        if (radioButton1Nfc.isChecked()) {
            user.setFloorNfc(1);
        } else {
            user.setFloorNfc(2);
        }
        user.setId(idUserSelect);
    }

    public void sendSignUpData(View view) {
        initUser();
        if (user != null) {
            if (user.getEmail().equals("") || user.getPassword().equals("")) {
                Toast.makeText(this, "Preencha todos as informações", Toast.LENGTH_LONG).show();
            } else {
                if (password.getText().toString().equals(oldPassword)){
                    user.saveUserEdit(idUserSelect, UserEditActivity.this);
                }
                else {
                    myAuth(user.getEmail(), oldPassword);
                }
                progressBar.setVisibility(View.VISIBLE);
                Log.i("setPassword", "Senha atualizada com sucesso");

            }
        } else {
            Toast.makeText(this, "Preencha todos as informações", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        myAuth(Util.getSP(getApplicationContext(), Constants.USER_MAIL), Util.getSP(getApplicationContext(), Constants.USER_PASSWORD));
        Toast.makeText(getApplicationContext(), "Usuário editado com sucesso!", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        finish();
    }

    public void actionSpinner(final ArrayList<User> listUsers) {
        spnUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name.setText(listUsers.get(position).getName());
                email.setText(listUsers.get(position).getEmail());
                password.setText(listUsers.get(position).getPassword());
                oldPassword = listUsers.get(position).getPassword();
                floorNfc = listUsers.get(position).getFloorNfc();
                setFloor(listUsers.get(position).getFloorsAllowed());
                idUserSelect = listUsers.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getListUsers() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsers = new ArrayList<User>();
                List<String> listNamesUsers = new ArrayList<String>();
                try {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        listUsers.add(child.getValue(User.class));
                        listNamesUsers.add(child.getValue(User.class).getName());
                    }
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                        //backgroundProgressBar.setBackground(null);
                    }
                    ArrayAdapter<String> petsAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, listNamesUsers) {
                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            // Set the Text color
                            tv.setTextColor(getResources().getColor(R.color.colorBlack));
                            return view;
                        }
                    };
                    spnUsers.setAdapter(petsAdapter);
                    actionSpinner(listUsers);
                } catch (Throwable e) {
                    Toast.makeText(getApplicationContext(), "Erro ao buscar dados do usuário!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void setFloor(String floorAllowed) {

        if (floorAllowed.equals("10")) {
            checkBox1.setChecked(true);
            checkBox2.setChecked(false);
            radioButton1Nfc.setChecked(true);
        } else if (floorAllowed.equals("02")) {
            checkBox1.setChecked(false);
            checkBox2.setChecked(true);
            radioButton2Nfc.setChecked(true);
        } else if (floorAllowed.equals("12")) {
            checkBox1.setChecked(true);
            checkBox2.setChecked(true);
            radioButton1Nfc.setChecked(true);
        }
        if (floorNfc == 1) {
            radioButton1Nfc.setChecked(true);
        } else if (floorNfc == 2) {
            radioButton2Nfc.setChecked(true);
        }
    }

    public void actionCheckBox() {
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrayFloors[0] = 1;
                } else {
                    arrayFloors[0] = 0;
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrayFloors[1] = 2;
                } else {
                    arrayFloors[1] = 0;
                }
            }
        });
    }

    public void editMailPassword(String oldPassword, final String newPassword) {
        final FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user2.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

        user2.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user2.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.saveUserEdit(idUserSelect, UserEditActivity.this);
                                //Toast.makeText(getApplicationContext(), "Senha editada com sucesso!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Erro ao mudar a senha do usuário!", Toast.LENGTH_LONG).show();
                                myAuth(Util.getSP(getApplicationContext(), Constants.USER_MAIL), Util.getSP(getApplicationContext(), Constants.USER_PASSWORD));
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "falha de autenticação!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void myAuth(final String mail, final String oldPassword) {
        final String newPassword = password.getText().toString();
        FirebaseAuth.getInstance().signOut();
        mAuth.signInWithEmailAndPassword(
                mail, oldPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "falha de autenticação!", Toast.LENGTH_LONG).show();
                            closeProgressBar();
                            return;
                        } else {
                            if (!mail.equals(Constants.EMAIL_ADMIN)){
                                editMailPassword(oldPassword, newPassword);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report(e);
            }
        });

    }
}