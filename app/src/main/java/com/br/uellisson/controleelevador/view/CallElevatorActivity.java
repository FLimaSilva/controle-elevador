package com.br.uellisson.controleelevador.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.google.firebase.auth.FirebaseAuth;

public class CallElevatorActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_elevator);

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
    }
}

