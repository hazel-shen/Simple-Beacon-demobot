package com.linecorp.beacamon.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpsConnection {

    Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    public String get(String url, String data,String contentType, String authorization) throws Exception {

        URL endpoint = new URL(url);
        HttpsURLConnection httpsUrlConnection =  (HttpsURLConnection) endpoint.openConnection();
        httpsUrlConnection.setRequestMethod("GET");
        httpsUrlConnection.setDoInput(true);
        httpsUrlConnection.setDoOutput(true);

        httpsUrlConnection.setRequestProperty("Content-Type", contentType);
        httpsUrlConnection.setRequestProperty("Authorization", authorization );

        String requestResult = flushData(data,  httpsUrlConnection);
        logger.info("Request" + url + " , Result: " + requestResult);

        return requestResult;

    }

    public String post(String url, String data,String contentType, String authorization) throws Exception{

        URL endpoint = new URL(url);
        HttpsURLConnection httpsUrlConnection =  (HttpsURLConnection) endpoint.openConnection();
        httpsUrlConnection.setRequestMethod("POST");
        httpsUrlConnection.setDoInput(true);
        httpsUrlConnection.setDoOutput(true);
        httpsUrlConnection.setRequestProperty("Content-Type", contentType);

        httpsUrlConnection.setRequestProperty("Authorization", authorization );

        String requestResult = flushData(data,  httpsUrlConnection);
        logger.info("Request" + url + " , Result: " + requestResult);

        return requestResult;
    }

    private String flushData (String data, HttpsURLConnection httpsUrlConnection) throws IOException {

        if(data != null && data.length() > 0) {
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(httpsUrlConnection.getOutputStream());
                dos.write(data.getBytes(Charset.forName("utf-8")));
                dos.flush();
            } finally {
                if(dos != null) dos.close();
            }
        }

        //get feedback
        InputStreamReader isr = null;
        BufferedReader br = null;
        String line;
        StringBuilder sb = new StringBuilder();


        try {
            isr = new InputStreamReader(httpsUrlConnection.getInputStream());
            br = new BufferedReader(isr);
            while( (line = br.readLine()) != null ) {
                sb.append(line);}
        } finally {
            if(br != null) br.close();
            if(isr != null) isr.close();
        }
        return sb.toString();
    }
}