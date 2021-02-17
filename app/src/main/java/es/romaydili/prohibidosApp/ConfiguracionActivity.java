package es.romaydili.prohibidosApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
//import androidx.security.crypto.MasterKeys;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.security.keystore.KeyGenParameterSpec;
//import android.security.keystore.KeyProperties;
//import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//import com.romaydili.prohibidosApp.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.timessquare.CalendarPickerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfiguracionActivity extends AppCompatActivity implements View.OnClickListener {
    CalendarPickerView datePicker;
    Date today;
    Calendar nextYear;
    Button setDate,backBtn,guardarBtn;
    FrameLayout frameLayout;
    ScrollView scrollView;
    FloatingActionButton fab;
    String firstDate, lastDate;
    CheckBox cashBox, creditBox;
    LinearLayout linearCredit, linearCash;
    List<Date> selectedDate;
    EditText adavancePay;
    TextView totalAmount;
    TextView androidID, configuracion,ultActualizacion,ficheroUltActualizacion,numProhibidosUltActualizacion,usuario_txt;
    EditText usuario_edit;
    CheckBox scannerInicial,debugMode,imprimirVales,ocultarInfo,beep;
    Boolean editando_usuario=false;

    int clicks=0,clicksUsuario=0;

    private static final String CONFIG_FILE_NAME = "FileName";
    private static final String CONFIG_MASTER_KEY_ALIAS = "KeyAlias";
    private static EncryptedSharedPreferences mInstance;
    private SharedPreferences mSharedPreferences;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        frameLayout = findViewById(R.id.framelatout_1);
        scrollView = findViewById(R.id.scrollView2);
        fab = findViewById(R.id.floatingActionButton);

        selectedDate= new ArrayList<>();
        backBtn=findViewById(R.id.back_btn);
        guardarBtn=findViewById(R.id.guardar_btn);
        androidID=findViewById(R.id.androidID_txt);
        configuracion=findViewById(R.id.configuracion_txt);
        ultActualizacion=findViewById(R.id.ultimaActualizacion);
        ficheroUltActualizacion=findViewById(R.id.fichero_actualizacion);
        numProhibidosUltActualizacion=findViewById(R.id.numProhibidos);
        scannerInicial=findViewById(R.id.scannerInical_ckBox);
        beep=findViewById(R.id.beep_ckBox);
        imprimirVales=findViewById(R.id.impresion_ckBox);
        debugMode=findViewById(R.id.debugMode_ckBox);
        ocultarInfo=findViewById(R.id.ocultarInfo_ckBox);
        usuario_edit=findViewById(R.id.usuario_edit);
        usuario_txt=findViewById(R.id.usuario_txt);


        configuracion.setOnClickListener(this);
        usuario_txt.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        guardarBtn.setOnClickListener(this);
        ocultarInfo.setOnClickListener(this);
        beep.setOnClickListener(this);
        usuario_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                editando_usuario=true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        if(MainActivity.getActivado()) {
            ultActualizacion.setText(MainActivity.getFechaUltActualizacion());
            ficheroUltActualizacion.setText(MainActivity.getFicheroUltActualizacion());
            numProhibidosUltActualizacion.setText(MainActivity.getNumProhibidosUltActualizacion());
        }


        //Imprimir Vales sólo para Seronuba
        if(!MainActivity.getVersion().equals("SerOnuba")){
            imprimirVales.setVisibility(View.GONE);
            imprimirVales.setChecked(false);
            imprimirVales.setEnabled(false);
        }

        if(MainActivity.getScannerInicial() == true){
            scannerInicial.setChecked(true);
        }else{
            scannerInicial.setChecked(false);
        }
        if(MainActivity.getBeep() == true){
            beep.setChecked(true);
        }else{
            beep.setChecked(false);
        }
        if(MainActivity.getOcultarInfo() == true){
            ocultarInfo.setChecked(true);
        }else{
            ocultarInfo.setChecked(false);
        }
        if(MainActivity.getImprimirVales() == true){
            imprimirVales.setChecked(true);
        }else{
            imprimirVales.setChecked(false);
        }
        if(MainActivity.getDebugMode() == true){
            debugMode.setChecked(true);
        }else{
            debugMode.setChecked(false);
        }



        //setDate.setText(resultado);

    }

    private void launchCalendar() {

        today = new Date();
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        datePicker = findViewById(R.id.calendar_view);
        datePicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(today);
        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                selectedDate = (ArrayList<Date>) datePicker.getSelectedDates();
                firstDate = selectedDate.get(0).getDate() + "." + selectedDate.get(0).getMonth() + "." + selectedDate.get(0).getYear();
                lastDate = selectedDate.get(selectedDate.size() - 1).getDate() + "." + selectedDate.get(0).getMonth() + "." + selectedDate.get(0).getYear();
                setDate.setText(firstDate + "-" + lastDate);
            }

            @Override
            public void onDateUnselected(Date date) {
                androidID.setText(MainActivity.getIdentificadorAndroid());
            }
        });
    }
    @Override
    public void onClick(View view) {
        if (view == setDate) {
            launchCalendar();
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
        if (view == fab) {
            frameLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            setDate.setText("From :" + firstDate + "\r\n" +
                    " To " + lastDate);
        }
        if (view == configuracion){
            clicks++;
            if(clicks >= 10) {
                androidID.setText(MainActivity.getIdentificadorAndroid());
                debugMode.setVisibility(View.VISIBLE);
                usuario_edit.setVisibility(View.VISIBLE);
                usuario_txt.setVisibility(View.VISIBLE);
            }
        }

        if (view == usuario_txt){
            clicksUsuario++;
            if(clicksUsuario >= 10) {
                usuario_edit.setText(MainActivity.getUsuario());
                editando_usuario=false;
            }
        }

        if (view == backBtn) {
            finish();
        }

        if (view == guardarBtn){
            guardarPrefencias();
        }
    }


    private void guardarPrefencias(){



        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putBoolean("scanner_inicial", scannerInicial.isChecked());
        myEditor.putBoolean("ocultar_info", ocultarInfo.isChecked());
        myEditor.putBoolean("imprimir_vales", imprimirVales.isChecked());
        myEditor.putBoolean("beep", beep.isChecked());
        myEditor.commit();

        MainActivity.setScannerInicial(scannerInicial.isChecked());
        MainActivity.setOcultarInfo(ocultarInfo.isChecked());
        MainActivity.setImprimirVales(imprimirVales.isChecked());
        MainActivity.setBeep(beep.isChecked());
        MainActivity.setDebugMode(debugMode.isChecked());
/*
        String name = myPreferences.getString("NAME", "unknown");
        int age = myPreferences.getInt("AGE", 0);
        boolean isSingle = myPreferences.getBoolean("SINGLE?", false);
*/
        try {
            String androidID = MainActivity.getIdentificadorAndroid();
            String usuario;
            if(usuario_edit.getVisibility() == View.VISIBLE && usuario_edit.getText().length() > 0){
                usuario=usuario_edit.getText().toString();
                MainActivity.setUsuario(usuario_edit.getText().toString());
            }else{
                usuario=MainActivity.getUsuario();
            }


            MasterKey masterKey =new
                    MasterKey.Builder(this,MasterKey.DEFAULT_MASTER_KEY_ALIAS).
                    setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

            SharedPreferences msharedPreferences = EncryptedSharedPreferences.create(
                    this,"id",masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            // use the shared preferences and editor as you normally would
            SharedPreferences.Editor editor = msharedPreferences.edit();
            //msharedPreferences.edit().putString("androidID",androidID).apply();
            //msharedPreferences.edit().putString("usuario",usuario).apply();
            editor.putString("androidID", androidID);
            editor.putString("usuario", usuario);
            editor.apply();

            finish();
            //Si se está editando el usuario, reiniciamos la App
            if(editando_usuario == true) {
                final Toast toast = Toast.makeText(getApplicationContext(), "Configuración Guardada\n\nReiniciando la App", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                        .FLAG_ACTIVITY_CLEAR_TOP));
            }else{
                final Toast toast = Toast.makeText(getApplicationContext(), "Configuración Guardada", Toast.LENGTH_SHORT);
                toast.show();
            }




        }catch (Exception e){
            //
            e.printStackTrace();
            final Toast toast = Toast.makeText(getApplicationContext(), "Error al Guardar la Configuración", Toast.LENGTH_SHORT);
            toast.show();
        }


    }


}
