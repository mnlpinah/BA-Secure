package com.panic.devandtech.panic;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.Result;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView escanner;
    private static final int MIS_PERMISOS = 1 ;
    private String mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verifica los permisos para utilizar la camara
        int permisos = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MIS_PERMISOS);

            }
        }

        //Verifica los permisos para utilizar la camara
        permisos = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.INTERNET},
                        MIS_PERMISOS);

            }
        }
    }

    //Escanea el codigo QR
    public void EscannerQr(View view ){
        escanner = new ZXingScannerView(this); //Objeto para el escaner
        setContentView(escanner);
        escanner.setResultHandler(this); //Obtiene el texto del codigo
        escanner.startCamera(); //Inicia la camara para escanear
    }

    // Envia la alerta al servidor del sistema de seguridad
    public void enviarAlerta(View view){
        //Crea cuadro de dialogo con boton de aceptacion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Envio");
        builder.setMessage("Alerta Enviada");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent ok = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(ok);
                HttpClient httpcliente = new DefaultHttpClient();

                HttpPost httppost = new HttpPost("http://10.24.169.230/TestRegistro.php");

                try{
                    List<NameValuePair> postValores = new ArrayList<NameValuePair>(2);
                    postValores.add(new BasicNameValuePair("registro", mensaje));

                    System.out.print(mensaje);

                    httppost.setEntity(new UrlEncodedFormEntity(postValores));

                    HttpResponse respuesta =  httpcliente.execute(httppost);
                }catch(IOException e){

                }
            }
        });
        AlertDialog envia = builder.create();
        envia.show();
    }

    //Pausa la camara una vez escaneado
    @Override
    protected void onPause() {
        super.onPause();
        escanner.stopCamera();// detiene la camara
    }

    //Obtiene el resultado de escanear el codigo
    @Override
    public void handleResult(Result result) {
        mensaje = result.getText(); //Guarda el resultado del escaner

        //Crea cuado de dialogo de confirmacion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Captura");
        builder.setMessage("Codigo escaneado con exito " + mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Regresa a la actividad principal
                Intent ok = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ok);
            }
        });
        AlertDialog captura = builder.create();
        captura.show();
        escanner.resumeCameraPreview(this);
    }

    //Evalua que se haya consedido el permiso de utilizar la camara
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MIS_PERMISOS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }


    //Termina la aplicacion
    public void salir(View view){
        finish();
    }
}
