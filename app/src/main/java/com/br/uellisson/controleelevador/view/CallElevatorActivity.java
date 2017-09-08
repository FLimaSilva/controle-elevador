package com.br.uellisson.controleelevador.view;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Constants;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.CallElevator;
import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.br.uellisson.controleelevador.model.User;
import com.br.uellisson.controleelevador.model.UserUI;
import com.br.uellisson.controleelevador.view.adapter.UserRecyclerAdapter;
import com.br.uellisson.controleelevador.view.adapter.UserViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CallElevatorActivity extends BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener{

    private ImageView ivUp;
    private ImageView ivDown;
    private ImageView ivElevator;
    private CheckedTextView checkDestinationT;
    private CheckedTextView checkDestination1;
    private CheckedTextView checkDestination2;
    private CheckedTextView checkDestination3;
    private CheckedTextView checkOriginT;
    private CheckedTextView checkOrigin1;
    private CheckedTextView checkOrigin2;
    private CheckedTextView checkOrigin3;
    int origin;
    int destination;
    int quantityCall;

    private UserRecyclerAdapter adapterRecycle;
    private DatabaseReference databaseReference;
    final User user = new User();
    private CallElevator callElevator;
    private FrequencyUse frequencyUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_elevator);

        //mToolbar = (Toolbar) findViewById(R.id.toolbar_custom);
       // mToolbar.setTitle("Uellisson");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.call_elevator));

        ivUp = (ImageView)findViewById(R.id.iv_up);
        ivDown = (ImageView)findViewById(R.id.iv_down);
        ivElevator = (ImageView) findViewById(R.id.iv_elevator);

        checkOriginT = (CheckedTextView) findViewById(R.id.check_origin_t);
        checkOrigin1 = (CheckedTextView) findViewById(R.id.check_origin_1);
        checkOrigin2 = (CheckedTextView) findViewById(R.id.check_origin_2);
        checkOrigin3 = (CheckedTextView) findViewById(R.id.check_origin_3);
        enableOrigin();

        checkDestinationT = (CheckedTextView) findViewById(R.id.check_destination_t);
        checkDestination1 = (CheckedTextView) findViewById(R.id.check_destination_1);
        checkDestination2 = (CheckedTextView) findViewById(R.id.check_destination_2);
        checkDestination3 = (CheckedTextView) findViewById(R.id.check_destination_3);
        enableDestination();

        enableArrow(ivUp);

        databaseReference = Util.getFirebase();
        final FrequencyUse frequencyUseContext = new FrequencyUse();
        frequencyUseContext.dataFrequencyUse( this );
        databaseReference.child("frequency_use");
    }

    public void exitApp(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void enableOrigin(){
        checkOriginT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOriginT.isChecked()){
                    checkOriginT.setChecked(false);
                }
                else {
                    unCheckedOrigin();
                    checkOriginT.setChecked(true);
                    origin = 0;
                }
            }
        });
        checkOrigin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrigin1.isChecked()){
                    checkOrigin1.setChecked(false);
                }
                else {
                    unCheckedOrigin();
                    checkOrigin1.setChecked(true);
                    origin = 1;
                }
            }
        });
        checkOrigin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrigin2.isChecked()){
                    checkOrigin2.setChecked(false);
                }
                else {
                    unCheckedOrigin();
                    checkOrigin2.setChecked(true);
                    origin = 2;
                }
            }
        });
        checkOrigin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrigin3.isChecked()){
                    checkOrigin3.setChecked(false);
                }
                else {
                    unCheckedOrigin();
                    checkOrigin3.setChecked(true);
                    origin = 3;
                }
            }
        });
    }

    public void unCheckedOrigin(){
        checkOriginT.setChecked(false);
        checkOrigin1.setChecked(false);
        checkOrigin2.setChecked(false);
        checkOrigin3.setChecked(false);
    }

    public void enableDestination(){
        checkDestinationT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDestinationT.isChecked()){
                    checkDestinationT.setChecked(false);
                }
                else {
                    unCheckedDestination();
                    checkDestinationT.setChecked(true);
                    destination = 0;
                }
            }
        });
        checkDestination1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDestination1.isChecked()){
                    checkDestination1.setChecked(false);
                }
                else {
                    unCheckedDestination();
                    checkDestination1.setChecked(true);
                    destination = 1;
                }
            }
        });
        checkDestination2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDestination2.isChecked()){
                    checkDestination2.setChecked(false);
                }
                else {
                    unCheckedDestination();
                    checkDestination2.setChecked(true);
                    destination = 2;
                }
            }
        });
        checkDestination3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDestination3.isChecked()){
                    checkDestination3.setChecked(false);
                }
                else {
                    unCheckedDestination();
                    checkDestination3.setChecked(true);
                    origin = 3;
                }
            }
        });
    }

    public void unCheckedDestination(){
        checkDestinationT.setChecked(false);
        checkDestination1.setChecked(false);
        checkDestination2.setChecked(false);
        checkDestination3.setChecked(false);
    }

    public void enableArrow(ImageView imageView){
        if (imageView==ivUp){
            ivUp.setEnabled(true);
            ivDown.setEnabled(false);
        }
        else if (imageView==ivDown){
            ivDown.setEnabled(true);
            ivUp.setEnabled(false);
        }
    }

    public void callElevator(View view){
        if (origin<destination){
            ivUp.setEnabled(true);
            ivDown.setEnabled(false);
        }
        else if(origin>destination){
            ivDown.setEnabled(true);
            ivUp.setEnabled(false);
        }
        ivElevator.setImageResource(R.mipmap.elevator_close);
        Toast.makeText(this, "O elevador está vindo", Toast.LENGTH_SHORT).show();
        saveCall();
        view.setEnabled(false);
    }

    private void initFrequencyUse(){
        frequencyUse = new FrequencyUse();
        frequencyUse.setFirstUse("01-07-2017");
        frequencyUse.setLastUse("10-09-2017");

        callElevator = new CallElevator();
        callElevator.setRoute(String.valueOf(origin)+"-"+String.valueOf(destination));
        String currentUser = Util.getSP(getApplicationContext(), Constants.USER_NAME);
        callElevator.setUserName(currentUser);
        getDates();
    }

    private void saveCall(){
        initFrequencyUse();

        frequencyUse.setQuantityCall(quantityCall+1);
        frequencyUse.updateFrequencyCall(CallElevatorActivity.this);
        callElevator.saveCall("call_"+String.valueOf(quantityCall+1), CallElevatorActivity.this);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
       // showToast( "Chamada salva com sucesso!" );
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FrequencyUse frequencyUseDate = dataSnapshot.getValue(FrequencyUse.class);
        quantityCall = frequencyUseDate.getQuantityCall();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void getDates(){
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String dd = String.valueOf(day);
        String mm = String.valueOf(month);
        String yy = String.valueOf(year);

        if (day<10){
            dd = "0"+dd;
        }
        if (month<10){
            mm = "0"+mm;
        }

        String ddMMaa = dd+"-"+mm+"-"+yy;
        callElevator.setDate(ddMMaa);
        frequencyUse.setLastUse(ddMMaa);

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        String hourString = "" + h;
        if (h < 10){
            hourString = "0" + hourString;
        }
        String minuteString = "" + m;
        if (m < 10){
            minuteString = "0" + minuteString;
        }
        callElevator.setHour(hourString+":"+minuteString);
    }
}

