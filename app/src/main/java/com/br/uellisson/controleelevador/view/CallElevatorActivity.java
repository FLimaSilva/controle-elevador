package com.br.uellisson.controleelevador.view;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.dados.Constants;
import com.br.uellisson.controleelevador.dados.Util;
import com.br.uellisson.controleelevador.model.CallElevator;
import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.br.uellisson.controleelevador.model.User;
import com.br.uellisson.controleelevador.presenter.CallPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private Button btCallElevator;
    int origin;
    int destination;
    int quantityCall;
    String floorAllowed;

    private DatabaseReference databaseReference;
    private CallElevator callElevator;
    private FrequencyUse frequencyUse;
    CallPresenter callPresenter;

    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private List<Tag> mTags = new ArrayList<Tag>();
    private NfcAdapter mAdapter;
    private String nfcRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_call_elevator);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.call_elevator));
        initViews();
        initClass();
        initNfc();
        resolveIntent(getIntent());
        enableArrow(ivUp);
        getAcessFloor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    public void exitApp(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void enableOrigin(int floorAllowed){
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
        if (floorAllowed != 0){
            if (floorAllowed == 1){
                checkOrigin1.setVisibility(View.VISIBLE);
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
            }else if (floorAllowed == 2){
                checkOrigin2.setVisibility(View.VISIBLE);
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
            }
        }
    }

    public void unCheckedOrigin(){
        checkOriginT.setChecked(false);
        checkOrigin1.setChecked(false);
        checkOrigin2.setChecked(false);
        checkOrigin3.setChecked(false);
    }

    public void enableDestination(int floorAllowed){
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

        if (floorAllowed != 0){
            if (floorAllowed == 1){
                checkDestination1.setVisibility(View.VISIBLE);
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
            }
            else if (floorAllowed == 2){
                checkDestination2.setVisibility(View.VISIBLE);
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
            }
        }
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
        Toast.makeText(this, getString(R.string.msg_elevator), Toast.LENGTH_SHORT).show();
        saveCall();
        view.setEnabled(false);
    }

    private void initFrequencyUse(){
        frequencyUse = new FrequencyUse();
        frequencyUse.setFirstUse("01-07-2017");

        callElevator = new CallElevator();
        callElevator.setRoute(String.valueOf(origin)+"-"+String.valueOf(destination));
        String currentMail = Util.getSP(getApplicationContext(), Constants.USER_MAIL);
        callElevator.setUserName(currentMail);
        setDateHour();
    }

    private void saveCall(){
        initFrequencyUse();
        String qCallString = String.valueOf(quantityCall+1);
        if (quantityCall<9){
            qCallString = "0"+qCallString;
        }
        frequencyUse.setQuantityCall(Integer.parseInt(qCallString));
        frequencyUse.updateFrequencyCall(CallElevatorActivity.this);
        callElevator.saveCall("call_"+qCallString, CallElevatorActivity.this);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
       // showToast( "Chamada salva com sucesso!" );
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FrequencyUse frequencyUseDate = dataSnapshot.getValue(FrequencyUse.class);
        if (frequencyUseDate.getQuantityCall()!=0){
            quantityCall = frequencyUseDate.getQuantityCall();
        }
        User currentUser = dataSnapshot.getValue(User.class);
        this.floorAllowed = currentUser.getFloorsAllowed();
        //Toast.makeText(this, floorAllowed, Toast.LENGTH_SHORT).show();
        if (floorAllowed!=null){
            manageAcessFloor(floorAllowed);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void setDateHour(){

        callElevator.setDate(callPresenter.getDate());
        frequencyUse.setLastUse(callPresenter.getDate());
        callElevator.setHour(callPresenter.getHour());
    }

    public void getAcessFloor(){
        String idUser = Util.getSP(getApplicationContext(), Constants.USER_ID);

        databaseReference.child("users").child(idUser);
        final User user = new User();
        user.dataUser( this );

    }

    public void manageAcessFloor(String acessFloor){
        for (int i = 0; i<acessFloor.length(); i++){
            if (!acessFloor.substring(i, i+1).equals("0")){
                enableOrigin(Integer.parseInt(acessFloor.substring(i, i+1)));
                enableDestination(Integer.parseInt(acessFloor.substring(i, i+1)));
            }
        }
    }

    private void setBackgroundCheckedTextView(CheckedTextView checkedTextView, int drawable){
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            checkedTextView.setBackground(getResources().getDrawable(drawable));
        else
            checkedTextView.setBackground(ContextCompat.getDrawable(this, drawable));
    }
    private void setBackgroundImageView(ImageView imageView, int drawable){
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            imageView.setBackground(getResources().getDrawable(drawable));
        else
            imageView.setBackground(ContextCompat.getDrawable(this, drawable));
    }

    public void initViews(){
        ivUp = (ImageView)findViewById(R.id.iv_up);
        setBackgroundImageView(ivUp, R.drawable.arrow_up_selector);
        ivDown = (ImageView)findViewById(R.id.iv_down);
        setBackgroundImageView(ivDown, R.drawable.arrow_down_selector);
        ivElevator = (ImageView) findViewById(R.id.iv_elevator);
        btCallElevator = (Button) findViewById(R.id.bt_call_elevator);

        checkOriginT = (CheckedTextView) findViewById(R.id.check_origin_t);
        checkOrigin1 = (CheckedTextView) findViewById(R.id.check_origin_1);
        checkOrigin2 = (CheckedTextView) findViewById(R.id.check_origin_2);
        checkOrigin3 = (CheckedTextView) findViewById(R.id.check_origin_3);
        setBackgroundCheckedTextView(checkOriginT, R.drawable.cicle_selector);
        setBackgroundCheckedTextView(checkOrigin1, R.drawable.cicle_selector);
        setBackgroundCheckedTextView(checkOrigin2, R.drawable.cicle_selector);
        setBackgroundCheckedTextView(checkOrigin3, R.drawable.cicle_selector);

        checkDestinationT = (CheckedTextView) findViewById(R.id.check_destination_t);
        checkDestination1 = (CheckedTextView) findViewById(R.id.check_destination_1);
        checkDestination2 = (CheckedTextView) findViewById(R.id.check_destination_2);
        checkDestination3 = (CheckedTextView) findViewById(R.id.check_destination_3);
        setBackgroundCheckedTextView(checkDestinationT, R.drawable.cicle_selector);
        setBackgroundCheckedTextView(checkDestination1, R.drawable.cicle_selector);
        setBackgroundCheckedTextView(checkDestination2, R.drawable.cicle_selector);
        setBackgroundCheckedTextView(checkDestination3, R.drawable.cicle_selector);
    }

    public void initClass(){
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        databaseReference = Util.getFirebase();
        final FrequencyUse frequencyUseContext = new FrequencyUse();
        frequencyUseContext.dataFrequencyUse( this );
        databaseReference.child("frequency_use");
        callPresenter = new CallPresenter();
    }

    public void initNfc() {
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
                Toast.makeText(this, msgs.toString(), Toast.LENGTH_SHORT).show();
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
                mTags.add(tag);
            }
        }
    }

    private String dumpTagData(Tag tag) {
        String id = String.valueOf(toDec(tag.getId()));
        //Toast.makeText(this,id, Toast.LENGTH_LONG).show();
        if (id.equals("2089877081")){
            if (btCallElevator.isEnabled()){
                callElevator(btCallElevator);
            }
            else{
                Toast.makeText(this, getString(R.string.msg_elevator), Toast.LENGTH_SHORT).show();
            }
        }

        return id;
    }
    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
}