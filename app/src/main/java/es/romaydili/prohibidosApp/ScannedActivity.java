package es.romaydili.prohibidosApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CancellationSignal;
//import android.os.Handler;
//import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
//import android.print.PrintManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.scansolutions.mrzscannerlib.MRZScanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes;

//import javax.crypto.Cipher;



public class ScannedActivity extends AppCompatActivity implements View.OnClickListener {
    private RequestQueue requestQueue;
    public static final int REQUEST_CODE = 1;
    public final String resultado ="";
    private String tipo_documento="";
    private int clicks = 0;

    Button scanBtn,backBtn,proceedBtn,manualBtn;

    EditText editGivenName, surName, editDocNum, editIssuingCount, editNationallity, editDateOfBirth, editSex, editExporationDate, editOptionalVal,editIssuingDate,editRawMrz,editSDK;
    TextView datosCliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        datosCliente = findViewById(R.id.activity_description_txt);
        scanBtn = findViewById(R.id.scan_button);
        manualBtn = findViewById(R.id.manual_button);
        proceedBtn = findViewById(R.id.proceede_btn);
        backBtn = findViewById(R.id.back_btn);
        editGivenName = findViewById(R.id.edit_given_name);
        surName = findViewById(R.id.edit_surname);
        editDocNum = findViewById(R.id.edit_document_number);
        editIssuingCount = findViewById(R.id.edit_issuing_country);
        editNationallity = findViewById(R.id.edit_nationality);
        editDateOfBirth = findViewById(R.id.edit_date_of_birth);
        editSex = findViewById(R.id.edit_sex);
        editExporationDate = findViewById(R.id.edit_expiration_date);
        editOptionalVal = findViewById(R.id.edit_optional_values);
        editIssuingDate = findViewById(R.id.edit_issuing_date);
        editRawMrz = findViewById(R.id.edit_raw_mrz);
        editSDK = findViewById(R.id.edit_sdk);
//      imgBtnFront=findViewById(R.id.imageButton_scanImage_front);
//      imgBtnBack=findViewById(R.id.imageButton_scanImage_back);

        scanBtn.setOnClickListener(this);
        manualBtn.setOnClickListener(this);
//        imgBtnBack.setOnClickListener(this);
//        imgBtnFront.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        proceedBtn.setOnClickListener(this);
        datosCliente.setOnClickListener(this);

        startScanner();

    }

    private void addResultToEditText(JSONObject successfulMrzScan) throws JSONException {

        editGivenName.setText(successfulMrzScan.getString("given_names_readable"));
        surName.setText(successfulMrzScan.getString("surname"));
        editDocNum.setText(successfulMrzScan.getString("document_number"));
        editIssuingCount.setText(successfulMrzScan.getString("issuing_country"));
        editIssuingDate.setText(successfulMrzScan.getString("est_issuing_date_readable"));
        editNationallity.setText(successfulMrzScan.getString("nationality"));
        editDateOfBirth.setText(successfulMrzScan.getString("dob_readable"));
        editSex.setText(successfulMrzScan.getString("sex"));
        editExporationDate.setText(successfulMrzScan.getString("expiration_date_readable"));
        editOptionalVal.setText(successfulMrzScan.getString("optionals"));
        editRawMrz.setText(successfulMrzScan.getString("raw_mrz"));
        tipo_documento=successfulMrzScan.getString("document_type_raw");
        editSDK.setText(MRZScanner.sdkVersion());

        if(MainActivity.getDebugMode() == true){
            editRawMrz.setVisibility(View.VISIBLE);
            editSDK.setVisibility(View.VISIBLE);
            findViewById(R.id.label_mrz).setVisibility(View.VISIBLE);
            findViewById(R.id.label_sdk).setVisibility(View.VISIBLE);
            findViewById(R.id.txt_given_name).setVisibility(View.VISIBLE);

            editGivenName.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_surname).setVisibility(View.VISIBLE);
            surName.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_issuing_country).setVisibility(View.VISIBLE);
            editIssuingCount.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_issuing_date).setVisibility(View.VISIBLE);
            editIssuingDate.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_nationality).setVisibility(View.VISIBLE);
            editNationallity.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_sex).setVisibility(View.VISIBLE);
            editSex.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_expiration_date).setVisibility(View.VISIBLE);
            editExporationDate.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_optional_values).setVisibility(View.VISIBLE);
            editOptionalVal.setVisibility(View.VISIBLE);
        }else{
            editRawMrz.setVisibility(View.GONE);
            editSDK.setVisibility(View.GONE);
            findViewById(R.id.label_mrz).setVisibility(View.GONE);
            findViewById(R.id.label_sdk).setVisibility(View.GONE);
        }
        if(MainActivity.getOcultarInfo() == true){
            findViewById(R.id.txt_given_name).setVisibility(View.GONE);
            editGivenName.setVisibility(View.GONE);
            findViewById(R.id.txt_surname).setVisibility(View.GONE);
            surName.setVisibility(View.GONE);
            findViewById(R.id.txt_issuing_country).setVisibility(View.GONE);
            editIssuingCount.setVisibility(View.GONE);
            findViewById(R.id.txt_issuing_date).setVisibility(View.GONE);
            editIssuingDate.setVisibility(View.GONE);
            findViewById(R.id.txt_nationality).setVisibility(View.GONE);
            editNationallity.setVisibility(View.GONE);
            findViewById(R.id.txt_sex).setVisibility(View.GONE);
            editSex.setVisibility(View.GONE);
            findViewById(R.id.txt_expiration_date).setVisibility(View.GONE);
            editExporationDate.setVisibility(View.GONE);
            findViewById(R.id.txt_optional_values).setVisibility(View.GONE);
            editOptionalVal.setVisibility(View.GONE);
        }else{
            findViewById(R.id.txt_given_name).setVisibility(View.VISIBLE);
            editGivenName.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_surname).setVisibility(View.VISIBLE);
            surName.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_issuing_country).setVisibility(View.VISIBLE);
            editIssuingCount.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_issuing_date).setVisibility(View.VISIBLE);
            editIssuingDate.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_nationality).setVisibility(View.VISIBLE);
            editNationallity.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_sex).setVisibility(View.VISIBLE);
            editSex.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_expiration_date).setVisibility(View.VISIBLE);
            editExporationDate.setVisibility(View.VISIBLE);
            findViewById(R.id.txt_optional_values).setVisibility(View.VISIBLE);
            editOptionalVal.setVisibility(View.VISIBLE);
        }


        Submit();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK) {
                String requiredValue = data.getStringExtra("key");
                JSONObject jsonObj = new JSONObject(requiredValue);

                addResultToEditText(jsonObj);
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Fallo en el resulatado de la actividad\r\n" + MRZScanner.sdkVersion(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == scanBtn) {
            startScanner();
        }

        if (view==manualBtn){
            //startActivity(new Intent(this,ProceedToCheckInActivity.class));
            solicitar_dni();
        }
//        if (view==imgBtnFront){
//            REQUEST_IMAGE_SCAN=2;
//            startImageScan();
//        }
//        if ( view==imgBtnBack)
//        {
//            REQUEST_IMAGE_SCAN=3;
//            startImageScan();
//        }
        if (view==backBtn){
            MainActivity.setScannerInicial(false);
            finish();
        }
        if (view==proceedBtn){
            //startActivity(new Intent(this,ProceedToCheckInActivity.class));
            Submit();
        }

        if (view == datosCliente){
            clicks++;
            if(clicks >= 10) {
                MainActivity.setDebugMode(true);
                editRawMrz.setVisibility(View.VISIBLE);
                editSDK.setVisibility(View.VISIBLE);
                findViewById(R.id.label_mrz).setVisibility(View.VISIBLE);
                findViewById(R.id.label_sdk).setVisibility(View.VISIBLE);

                findViewById(R.id.txt_given_name).setVisibility(View.VISIBLE);
                editGivenName.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_surname).setVisibility(View.VISIBLE);
                surName.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_issuing_country).setVisibility(View.VISIBLE);
                editIssuingCount.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_issuing_date).setVisibility(View.VISIBLE);
                editIssuingDate.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_nationality).setVisibility(View.VISIBLE);
                editNationallity.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_sex).setVisibility(View.VISIBLE);
                editSex.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_expiration_date).setVisibility(View.VISIBLE);
                editExporationDate.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_optional_values).setVisibility(View.VISIBLE);
                editOptionalVal.setVisibility(View.VISIBLE);
            }
        }
    }
//
//    private void startImageScan() {
//        Intent intent = new Intent(getApplicationContext(),ScannerActivity.class);
//        intent.putExtra("imagekey",REQUEST_IMAGE_SCAN);
//        startActivity(intent);
//    }

    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String reg = "^[a-zA-Z0-9]*$";
            if(!source.toString().matches(reg)) {
                return "";
            }

            return null;
        }
    };

    public void solicitar_dni(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScannedActivity.this);

        builder.setIcon(android.R.drawable.ic_menu_search);
        builder.setTitle("Comprobar Identificación");
        builder.setMessage("Introduzca DNI, NIE o Pasaporte\r\n(Sin espacios ni guiones)");
        // Set `EditText` to `dialog`. You can add `EditText` from `xml` too.
        final EditText input = new EditText(ScannedActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.setFilters(new InputFilter[] { filter });
        builder.setPositiveButton("Comprobar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String m_Text = input.getText().toString().toUpperCase();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                        String reg = "^[a-zA-Z0-9]*$";
                        if(m_Text.matches(reg) && m_Text.length() >= 4 && m_Text.length() <= 12) {
                            comprobar_dni(m_Text);
                            //final Toast toast = Toast.makeText(getApplicationContext(), "Comprobando " + m_Text, Toast.LENGTH_SHORT);
                            //toast.show();
                        }else{
                            if(m_Text.length() < 4) {
                                final Toast toast = Toast.makeText(getApplicationContext(), "Introduzca al menos 4 caracteres (" + m_Text + ")", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            if(m_Text.length() > 12){
                                final Toast toast = Toast.makeText(getApplicationContext(), "Introduzca Menos de 12 caracteres (" + m_Text + ")", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            if(!m_Text.matches(reg)){
                                final Toast toast = Toast.makeText(getApplicationContext(), "Introduzca Sólo dígitos y caracteres (" + m_Text + ")", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // DO TASK
                    }
                });

        final android.app.AlertDialog dialog = builder.create();
        dialog.show();
// Initially disable the button
        ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
// OR you can use here setOnShowListener to disable button at first
// time.

// Now set the textchange listener for edittext
        input.addTextChangedListener(new TextWatcher() {
            String reg = "^[a-zA-Z0-9]*$";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(!s.toString().matches(reg)){
                    final Toast toast = Toast.makeText(getApplicationContext(), "Introduzca Sólo dígitos y caracteres", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if(s.length() > 12){
                    final Toast toast = Toast.makeText(getApplicationContext(), "Introduzca Menos de 12 caracteres", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (s.toString().matches(reg) && s.length() >= 4 && s.length() <= 12) {
                    // Enable ok button
                    ((android.app.AlertDialog) dialog).getButton(
                            android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    // Disable the button.
                    ((android.app.AlertDialog) dialog).getButton(
                            android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

            }
        });
    }

    private void comprobar_dni(final String documento){
        //URL of the request we are sending

        StringRequest postRequest = new StringRequest(Request.Method.POST, MainActivity.getUrlDocumento(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        boolean permitido=false,resultado_correcto=false;
                        //boolean certificadoCovid = false; //Certificado Covid
                        boolean prohibidoDispositivo = false; //mensaje de prohibido en el dispositivo
                        String mensaje="";
                        String mensajeProhibidoDispositivo="";
                        int numAccesosHoy=-1;

                        // Creo un array con los datos JSON que he obtenido
                        ArrayList listaArray = new ArrayList<>();

                        // Solicito los datos al archivo JSON
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // En los datos que recibo verifico si obtengo el estado o 'status' con el valor 'true'
                            // El dato 'status' con el valor 'true' se encuentra dentro del archivo JSON
                            if (jsonObject.getString("status").equals("true")) {
                                resultado_correcto=jsonObject.getBoolean("resultado_correcto");
                                permitido=jsonObject.getBoolean("acceso_permitido");
                                mensaje=jsonObject.getString("mensaje");
                                numAccesosHoy=jsonObject.getInt("num_accesos_hoy");
                                //certificadoCovid = jsonObject.getBoolean("certificadoCovid"); //Certificado Covid
                                prohibidoDispositivo = jsonObject.getBoolean("prohibidoDispositivo"); //Tiene mensaje de prohibido en el dispositivo
                                mensajeProhibidoDispositivo = jsonObject.getString("mensajeProhibidoDispositivo");
                            }else{
                                if(jsonObject.getString("status").equals("true")) {
                                    resultado_correcto=false;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            final Toast toast = Toast.makeText(getApplicationContext(), "Error procesando JSON", Toast.LENGTH_LONG);
                            toast.show();
                        }

                        if(MainActivity.getDebugMode() == true) {
                            final Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG);
                            toast.show();
                        }else{
                            //final Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
                            //toast.show();
                        }


                        ///////////////////////////////
                        final CharSequence[] opciones={"Escanear","Volver"};
                        int estilo = R.style.MyDialogThemeProhibido;

                        String titulo;
                        int icono;
                        if(resultado_correcto == true && permitido == true) {
                            estilo = R.style.MyDialogThemePermitido;
                            titulo="Accceso Permitido";
                            icono=R.drawable.ic_baseline_check_circle_24;
                            if(MainActivity.getPromociones() == true && MainActivity.getImprimirVales() == true && numAccesosHoy == 1) { //Primer acceso del día
                                imprimir(getApplicationContext(), documento);
                                titulo="Accceso Permitido + VALE";
                                mensaje=mensaje+"\n\n\n\nIMPRIMIENDO VALE";
                            }
                        }else{
                            if(resultado_correcto == true && permitido ==false) {
                                estilo = R.style.MyDialogThemeProhibido;
                                titulo="Acceso Denegado";
                                icono=R.drawable.ic_baseline_not_interested_24;
                            }else{
                                estilo = R.style.MyDialogThemeNeutral;
                                titulo="Resultado";
                                icono=R.drawable.ic_baseline_info_24;
                            }
                        }


                        final android.app.AlertDialog.Builder alertOpciones = new android.app.AlertDialog.Builder(ScannedActivity.this, estilo );

                        alertOpciones.setTitle(titulo);
                        alertOpciones.setIcon(icono);
                        alertOpciones.setMessage(Html.fromHtml(mensaje));
                        alertOpciones.setCancelable(false);

                        if (prohibidoDispositivo == true) {
                            LinearLayout diagLayout = new LinearLayout(ScannedActivity.this);
                            diagLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView textoMensaje = new TextView(ScannedActivity.this);
                            textoMensaje.setTextColor(Color.BLACK);
                            textoMensaje.setText(Html.fromHtml(mensajeProhibidoDispositivo));
                            textoMensaje.setPadding(30, 30, 10, 30);
                            textoMensaje.setBackgroundColor(ContextCompat.getColor(ScannedActivity.this, R.color.peligro));
                            textoMensaje.setGravity(Gravity.CENTER);
                            textoMensaje.setTextSize(30);
                            diagLayout.addView(textoMensaje);
                            alertOpciones.setView(diagLayout);
                        }

                        alertOpciones.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
/*
                        if(certificadoCovid == false) {
                            alertOpciones.setNeutralButton("Certificar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i;
                                    PackageManager manager = getPackageManager();
                                    try {
                                        i = manager.getLaunchIntentForPackage("ch.admin.bag.covidcertificate.verifier");
                                        if (i == null)
                                            throw new PackageManager.NameNotFoundException();
                                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                                        startActivity(i);
                                    } catch (PackageManager.NameNotFoundException e) {

                                    }
                                    alertOpciones.show();
                                    ((TextView) alertOpciones.show().findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            });
                        }
*/
                        android.app.AlertDialog dialog = alertOpciones.show();
                        //alertOpciones.show();
                        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        messageText.setTextSize(18);
                        dialog.show();
                        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Context context = getApplicationContext();
                        CharSequence text = "Compruebe su Conexión a Internet:\r\n\r\n" + error.toString();
                        int duration = Toast.LENGTH_SHORT;

                        final Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("documento", documento);
                params.put("dispositivo", MainActivity.getIdentificadorAndroid());
                params.put("usuario", MainActivity.getUsuario());
                params.put("provincia", MainActivity.getProvincia());
                params.put("version", MainActivity.getVersion());

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void startScanner() {
        startActivityForResult(new Intent(getApplicationContext(),ScannerActivity.class),REQUEST_CODE);
    }


    private void Submit(){
        //URL of the request we are sending
        StringRequest postRequest = new StringRequest(Request.Method.POST, MainActivity.getUrlMrz(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Context context = getApplicationContext();
                        CharSequence text = response.substring(1);



                        boolean permitido=false,resultado_correcto=false;
                        //boolean certificadoCovid = false; //Certificado Covid
                        boolean prohibidoDispositivo = false; //mensaje de prohibido en el dispositivo
                        String mensaje="";
                        String mensajeProhibidoDispositivo="";
                        int numAccesosHoy=-1;

                        // Creo un array con los datos JSON que he obtenido
                        ArrayList listaArray = new ArrayList<>();

                        // Solicito los datos al archivo JSON
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // En los datos que recibo verifico si obtengo el estado o 'status' con el valor 'true'
                            // El dato 'status' con el valor 'true' se encuentra dentro del archivo JSON
                            if (jsonObject.getString("status").equals("true")) {
                                resultado_correcto=jsonObject.getBoolean("resultado_correcto");
                                permitido=jsonObject.getBoolean("acceso_permitido");
                                mensaje=jsonObject.getString("mensaje");
                                numAccesosHoy=jsonObject.getInt("num_accesos_hoy");
                                //certificadoCovid = jsonObject.getBoolean("certificadoCovid"); //Certificado Covid
                                prohibidoDispositivo = jsonObject.getBoolean("prohibidoDispositivo"); //Tiene mensaje de prohibido en el dispositivo
                                mensajeProhibidoDispositivo = jsonObject.getString("mensajeProhibidoDispositivo");
                            }else{
                                if(jsonObject.getString("status").equals("true")) {
                                    resultado_correcto=false;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            final Toast toast = Toast.makeText(getApplicationContext(), "Error procesando JSON", Toast.LENGTH_LONG);
                            toast.show();
                        }


                        if(MainActivity.getDebugMode() == true) {
                            final Toast toast = Toast.makeText(context, response, Toast.LENGTH_LONG);
                            toast.show();
                        }else{
                            //final Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
                            //toast.show();
                        }


                        int estilo = R.style.MyDialogThemeProhibido;

                        String titulo;
                        int icono;
                        if(resultado_correcto == true && permitido == true) {
                            estilo = R.style.MyDialogThemePermitido;
                            titulo="Accceso Permitido";
                            icono=R.drawable.ic_baseline_check_circle_24;
                            if(MainActivity.getPromociones() == true && MainActivity.getImprimirVales() == true && numAccesosHoy == 1) { //Primer acceso del día
                                imprimir(getApplicationContext(), editDocNum.getText().toString());
                                titulo="Accceso Permitido + VALE";
                                mensaje=mensaje+"\n\n\n\n IMPRIMIENDO VALE";
                            }
                        }else{
                            if (resultado_correcto == true && permitido == false) {
                                estilo = R.style.MyDialogThemeProhibido;
                                titulo="Acceso Denegado";
                                icono=R.drawable.ic_baseline_not_interested_24;
                            }else{
                                estilo = R.style.MyDialogThemeNeutral;
                                titulo="Resultado";
                                icono=R.drawable.ic_baseline_info_24;
                            }
                        }


                        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ScannedActivity.this, estilo );

                        alertOpciones.setTitle(titulo);
                        alertOpciones.setIcon(icono);
                        alertOpciones.setMessage(Html.fromHtml(mensaje));
                        alertOpciones.setCancelable(false);

                        if (prohibidoDispositivo == true) {
                            LinearLayout diagLayout = new LinearLayout(ScannedActivity.this);
                            diagLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView textoMensaje = new TextView(ScannedActivity.this);
                            textoMensaje.setTextColor(Color.BLACK);
                            textoMensaje.setText(Html.fromHtml(mensajeProhibidoDispositivo));
                            textoMensaje.setPadding(30, 30, 10, 30);
                            textoMensaje.setBackgroundColor(ContextCompat.getColor(ScannedActivity.this, R.color.peligro));
                            textoMensaje.setGravity(Gravity.CENTER);
                            textoMensaje.setTextSize(30);
                            diagLayout.addView(textoMensaje);
                            alertOpciones.setView(diagLayout);
                        }

                        alertOpciones.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertOpciones.setNeutralButton("LEER DE NUEVO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startScanner();
                            }
                        });
/*
                        if(certificadoCovid == false) {
                            alertOpciones.setNegativeButton("Certificar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i;
                                    PackageManager manager = getPackageManager();
                                    try {
                                        i = manager.getLaunchIntentForPackage("ch.admin.bag.covidcertificate.verifier");
                                        if (i == null)
                                            throw new PackageManager.NameNotFoundException();
                                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                                        startActivity(i);
                                    } catch (PackageManager.NameNotFoundException e) {

                                    }
                                    alertOpciones.show();
                                    ((TextView) alertOpciones.show().findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            });
                        }
*/
                        AlertDialog dialog = alertOpciones.show();

                        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
                        messageText.setTextSize(18);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                        //Custom AlertDialog DESACTIVADO
                        /*
                        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                        ViewGroup viewGroup = findViewById(android.R.id.content);

                        //then we will inflate the custom alert dialog xml that we created
                        final View dialogView = LayoutInflater.from(context).inflate(R.layout.my_dialog_scanned_ok, viewGroup, false);

                        //Modificamos el mensaje
                        TextView textView = (TextView) dialogView.findViewById(R.id.mensaje);
                        textView.setText(mensaje);

                        //Now we need an AlertDialog.Builder object
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ScannedActivity.this);

                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView);

                        //finally creating the alert dialog and displaying it
                        final AlertDialog alertDialog = builder.create();

                        Button ok_item = (Button)dialogView.findViewById(R.id.ok_btn);
                        ok_item.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                alertDialog.dismiss();

                            }
                        });
                        Button scanear_item = (Button)dialogView.findViewById(R.id.escanear_btn);
                        scanear_item.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                startScanner();

                            }
                        });
                        alertDialog.show();

                         */
                    //Fin de Custom AlertDialog









///////////////////////////////

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Context context = getApplicationContext();
                        CharSequence text = "Compruebe su Conexión a Internet:\r\n\r\n" + error.toString();
                        int duration = Toast.LENGTH_SHORT;

                        final Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put( "nacionalidad", editNationallity.getText().toString() );
                params.put( "fecha_nacimiento", editDateOfBirth.getText().toString());
                params.put("dni", editDocNum.getText().toString());
                params.put("nombre", editGivenName.getText().toString());
                params.put("apellidos", surName.getText().toString());
                params.put("sexo", editSex.getText().toString());
                params.put("caducidad_dni", editExporationDate.getText().toString());
                params.put("opcional", editOptionalVal.getText().toString());
                params.put("pais_origen", editIssuingCount.getText().toString());
                params.put("mrz_completo", editRawMrz.getText().toString());
                params.put("tipo_documento", tipo_documento);
                params.put("fecha_expedicion", editIssuingDate.getText().toString());
                params.put("dispositivo", MainActivity.getIdentificadorAndroid());
                params.put("usuario", MainActivity.getUsuario());
                params.put("provincia", MainActivity.getProvincia());
                params.put("version", MainActivity.getVersion());


                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    public void imprimir(final Context context, final String msg){


        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try (Socket socket = new Socket(MainActivity.getIpServidor(), 9090)) {
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    //Console console = System.console();
                     writer.println(msg);

                     InputStream input = socket.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                     final String respuesta = reader.readLine();

                     runOnUiThread(new Runnable() {
                         public void run() {
                             Toast.makeText(context, respuesta, Toast.LENGTH_LONG).show();
                         }
                    });

                    //System.out.println("Acabo el Thread " + Thread.currentThread().getId());
                    //socket.close();
                } catch (final UnknownHostException ex) {

                    System.out.println("Servidor no Encontrado: " + ex.getMessage());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, "Servidor no Encontrado: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (final IOException ex) {
                    System.out.println("I/O error: " + ex.getMessage());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, "I/O Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            });
            thread.start();


        /*
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Socket s = new Socket("192.168.1.2", 9090);

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //String s = mTextViewReplyFromServer.getText().toString();
                            if (st.trim().length() != 0) {
                                //mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
                                Context context = getApplicationContext();
                                CharSequence text = "Compruebe su Conexión a Internet:\r\n\r\n";
                                int duration = Toast.LENGTH_SHORT;

                                final Toast toast = Toast.makeText(context,"\nFrom Server : " + st, duration);
                                toast.show();

                                //final Toast toast = Toast.makeText(getApplicationContext(), s + "\nFrom Server : " + st, Toast.LENGTH_LONG);
                                //toast.show();
                            }
                        }
                    });

                    output.close();
                    out.close();
                    //s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

         */
    }


}


class MyPrintDocumentAdapter extends PrintDocumentAdapter
    {
        Context context;

        public MyPrintDocumentAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {
        }


        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor destination,
                            final CancellationSignal
                                    cancellationSignal,
                            final WriteResultCallback callback) {
        }

    }

