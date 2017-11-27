package edu.tcc.controleelevador.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;
import edu.tcc.controleelevador.dados.Constants;
import edu.tcc.controleelevador.dados.Util;
import edu.tcc.controleelevador.model.CallElevator;
import edu.tcc.controleelevador.model.FrequencyUse;
import edu.tcc.controleelevador.model.Notify;
import edu.tcc.controleelevador.model.User;
import edu.tcc.controleelevador.presenter.CallPresenter;
import edu.tcc.controleelevador.view.adapter.CallsAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Classe responsável pela criação e operação da tela
 * de chamadas do elevador.
 */
public class CallElevatorActivity extends BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener{

    /**
     * Atributos da Classe
     */
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
    private TextView tvFloorNfc;
    private Button btCallElevator;
    private int origin;
    private int destination;
    private int quantityCall;
    private String floorAllowed;
    private long currentFloor;
    private int nextFloor;
    private long ld;
    private long ls;
    private int qtdNotifications;
    private int openPortPrev;

    private DatabaseReference databaseReference;
    private CallElevator callElevator;
    private FrequencyUse frequencyUse;
    private CallPresenter callPresenter;

    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private List<Tag> mTags = new ArrayList<Tag>();
    private NfcAdapter mAdapter;
    private String idNfc;
    private ProgressBar progressBar;
    private RelativeLayout backgroundProgressBar;
    private TextView tvCurrentFloor;
    private long openPort;
    private int floorNfc;

    /**
     * Método onde é criada a tela de Chamadas do elevador
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_call_elevator);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.call_elevator));
        initViews();
        initClass();

        enableArrow(ivUp);
        getAcessFloor();
        getIdNfc();
        getCurrentFoor();
        getOpenPort();
        initNfc();
        resolveIntent(getIntent());
    }

    /**
     * Método que é acionado sempre que o usuário acessa a tela
     * de chamdas do elevador
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            tvFloorNfc.setVisibility(View.VISIBLE);
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    /**
     * método que chama o verificação dos nfcs
     * proximos do celular
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    /**
     * Médodo para o usuário sair do aplicativo
     * @param view
     */
    public void exitApp(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método que ativa os andares de origem do liberados
     * para o usuário
     * @param floorAllowed
     */
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

    /**
     * Método que muda os andares de origem, que o usuário não tem acesso,
     * para indisponiveis
     */
    public void unCheckedOrigin(){
        checkOriginT.setChecked(false);
        checkOrigin1.setChecked(false);
        checkOrigin2.setChecked(false);
        checkOrigin3.setChecked(false);
    }

   /**
     * Método que ativa os andares de destino do liberados
     * para o usuário
     * @param floorAllowed
     */
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

    /**
     * Método que muda os andares de destino, que o usuário não tem acesso,
     * para indisponiveis
     */
    public void unCheckedDestination(){
        checkDestinationT.setChecked(false);
        checkDestination1.setChecked(false);
        checkDestination2.setChecked(false);
        checkDestination3.setChecked(false);
    }

    /**
     * Método que ativa e muda a direção das setas
      * @param imageView
     */
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

    /**
     * Método que chama o elevador
     * @param view
     */
    public void callElevator(View view){
        if (origin==destination){
            Toast.makeText(this, getString(R.string.msg_equal_floor), Toast.LENGTH_SHORT).show();
        }
        else {
            saveCall();
            view.setEnabled(false);
        }
    }

    /**
     * Mátodo que inicializa os dados da chamada do elevador,
     * antes de salvar a chamada
     */
    private void initCallElevator(){
        frequencyUse = new FrequencyUse();
        if (quantityCall==0){
            DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
            String today = dateFormat.format(new Date(System.currentTimeMillis()));
            frequencyUse.setFirstUse(today);
        }

        callElevator = new CallElevator();
        callElevator.setRoute(String.valueOf(origin)+"-"+String.valueOf(destination));
        String currentMail = Util.getSP(getApplicationContext(), Constants.USER_MAIL);
        callElevator.setUserName(currentMail);
        setDateHour();
    }

    /**
     * ´Método que salva a chamada do elevador
     */
    private void saveCall(){
        initCallElevator();
        String qCallString = String.valueOf(quantityCall+1);
        if (quantityCall<9){
            qCallString = "0"+qCallString;
        }
        frequencyUse.setQuantityCall(Integer.parseInt(qCallString));
        frequencyUse.updateFrequencyCall(CallElevatorActivity.this);
        callElevator.saveCall("call_"+qCallString, CallElevatorActivity.this);
        if (currentFloor==origin){
            saveNextFloor(destination+10);
            nextFloor = destination+10;
        }
        else {
            saveNextFloor(origin+10);
            nextFloor = origin+10;
        }
        Toast.makeText(this, getString(R.string.msg_elevator), Toast.LENGTH_SHORT).show();
    }

    /**
     * Método que é executado quando os dados da chamada do
     * elevador é salva no banco de dados
     * @param databaseError
     * @param databaseReference
     */
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
       // showToast( "Chamada salva com sucesso!" );
    }

    /**
     * Método usado para pegar dados do banco
     * necessário para a chamada do elevador
     * @param dataSnapshot
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        FrequencyUse frequencyUseDate = dataSnapshot.getValue(FrequencyUse.class);
        if (frequencyUseDate!=null){
            if (frequencyUseDate.getQuantityCall()>0){
                quantityCall = frequencyUseDate.getQuantityCall();
            }
        }
        User currentUser = dataSnapshot.getValue(User.class);

        if (currentUser!=null){
            this.floorAllowed = currentUser.getFloorsAllowed();
            if (currentUser.getFloorNfc()!=0){
                this.floorNfc = currentUser.getFloorNfc();
            }
            tvFloorNfc.setText(getString(R.string.msg_floor_nfc, String.valueOf(floorNfc)));
        }
        //Toast.makeText(this, floorAllowed, Toast.LENGTH_SHORT).show();
        if (floorAllowed!=null){
            btCallElevator.setEnabled(true);
            manageAcessFloor(floorAllowed);
            if (progressBar.getVisibility()==View.VISIBLE){
                progressBar.setVisibility(View.GONE);
                backgroundProgressBar.setBackground(null);
            }
        }
    }

    /**
     * Método chamada quando a conexão com o banco é cancelada,
     * antes de pegar os dados
     * @param databaseError
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    /**
     * Método que formata a data de chamada do elevador
     */
    public void setDateHour(){
        callElevator.setDate(callPresenter.getDate());
        frequencyUse.setLastUse(callPresenter.getDate());
        callElevator.setHour(callPresenter.getHour());
    }

    /**
     * Método que pega os andares que o usuário tem acesso
     */
    public void getAcessFloor(){
        String idUser = Util.getSP(getApplicationContext(), Constants.USER_ID);

        databaseReference.child("users").child(idUser);
        final User user = new User();
        user.dataUser( this );

    }

    /**
     * Método que gerencia a ativação dos andares que o
     * usuário tem acesso.
     * @param acessFloor
     */
    public void manageAcessFloor(String acessFloor){
        for (int i = 0; i<acessFloor.length(); i++){
            if (!acessFloor.substring(i, i+1).equals("0")){
                enableOrigin(Integer.parseInt(acessFloor.substring(i, i+1)));
                enableDestination(Integer.parseInt(acessFloor.substring(i, i+1)));
            }
        }
    }

    /**
     * Método que resolve problema de compatibildiade do
     * background utilizado nos componentes da tela
     * @param checkedTextView
     * @param drawable
     */
    private void setBackgroundCheckedTextView(CheckedTextView checkedTextView, int drawable){
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            checkedTextView.setBackground(getResources().getDrawable(drawable));
        else
            checkedTextView.setBackground(ContextCompat.getDrawable(this, drawable));
    }

    /**
     * Método que resolve problema de compatibildiade do
     * background utilizado nos componentes da tela
     * @param imageView
     * @param drawable
     */
    private void setBackgroundImageView(ImageView imageView, int drawable){
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            imageView.setBackground(getResources().getDrawable(drawable));
        else
            imageView.setBackground(ContextCompat.getDrawable(this, drawable));
    }

    /**
     * Método que inicializa as views (componentes gráficos) da tela
     */
    public void initViews(){
        backgroundProgressBar = (RelativeLayout) findViewById(R.id.background_progress_bar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        tvCurrentFloor = (TextView) findViewById(R.id.tv_curr_floor);
        ivUp = (ImageView)findViewById(R.id.iv_up);
        setBackgroundImageView(ivUp, R.drawable.arrow_up_selector);
        ivDown = (ImageView)findViewById(R.id.iv_down);
        setBackgroundImageView(ivDown, R.drawable.arrow_down_selector);
        ivElevator = (ImageView) findViewById(R.id.iv_elevator);
        btCallElevator = (Button) findViewById(R.id.bt_call_elevator);
        btCallElevator.setEnabled(false);
        tvFloorNfc = (TextView) findViewById(R.id.tv_floor_nfc);

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

    /**
     * Método que inicializa os objetos usados na classe.
     */
    public void initClass(){
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        databaseReference = Util.getFirebase();
        final FrequencyUse frequencyUseContext = new FrequencyUse();
        frequencyUseContext.dataFrequencyUse( this );
        databaseReference.child("frequency_use");
        callPresenter = new CallPresenter();
    }

    /**
     * Método que incializa os atributos do NFC
     */
    public void initNfc() {
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }

    /**
     * Método que captura dos dados do NFC
      * @param text
     * @param locale
     * @param encodeInUtf8
     * @return
     */
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

    /**
     * Método que notifica o usuário que o NFC do seu dispositivo
     * está desligado.
     */
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

    /**
     * Método que processa dados do NFC
     * @param intent
     */
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
                byte[] payload = callElevatorNFC(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
                mTags.add(tag);
            }
        }
    }

    /**
     * Método que chama elevador com NFC
     * @param tag
     * @return
     */
    private String callElevatorNFC(Tag tag) {
        String id = String.valueOf(toDec(tag.getId()));
        //Toast.makeText(this,id, Toast.LENGTH_LONG).show();
        if (progressBar.getVisibility()!=View.VISIBLE){
            if (id.equals(idNfc)){
                if (btCallElevator.isEnabled()){
                    origin=0;
                    destination=floorNfc;
                    callElevator(btCallElevator);
                }
                else{
                    Toast.makeText(this, getString(R.string.msg_elevator), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Não é possivel chamar o elevador com esse cartão.", Toast.LENGTH_SHORT).show();
            }
        }

        return id;
    }

    /**
     * Método que converte id do NFC para decimal
     * @param bytes
     * @return
     */
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

    /**
     * Método que pega o id do NFC do usuário no banco de dados
     */
    public void getIdNfc(){
        databaseReference.child("idNfc").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                try{
                    idNfc  = (String) snapshot.getValue();
                    if (progressBar.getVisibility()==View.VISIBLE){
                        progressBar.setVisibility(View.GONE);
                        backgroundProgressBar.setBackground(null);
                    }
                } catch (Throwable e) {
                    Log.i("Erro", "Erro peger nfc");
                }
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }

    /**
     * Método que pega o andar atual do elevador
     */
    public void getCurrentFoor(){
        databaseReference.child("currentFloor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    currentFloor = (long) snapshot.getValue();
                    if (currentFloor==0){
                        tvCurrentFloor.setText("T");
                    }
                    else {
                        tvCurrentFloor.setText(String.valueOf(currentFloor)+"º");
                    }
                    if (openPortPrev==0 && currentFloor==nextFloor-10){
                        saveNextFloor(nextFloor-10);
                    }
                    manageUpDown();
                } catch (Throwable e) {
                    Log.i("Erro", "Erro peger currentFloor");
                }
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }

    /**
     * Método que pega o andar atual do elevador
     */
    public void getOpenPort(){
        databaseReference.child("openPort").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    openPort = (long) snapshot.getValue();
                    //0 para porta aberta e 1 para porta fechada antes de
                    //finalizar o serviço e 2 apos finaliza a chamada.
                    if (openPort==0){
                        openPortPrev=0;
                        saveNextFloor(destination+10);
                        nextFloor = destination+10;
                        ivElevator.setImageResource(R.mipmap.elevator_open);
                        btCallElevator.setText(getString(R.string.wait));
                        btCallElevator.setEnabled(false);
                    }
                    else if (openPort==1){
                        ivElevator.setImageResource(R.mipmap.elevator_close);
                        btCallElevator.setText(getString(R.string.wait));
                        btCallElevator.setEnabled(false);
                    }
                    else {
                        if (currentFloor!=origin){
                            openPortPrev=2;
                        }else {
                            openPortPrev=0;
                        }
                        ivElevator.setImageResource(R.mipmap.elevator_open);
                        btCallElevator.setText(getString(R.string.call_elevator));
                        btCallElevator.setEnabled(true);
                        getListNotifications();
                    }
                    manageUpDown();
                } catch (Throwable e) {
                    Log.i("Erro", "Erro peger currentFloor");
                }
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }

    /**
     * Método que salva o próximo andar do elevador
     * @param nextFloor
     * @param completionListener
     */
    public void saveNextFloor(int nextFloor, DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = Util.getFirebase().child("nextFloor");

        if( completionListener.length == 0 ){
            firebase.setValue(nextFloor);
        }
        else{
            firebase.setValue(nextFloor, completionListener[0]);
        }
    }

    public void manageUpDown(){
        int nextFloorCompare=nextFloor;
        if (nextFloorCompare>=10){
            nextFloorCompare = nextFloor-10;
        }
        if (currentFloor<nextFloorCompare){
            ivUp.setEnabled(true);
            ivDown.setEnabled(false);
        }
        else if(currentFloor>nextFloorCompare){
            ivDown.setEnabled(true);
            ivUp.setEnabled(false);
        }
    }

    public void getLd(){
        databaseReference.child("ld").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    ld = (long) snapshot.getValue();
                    if (ld!=0){
                        btCallElevator.setText(getString(R.string.wait));
                        btCallElevator.setEnabled(false);
                        saveNotificationLdLs(getString(R.string.error_ld));
                    }
                    else if (ls==0 && openPort==2){
                        btCallElevator.setText(getString(R.string.call_elevator));
                        btCallElevator.setEnabled(true);
                    }
                } catch (Throwable e) {
                    Log.i("Erro", "Erro peger limite de descida");
                }
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }

    public void getLs(){
        databaseReference.child("ls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{
                    ls = (long) snapshot.getValue();
                    if (ls!=0){
                        btCallElevator.setText(getString(R.string.wait));
                        btCallElevator.setEnabled(false);
                        saveNotificationLdLs(getString(R.string.error_ls));
                    }
                    else if (ld==0 && openPort==2){
                        btCallElevator.setText(getString(R.string.call_elevator));
                        btCallElevator.setEnabled(true);
                    }
                } catch (Throwable e) {
                    Log.i("Erro", "Erro peger limite de subida");
                }
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }

    /**
     * Método que salva a ultrapassagem do limite de descida do elevador
     * @param notification
     */
    public void saveNotificationLdLs(String notification){
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        String today = dateFormat.format(new Date(System.currentTimeMillis()));
        Notify notify = new Notify(notification, today);
        String msgError = "";

        notify.saveNotify("notify_"+String.valueOf(qtdNotifications), this);
        qtdNotifications++;

        if (notification.contains("descida")){
            msgError = getString(R.string.msg_error_ld);
        }
        else {
            msgError = getString(R.string.msg_error_ls);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.attention);
        builder.setMessage(msgError)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        }

    public void getListNotifications(){
        databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        qtdNotifications++;
                    }
                    getLd();
                    getLs();
                    if (progressBar.getVisibility()==View.VISIBLE){
                        progressBar.setVisibility(View.GONE);
                        backgroundProgressBar.setBackground(null);
                    }
                }
                catch (Throwable e){
                    Toast.makeText(getApplicationContext(), "Erro ao buscar quantidade de notificações!", Toast.LENGTH_LONG ).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override public void onCancelled(DatabaseError error) { }
        });
    }
}