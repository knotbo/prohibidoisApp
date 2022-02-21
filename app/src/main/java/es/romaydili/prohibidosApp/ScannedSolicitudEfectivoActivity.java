package es.romaydili.prohibidosApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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



public class ScannedSolicitudEfectivoActivity extends AppCompatActivity implements View.OnClickListener {
    private RequestQueue requestQueue;
    public static final int REQUEST_CODE = 1;
    public final String resultado ="";
    private String tipo_documento="";
    private int clicks = 0;

    Button backBtn,imprimirBtn;

    EditText editNombre, editApellidos, editDocNum, editOtroBanco, editImporte;
    TextView datosCliente, txtOtroBanco;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_solicitud_efectivo);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        datosCliente = findViewById(R.id.activity_description_txt);

        imprimirBtn = findViewById(R.id.imprimir_btn);
        backBtn = findViewById(R.id.back_btn);
        editNombre = findViewById(R.id.edit_given_name);
        editApellidos = findViewById(R.id.edit_surname);
        editDocNum = findViewById(R.id.edit_document_number);
        txtOtroBanco = findViewById(R.id.txt_otro_banco);
        editOtroBanco = findViewById(R.id.edit_otro_banco);
        editImporte = findViewById(R.id.edit_importe);

        spinner = findViewById(R.id.spinner_banco);

        backBtn.setOnClickListener(this);
        imprimirBtn.setOnClickListener(this);
        datosCliente.setOnClickListener(this);

        String[] lista_bancos={"Seleccione Banco", "SANTANDER", "LA CAIXA", "BBVA", "SABADELL", "UNICAJA", "BANKINTER", "ABANCA", "KUTXABANK", "CAJAMAR", "IBERCAJA", "BANCA MARCH", "Otro..."};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lista_bancos);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item == "Otro...") {
                    txtOtroBanco.setVisibility(View.VISIBLE);
                    editOtroBanco.setVisibility(View.VISIBLE);
                    //Toast.makeText(ScannedSolicitudEfectivoActivity.this, item.toString(),
                      //      Toast.LENGTH_SHORT).show();

                }else{
                    txtOtroBanco.setVisibility(View.GONE);
                    editOtroBanco.setVisibility(View.GONE);
                }

                checkForm();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        editNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                checkForm();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        editApellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                checkForm();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        editDocNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                checkForm();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        editImporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                checkForm();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        editOtroBanco.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                checkForm();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        startScanner();

    }

    private void addResultToEditText(JSONObject successfulMrzScan) throws JSONException {

        editNombre.setText(successfulMrzScan.getString("given_names_readable"));
        editApellidos.setText(successfulMrzScan.getString("surname"));
        editDocNum.setText(successfulMrzScan.getString("document_number"));

        //Submit();
    }


    private void checkForm(){
        if (editNombre.getText().length() > 3
            && editApellidos.getText().length() > 3
            && editDocNum.getText().length() > 3
            && editImporte.getText().length() > 0
            && !spinner.getSelectedItem().toString().equals("Seleccione Banco")
            && !(spinner.getSelectedItem().toString().equals("Otro...") && editOtroBanco.getText().length() <= 2)
        ){
            imprimirBtn.setEnabled(true);
        }else{
            imprimirBtn.setEnabled(false);
        }
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
            Toast.makeText(this, "Fallo en el resultado de la actividad\r\n" + MRZScanner.sdkVersion(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            MainActivity.setScannerInicial(false);
            finish();
        }
        if (view==imprimirBtn){
            //startActivity(new Intent(this,ProceedToCheckInActivity.class));
            Submit();
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


    private void startScanner() {
        startActivityForResult(new Intent(getApplicationContext(),ScannerActivity.class),REQUEST_CODE);
    }


    private void Submit(){
        String banco="";
        if (spinner.getSelectedItem().toString() != "Otro..." ){
            banco = spinner.getSelectedItem().toString();
        }else{
            banco = editOtroBanco.getText().toString();
        }

        imprimirSolicitudEfectivo(getApplicationContext(), editDocNum.getText().toString(),  editNombre.getText().toString(),  editApellidos.getText().toString(),  banco, editImporte.getText().toString() );
        /*
        //URL of the request we are sending
        StringRequest postRequest = new StringRequest(Request.Method.POST, MainActivity.getUrlEfectivo(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Context context = getApplicationContext();
                        CharSequence text = response.substring(1);



                        boolean permitido=false,resultado_correcto=false;
                        boolean certificadoCovid = false; //Certificado Covid
                        String mensaje="";
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
                                certificadoCovid = jsonObject.getBoolean("certificadoCovid"); //Certificado Covid
                            }else{
                                if(jsonObject.getString("status").equals("true")) {
                                    resultado_correcto=false;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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


                        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ScannedSolicitudEfectivoActivity.this, estilo );

                        alertOpciones.setTitle(titulo);
                        alertOpciones.setIcon(icono);
                        alertOpciones.setMessage(Html.fromHtml(mensaje));
                        alertOpciones.setCancelable(false);
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

                        AlertDialog dialog = alertOpciones.show();

                        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
                        messageText.setTextSize(18);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());











///////////////////////////////

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Context context = getApplicationContext();
                        CharSequence textToast = "";
                        int duration = Toast.LENGTH_SHORT;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            textToast = "Servidor no Responde o No Hay Conexión a Internet";
                        } else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            textToast="Fallo de autenticación del Servidor";
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            textToast="Error en la Respuesta del Servidor";
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            textToast="Error de Red";
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            textToast="No se puede interpretar la Respuesta del Servidor";
                        }

                        final Toast toast = Toast.makeText(context, textToast, duration);
                        toast.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("dni", editDocNum.getText().toString());
                params.put("nombre", editNombre.getText().toString());
                params.put("apellidos", editApellidos.getText().toString());
                params.put("banco", spinner.getSelectedItem().toString());
                params.put("dispositivo", MainActivity.getIdentificadorAndroid());
                params.put("usuario", MainActivity.getUsuario());
                params.put("provincia", MainActivity.getProvincia());
                params.put("version", MainActivity.getVersion());


                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

         */
    }

    public void imprimirSolicitudEfectivo(final Context context, final String dni, final String nombre, final String apellidos, final String banco, final String importe){


        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try (Socket socket = new Socket(MainActivity.getIpServidor(), 9090)) {
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    //Console console = System.console();
                    writer.println("solicitudEfectivo," + dni + ',' + nombre + ',' + apellidos + ',' + banco + ',' + importe);

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


//class MyPrintDocumentAdapter extends PrintDocumentAdapter
//{
//    Context context;
//
//    public MyPrintDocumentAdapter(Context context)
//    {
//        this.context = context;
//    }
//
//    @Override
//    public void onLayout(PrintAttributes oldAttributes,
//                         PrintAttributes newAttributes,
//                         CancellationSignal cancellationSignal,
//                         LayoutResultCallback callback,
//                         Bundle metadata) {
//    }
//
//
//    @Override
//    public void onWrite(final PageRange[] pageRanges,
//                        final ParcelFileDescriptor destination,
//                        final CancellationSignal
//                                cancellationSignal,
//                        final WriteResultCallback callback) {
//    }
//
//}

