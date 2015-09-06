package com.bugisela.shecodesandroid;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michalsela on 8/14/15.
 */
public class NetworkManager extends AsyncTask<String, Void, String> {

     public static String sendLoginInfo(String userId) {
        String status = "";

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
    public static void writeStream(BufferedWriter out, String message) throws IOException {
//        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
//        writer.setIndent("  ");
//        writer.beginObject();
//        writer.name("fbid").value(message);
//        writer.endObject();
 //       writer.close();
//        System.out.println(writer);
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("fbcode", message));
        out.write(getQuery(params));
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String response = "";
        try {


            URL urlFB = new URL("https://www.facebook.com/dialog/oauth?client_id=492475584228147&redirect_uri=http://server.shecodesconnect.com/login/fb_login");
            URLConnection urlConnectionFB = urlFB.openConnection();
     //       urlConnection.setRequestProperty("Accept", "application/json");


            try {
                InputStream in = new BufferedInputStream(urlConnectionFB.getInputStream());
                String fbResponse = readStream(in);
                System.out.println(fbResponse);
            }
            finally {
              //  urlConnectionFB.disconnect();

            }



            URL url = new URL("http://server.shecodesconnect.com/login/fb_login");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Accept", "application/json");

          //  OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
         //   OutputStream os = urlConnection.getOutputStream();
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writeStream(out, params[0]);
         //   out.close();
            int status = urlConnection.getResponseCode();
            BufferedInputStream in;
            if (status >= 400 ) {
                in = new BufferedInputStream( urlConnection.getErrorStream() );
                System.out.println("error message: " + in.toString());
            } else {
                in = new BufferedInputStream( urlConnection.getInputStream() );
                response = readStream(in);
            }

        } catch (Exception e) {
            System.out.println("exception" + e);
        } finally{
            urlConnection.disconnect();
        }

        return response;
    }
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        System.out.println(result.toString());
        return result.toString();
    }
}
