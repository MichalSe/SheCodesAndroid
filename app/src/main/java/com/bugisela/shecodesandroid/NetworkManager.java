package com.bugisela.shecodesandroid;

import android.util.JsonWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michalsela on 8/14/15.
 */
public class NetworkManager {

     public String sendLoginInfo(String userToken) throws IOException {
        String status = "";
        URL url = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out, userToken);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } finally {
                 urlConnection.disconnect();
             }

        return status;
    }
    public static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
    public static void writeStream(OutputStream out, String message) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginObject();
        writer.name("fbid").value(message);
        writer.endObject();
        writer.close();
        System.out.println(writer);
    }


}
