package es.romaydili.prohibidosApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.scansolutions.mrzscannerlib.MRZLicenceResultListener;
import com.scansolutions.mrzscannerlib.MRZResultModel;
import com.scansolutions.mrzscannerlib.MRZScanner;
import com.scansolutions.mrzscannerlib.MRZScannerListener;
import com.scansolutions.mrzscannerlib.ScannerType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class ScannerActivity extends AppCompatActivity implements MRZScannerListener {

    private static final String TAG = "ScannerActivity";
    JSONObject successfulMrzScan;
    MRZScanner mrzScanner;
    int imageRequest;

    private static final String TYPE_EXTRA = "TYPE_EXTRA";
    public static void startScannerActivity(Context context, ScannerType type) {
        Intent intent = new Intent(context, ScannerActivity.class);
        intent.putExtra(TYPE_EXTRA, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ScannerType type = (ScannerType) getIntent().getSerializableExtra(TYPE_EXTRA);

        mrzScanner = (MRZScanner) getSupportFragmentManager().findFragmentById(R.id.scannerFragment);
        //mrzScanner.setScannerType(type);    // Options: [SCANNER_TYPE_MRZ, SCANNER_TYPE_DOC_IMAGE_ID, SCANNER_TYPE_DOC_IMAGE_PASSPORT]
        mrzScanner.setScannerType(ScannerType.SCANNER_TYPE_MRZ);    // Options: [SCANNER_TYPE_MRZ, SCANNER_TYPE_DOC_IMAGE_ID, SCANNER_TYPE_DOC_IMAGE_PASSPORT]
        MRZScanner.setIDActive(true);                               // Enable/disable the ID document type
        MRZScanner.setPassportActive(true);                         // Enable/disable the Passport document type
        MRZScanner.setVisaActive(false);                             // Enable/disable the Visa document type
        MRZScanner.setMaxThreads(2);                                // Set the max CPU threads that the scanner can use
        MRZScanner.setEnableVibrationOnSuccess(true);
        //MRZScanner.setDateFormat("dd.MM.YYYY");

        mrzScanner.toggleFlash(false);
        mrzScanner.enableScanFromGallery(false);
        MRZScanner.setEnableUpsideDownScanning(true);
        //mrzScanner.setScanningRectangle(5,35,90,30); //Por defecto
        //mrzScanner.setCustomImageOverlay(R.drawable.logo_gehd);
        MRZScanner.setIgnoreInvalidCheckDigitsEnabled(true);
        mrzScanner.setShowFlashButton(true);




        //MRZScanner.registerWithLicenseKey(this, "FAB6EFBBE1807CE52B8DA98ED3C1290BD076F75F0D604B820ADF9113CA78C89EF43DDBA01C98510F42005F2B80E37597");
        MRZScanner.registerWithLicenseKey(this, "14FF5B9FF1AE457B370122C3ABD9625C69BF96CD0C391838037E8A34AA61B696F8B1F3C5B3CF7150FC20929F8F25E09EF43DDBA01C98510F42005F2B80E37597", new MRZLicenceResultListener() {
            @Override
            public void onRegisterWithLicenceResult(int result, @Nullable String errorMessage) {
                if (errorMessage != null) {
                    Log.i(TAG, "Result code: " + result + ". " + errorMessage);
                } else {
                    Log.i(TAG, "Result code: " + result + ". Registration successful.");
                }
            }
        });
    }


            public void getScannerTypeKey(){
                Intent intent = getIntent();
                imageRequest = intent.getIntExtra("imagekey",-1);
                if(imageRequest==2 || imageRequest==3)
                     {
                    mrzScanner.setScannerType(ScannerType.SCANNER_TYPE_DOC_IMAGE_ID);
                     }
                else
                    {
                    mrzScanner.setScannerType(ScannerType.SCANNER_TYPE_MRZ);
                    }
            }

            @Override
            public void successfulScanWithResult(MRZResultModel mrzResultModel) {

                if(MainActivity.getBeep()) {
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    toneGen1.startTone(ToneGenerator.TONE_PROP_ACK, 150);
                }

                successfulMrzScan = new JSONObject();
                try {

                    successfulMrzScan.put("document_type_raw", mrzResultModel.documentTypeRaw);
                    successfulMrzScan.put("document_type_readable", mrzResultModel.documentTypeReadable);
                    successfulMrzScan.put("issuing_country", mrzResultModel.issuingCountry);
                    successfulMrzScan.put("surname", mrzResultModel.surnamesReadable());
                    if(mrzResultModel.documentTypeRaw.equals("IR")){
                        successfulMrzScan.put("document_number",mrzResultModel.documentNumber + mrzResultModel.rawResult.charAt(24));
                    }else {
                        successfulMrzScan.put("document_number", mrzResultModel.documentNumber);
                    }
                    successfulMrzScan.put("nationality", mrzResultModel.nationality);
                    successfulMrzScan.put("dob_raw", mrzResultModel.dobRaw);
                    successfulMrzScan.put("dob_readable", mrzResultModel.dobReadable);
                    successfulMrzScan.put("sex", mrzResultModel.sex);
                    successfulMrzScan.put("est_issuing_date_raw", mrzResultModel.estIssuingDateRaw);
                    successfulMrzScan.put("expiration_date_raw", mrzResultModel.expirationDateRaw);
                    if(mrzResultModel.expirationDateRaw.equals("990101") && mrzResultModel.expirationDateReadable.equals("01.01.1999")){
                        successfulMrzScan.put("expiration_date_readable", "01.01.9999");
                    }else{
                        successfulMrzScan.put("expiration_date_readable", mrzResultModel.expirationDateReadable);
                    }
                    if(mrzResultModel.estIssuingDateReadable.equals("Unknown")){
                        successfulMrzScan.put("est_issuing_date_readable", "Desconocida");
                    }else{
                        successfulMrzScan.put("est_issuing_date_readable", mrzResultModel.estIssuingDateReadable);
                    }
                    successfulMrzScan.put("given_names_readable", mrzResultModel.givenNamesReadable());
                    successfulMrzScan.put("optionals", mrzResultModel.optionalsReadable());
                    successfulMrzScan.put("raw_mrz", mrzResultModel.rawResult);

                    sendResultMrz(successfulMrzScan);


                    /*
                    final AlertDialog alertDialog = new AlertDialog.Builder(ScannerActivity.this)
                            .setMessage(mrzResultModel.rawResult + "\r\n" + MRZScanner.sdkVersion())
                            .setCancelable(false)
                            .setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mrzScanner.resumeScanning();
                                }
                            })
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ScannerActivity.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .create();

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                    .setTextColor(getResources().getColor(R.color.colorPrimary));
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                    .setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });

                    alertDialog.show();

                    */









                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void successfulScanWithDocumentImage(Bitmap bitmap) {
                //sendResultImage(bitmap);
            }

//            @Override
            public void  successfulIdFrontImageScan(Bitmap fullImage, Bitmap portrait) {
                //successfulScanWithImages(fullImage, portrait);

            }

           @Override
            public void scanImageFailed() {

            }

            @Override
            public void permissionsWereDenied() {

            }
/*            private void sendResultImage(Bitmap bitmap) {
                Intent intent = new Intent(this, ScannedActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                if(imageRequest==2){
                    intent.putExtra("frontIDImage", byteArray);
                }
                if(imageRequest==3){
                    intent.putExtra("backIDImage", byteArray);
                }
                startActivity(intent);
            }
*/
            private void sendResultMrz(JSONObject successfulMrzScan) {
                Intent intent = getIntent();
                intent.putExtra("key", successfulMrzScan.toString());
                setResult(RESULT_OK, intent);
                finish();
            }

         

            @Override
            public void onBackPressed() {
                super.onBackPressed();
            }




}
