package de.htwds.mada.foodsharing;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Arax on 06.02.2015.
 */

public class JSONParser {
    public static final String LOG=JSONParser.class.getName();

    static InputStream is = null;
    static JSONObject jObj = new JSONObject();
    static String json = "";

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String UTF = "utf-8";
    private static final String ISO = "iso-8859-1";

    public JSONParser() {
    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
        try {
            if (method.equals(POST)) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } else if (method.equals(GET)) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, UTF);
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (Exception e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, "Error in http connection " + errorMessage);
            try {
                jObj.put("success", false);
                jObj.put("message", errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }
        /*
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, ISO), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, "Error converting result " + errorMessage);
            try {
                jObj.put("success", false);
                jObj.put("message", errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        try{
            jObj = new JSONObject(json);
            Log.i(LOG,"Success: "+jObj.optInt("success", -1)+
                    ", message: "+jObj.optString("message") + " " + json);
            if (jObj.getInt("success") == 1) {
                jObj.put("success", true);
            }
            else
            {
                String errorMessage=jObj.getString("message");
                jObj.put("success", false);
                jObj.put("message", errorMessage);
            }
        } catch (JSONException e) {
            String errorMessage=e.getLocalizedMessage();
            Log.e(LOG, "Error parsing result string " + json + " " + errorMessage);
            try {
                jObj.put("success", false);
                jObj.put("message", errorMessage);
            } catch (JSONException ignored) { }
            return jObj;
        }

        return jObj;
    }
}
