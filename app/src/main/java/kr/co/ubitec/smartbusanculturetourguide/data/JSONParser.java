package kr.co.ubitec.smartbusanculturetourguide.data;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017-09-15.
 */

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public String getJSONStringFromUrl(URL url) throws IOException {
        String str = "";
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
        int responseCode = httpUrlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpUrlConnection.getInputStream();
            str = convertStreamToString(inputStream);
            return str;
        }
        return str;
    }


    public static String convertStreamToString(InputStream inputStream) {

        if (inputStream != null) {

            Writer writer = new StringWriter();

            char[] buffer = new char[1024];

            try {

                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                int n;

                while ((n = reader.read(buffer)) != -1) {

                    writer.write(buffer, 0, n);

                }

                return writer.toString();

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return null;

    }
}
