package web.service;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by chc on 15.8.19.
 */
public class ConnectionService {
    private static final String SERVER_URL = "http://79.98.29.151:8080";
    private static final HttpClient httpClient = new DefaultHttpClient();

    private static List<NameValuePair> params = new ArrayList<NameValuePair>();
    private static ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();

    protected static Response getJSONPost(String url) throws ExecutionException, InterruptedException {
        return new GetJsonPostTask().execute(SERVER_URL+url).get();
    }

    protected static Response getJSONGet(String url) throws ExecutionException, InterruptedException {
        return new GetJsonGetTask().execute(SERVER_URL+url).get();
    }

    private static class GetJsonPostTask extends AsyncTask<String, String, Response>  {
        @Override
        protected Response doInBackground(String... urls) {
            StringBuilder builder = new StringBuilder();
            HttpPost httpPost = new HttpPost(urls[0]);
            addHeader("Content-Type", "application/x-www-form-urlencoded");

            for(NameValuePair h: headers) {
                httpPost.addHeader(h.getName(), h.getValue());
            }

            int statusCode = -1;
            try{
                if (!params.isEmpty()) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }
                HttpResponse response = httpClient.execute(httpPost);
                Log.d("post","executed post");
                StatusLine statusLine = response.getStatusLine();
                Log.d("post","get status line");
                statusCode = statusLine.getStatusCode();
                Log.d("post","get status =code");
                if(statusCode == 200){
                    HttpEntity entity = response.getEntity();
                    Log.d("post","get entity");
                    InputStream content = entity.getContent();
                    Log.d("post","get inputstream content");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                }
            }catch(ClientProtocolException e){
                Log.d("clientProtocol",e.getMessage());
                return new Response(statusCode,e.getMessage());
            } catch (IOException e){
                Log.d("IOException",e.getMessage());
                return new Response(statusCode,e.getMessage());
            }
            Log.d("success","successful");
            return new Response(statusCode,builder.toString());
        }

    }

    private static class GetJsonGetTask extends AsyncTask<String, String, Response> {
        @Override
        protected Response doInBackground(String... urls) {
            StringBuilder builder = new StringBuilder();
            HttpGet httpGet = new HttpGet(urls[0]);
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

            String combinedParams = "";
            if (!params.isEmpty()) {
                combinedParams += "?";

                for(NameValuePair p: params) {
                    combinedParams += p.getName() + "=" + p.getValue() + "&";
                }
                combinedParams.substring(combinedParams.lastIndexOf("&"));
            }

            int statusCode = 500;
            try{
                HttpResponse response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                statusCode = statusLine.getStatusCode();
                if(statusCode == 200){
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                } else {
                    return new Response(statusCode,builder.toString());
                }
            }catch(ClientProtocolException e){
                return new Response(statusCode,e.getMessage());
            } catch (IOException e){
                return new Response(statusCode,e.getMessage());
            }
            return new Response(statusCode,builder.toString());
        }

    }

    public static void addParam(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
    }

    public static void addHeader(String key, String value) {
        headers.add(new BasicNameValuePair(key, value));
    }
}
