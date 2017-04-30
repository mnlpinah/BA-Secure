package com.panic.devandtech.panic;

/**
 * Created by Jeix on 30/04/2017.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpHandler {

    public String post(String posturl){
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(posturl);

            HttpResponse respuesta = httpclient.execute(httppost);
            HttpEntity entidad = respuesta.getEntity();

            String texto = EntityUtils.toString(entidad);

            return texto;
        }catch (Exception e){};
    }
}
