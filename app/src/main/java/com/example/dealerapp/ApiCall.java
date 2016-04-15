package com.example.dealerapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Sandesh on 22-Nov-15.
 */
public class ApiCall
{

    private ConnectionDetector testConnection;
    private StringBuilder Response=new StringBuilder();
    Context context;
    public ApiCall(Context context)
    {
        this.context=context;
        testConnection=new ConnectionDetector(context);
    }


    public String HttpPost(String apiUrl,HashMap<String,String> parameters)
    {
        try {
            boolean connectionStatus=testConnection.isConnectingToInternet();
            if (connectionStatus) {

                Log.i("url", "" + apiUrl);
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Content-Type","application/json");
                //conn.setRequestProperty("Content-Type","multipart/form-data");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.i("values", "" + getQuery(parameters));
                writer.write(getQuery(parameters));
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                Log.i("Result", "" + conn.getResponseCode());
                int ResponseCode = conn.getResponseCode();
                if (ResponseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Response.append(line);
                    }
                } else {//{"status":true,"message":"Bets Placed Successfully"}
                    String r = "{\"status\":false,\"message\":\"" + "Resonse Code" + ResponseCode + "\"}";//"{\"status\":true,\"message\":Resonse Code "+ResponseCode+"}";
                    Response.append(r);//"{\"status\":flase,\"message\":Resonse Code "+ResponseCode+"}");
                }
            }
            else
            {
                String r ="{\"status\":false,\"message\":\"Internet Connection Not Present!!!\"}";
                Response.append(r);
            }
        }
        catch (UnknownHostException e)
        {
            String r = "{\"status\":false,\"message\":\"Please check internet connection!!!\"}";
            Response.append(r);
        }
        catch (SocketTimeoutException e)
        {
            String r = "{\"status\":false,\"message\":\"Connection Timeout!!!\"}";
            Response.append(r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String r ="{\"status\":false,\"message\":\"Something went wrong please try again!!!\"}";

                Response.append(r);

        }
        return Response.toString();
    }


    private String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String pair : params.keySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(pair), "UTF-8"));
        }

        return result.toString();
    }


    private String getQueryCancelBet(HashMap<String,Integer> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String pair : params.keySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair, "UTF-8"));
            result.append("=");
            result.append(params.get(pair));
        }

        return result.toString();
    }

    public String HttpPostCancelBet(String apiUrl,HashMap<String,Integer> parameters)
    {
        try {
            boolean connectionStatus=testConnection.isConnectingToInternet();
            if (connectionStatus) {

                Log.i("url", "" + apiUrl);
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Content-Type","application/json");
                //conn.setRequestProperty("Content-Type","multipart/form-data");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.i("values", "" + getQueryCancelBet(parameters));
               /* if(apiUrl.equals("http://lottery.pixmadness.in/api/players/login"))
                {*/
                    writer.write(getQueryCancelBet(parameters));
                /*}
                else {
                    writer.write("player_id=1&data[0][digit]=1&data[0][bet_amount]=100");
                }*/
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                Log.i("Result", "" + conn.getResponseCode());
                int ResponseCode = conn.getResponseCode();
                if (ResponseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Response.append(line);
                    }
                } else {//{"status":true,"message":"Bets Placed Successfully"}
                    String r = "{\"status\":false,\"message\":\"" + "Resonse Code" + ResponseCode + "\"}";//"{\"status\":true,\"message\":Resonse Code "+ResponseCode+"}";
                    Response.append(r);//"{\"status\":flase,\"message\":Resonse Code "+ResponseCode+"}");
                }
            }
            else
            {
                String r ="{\"status\":false,\"message\":\"Internet Connection Not Present!!!\"}";
                Response.append(r);
            }
        }
        catch (UnknownHostException e)
        {
            String r = "{\"status\":false,\"message\":\"Please check internet connection!!!\"}";
            Response.append(r);
        }
        catch (SocketTimeoutException e)
        {
            String r = "{\"status\":false,\"message\":\"Connection Timeout!!!\"}";
            Response.append(r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String r ="{\"status\":false,\"message\":\"Something went wrong please try again!!!\"}";

            Response.append(r);

        }
        return Response.toString();
    }



    public String HttpGet(String apiUrl)
    {
        try {
            boolean connectionStatus=testConnection.isConnectingToInternet();
            if (connectionStatus) {

                URL url = new URL(apiUrl);
                URLConnection urlConn = url.openConnection();
                Log.i("url", "" + apiUrl);
                HttpURLConnection conn = (HttpURLConnection) urlConn;
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("GET");
                conn.connect();

                Log.i("Result", "" + conn.getResponseCode());
                int ResponseCode = conn.getResponseCode();
                if (ResponseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Response.append(line);
                    }
                } else {//{"status":true,"message":"Bets Placed Successfully"}
                    String r = "{\"status\":false,\"message\":\"" + "Resonse Code" + ResponseCode + "\"}";//"{\"status\":true,\"message\":Resonse Code "+ResponseCode+"}";
                    Response.append(r);//"{\"status\":flase,\"message\":Resonse Code "+ResponseCode+"}");
                }
            }
            else
            {
                String r ="{\"status\":false,\"message\":\"Internet Connection Not Present!!!\"}";
                Response.append(r);
            }
        }
        catch (SocketTimeoutException e)
        {
            String r = "{\"status\":false,\"message\":\"Connection Timeout!!!\"}";
            Response.append(r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String r ="{\"status\":false,\"message\":\"Something went wrong please try again!!!\"}";

            Response.append(r);

        }
        return Response.toString();
    }

    public String HttpPost(String apiUrl,String parameter)
    {
        try {
            boolean connectionStatus=testConnection.isConnectingToInternet();
            if (connectionStatus) {

                Log.i("url", "" + apiUrl);
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Content-Type","application/json");
                //conn.setRequestProperty("Content-Type","multipart/form-data");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.i("values", "" + parameter.toString());

                writer.write(parameter.toString());

                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                Log.i("Result", "" + conn.getResponseCode());
                int ResponseCode = conn.getResponseCode();
                if (ResponseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Response.append(line);
                    }
                } else {//{"status":true,"message":"Bets Placed Successfully"}
                    String r = "{\"status\":false,\"message\":\"" + "Resonse Code" + ResponseCode + "\"}";//"{\"status\":true,\"message\":Resonse Code "+ResponseCode+"}";
                    Response.append(r);//"{\"status\":flase,\"message\":Resonse Code "+ResponseCode+"}");
                }
            }
            else
            {
                String r ="{\"status\":false,\"message\":\"Internet Connection Not Present!!!\"}";
                Response.append(r);
            }
        }
        catch (SocketTimeoutException e)
        {
            String r = "{\"status\":false,\"message\":\"Connection Timeout!!!\"}";
            Response.append(r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String r ="{\"status\":false,\"message\":\"Something went wrong please try again!!!\"}";

            Response.append(r);

        }
        return Response.toString();
    }
}
