package com.example.dealerapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TansactionWiseHistory extends AppCompatActivity {

    private ListView mList;
    private List<HistoryGetSet> list = new ArrayList<>();
    private TimeSlotAdapter adapter;
    List<TransactionDetailsGetSet> transList;
    String transaction_id, formattedDate;
    private static int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tansaction_wise_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mList= (ListView) findViewById(R.id.transactionHistory);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (this.getIntent().hasExtra("date")) {
                String dte = this.getIntent().getStringExtra("date");
                Date dt=df.parse(dte);
                formattedDate=df.format(dt);
            } else {
                formattedDate = df.format(cal.getTime());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.i("url",""+getString(R.string.get_transaction) +getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("player_id", "") + "&date="+formattedDate+"&draw_time=" + getIntent().getStringExtra("timeslot"));
        getTransactionDetails(getString(R.string.get_transaction) + getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("player_id", "") + "&date=" + formattedDate + "&draw_time=" + getIntent().getStringExtra("timeslot"));

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               /* HistoryGetSet item = list.get(i);
                transaction_id = item.getTransactionNo();
                Log.i("url",""+getString(R.string.get_specific_transaction)+item.getTransactionNo()+ "&date="+formattedDate+"&draw_time=" + getIntent().getStringExtra("timeslot"));
                getDetails(getString(R.string.get_specific_transaction) + item.getTransactionNo() + "&date=" + formattedDate + "&draw_time=" + getIntent().getStringExtra("timeslot"));*/
            }
        });
    }


    private void showDetails(List<TransactionDetailsGetSet> List,String tno,String dTime,String tTime,String result,String whichBet,String payout)
    {
// custom dialog
        final Dialog dialog = new Dialog(TansactionWiseHistory.this);
        dialog.setContentView(R.layout.transaction_details_custom_layout);
        dialog.setTitle("Transaction Details");

        TextView mDigit,mChip,mTransNo,mDrawTime,mTransTime,mResult,mTotal,mBetText,mWin,mLoss;
        ListView mListView;

//        mDigit = (TextView) dialog.findViewById(R.id.digit);
        //      mChip = (TextView) dialog.findViewById(R.id.chip);
        mTransNo = (TextView) dialog.findViewById(R.id.trans_id);
        mDrawTime = (TextView) dialog.findViewById(R.id.draw_time);
        mTransTime = (TextView) dialog.findViewById(R.id.trans_time);
        mResult = (TextView) dialog.findViewById(R.id.result);
        mTotal =  (TextView) dialog.findViewById(R.id.total);
        mBetText = (TextView) dialog.findViewById(R.id.betText);
        mWin = (TextView) dialog.findViewById(R.id.win);
        mLoss = (TextView) dialog.findViewById(R.id.loss);

        mListView = (ListView) dialog.findViewById(R.id.list);

        TransacionDetailsAdapter adapter = new TransacionDetailsAdapter(getApplicationContext(),List);
        adapter.notifyDataSetChanged();
        mListView.setAdapter(adapter);
        /*mDigit.setText(digit);
        mChip.setText(chip);*/
        mTransNo.setText(tno);
        mDrawTime.setText(dTime);
        mTransTime.setText(tTime);
        mResult.setText(result);

        int chip=0;
        for(TransactionDetailsGetSet item:transList)
        {
            chip+=(int) Math.round(Double.parseDouble(item.getChip()));
        }
        mTotal.setText(""+chip);

        if(whichBet.equals("Single Digit Second")) {
            mBetText.setText("Single Digit Second");
            //double amt = Integer.parseInt(Chip)*8.5;

        }
        else if(whichBet.equals("Single Digit First"))
        {
            mBetText.setText("Single Digit First");
            //double amt = Integer.parseInt(mChip.getText().toString()) * 8.5;
            //    mTotal.setText(mChip.getText().toString());
        }
        else{
            mBetText.setText("Jodi");
        }

        if(flag==0)
        {
            mWin.setText("0");
            mLoss.setText(payout);
        }
        else
        {
            mWin.setText(payout);
            mLoss.setText("0");
        }

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }*/
        dialog.show();
    }
    private void getTransactionDetails(String url)
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            Log.i("url",url);
            final ProgressDialog pDialog = new ProgressDialog(TansactionWiseHistory.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            final String TAG = "login";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try
                    {
                        Log.i("res",""+response);
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("true")) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = object.getJSONArray("data_weekly");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                /*{
                                    "sr_no": 1,
                                        "user_code": "AUR00100001",
                                        "bet_amount": "200",
                                        "payout": null,
                                        "transaction_id": "AUR00100001L294",
                                        "total_bet": 200,
                                        "total_wins": 0
                                }*/
                                    HistoryGetSet item = new HistoryGetSet();
                                    JSONObject trnsaction = jsonArray.getJSONObject(i);

                                    item.setTransactionNo(trnsaction.getString("transaction_id"));
                                    item.setAmount(trnsaction.getString("bet_amount"));
                                    if (trnsaction.getString("payout").equals("null")) {
                                        item.setTime("0");
                                    } else {
                                        item.setTime(trnsaction.getString("payout"));
                                    }

                                    list.add(item);
                                }
                                adapter = new TimeSlotAdapter(getApplicationContext(), list);
                                mList.setAdapter(adapter);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"No transaction present to display!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Something went wrong please try again!",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "Request Timeout!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "History Not Present!!!", Toast.LENGTH_SHORT).show();
                    }
                    error.printStackTrace();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });
            strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
            AppControler.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"please check internet connetion!!!",Toast.LENGTH_SHORT).show();
        }
    }


    private void getDetails(String url)
    {

        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            Log.i("url",url);
            final ProgressDialog pDialog = new ProgressDialog(TansactionWiseHistory.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            final String TAG = "login";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    String payout="",digit,bAmt,wamt,whichBet="",transactionNo = null;
                    String dTime = "", tTime = "", result ="", drawTime= "", currentTime="";
                    transList = new ArrayList<>();
                    try
                    {
                        Log.i("res",""+response);

                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("true")) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = object.getJSONObject("data_weekly");
                            JSONArray jsonArray = jsonObject1.getJSONArray(transaction_id);
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject trnsaction = jsonArray.getJSONObject(i);

                                    TransactionDetailsGetSet item = new TransactionDetailsGetSet();
                                    if (trnsaction.getInt("first_digit")==999 && trnsaction.getInt("second_digit")==999) {

                                        item.setDigit(trnsaction.getString("jodi_digit"));
                                        item.setChip(trnsaction.getString("chips"));
                                        whichBet = "Jodi Digit";

                                    } else if(trnsaction.getInt("first_digit")==999 && trnsaction.getInt("jodi_digit")==999){

                                        item.setDigit(trnsaction.getString("second_digit"));
                                        item.setChip(trnsaction.getString("chips"));
                                        whichBet = "Single Digit Second";
                                    }
                                    else{
                                        item.setDigit(trnsaction.getString("first_digit"));
                                        item.setChip(trnsaction.getString("chips"));
                                        whichBet = "Single Digit First";
                                    }

                                    transList.add(item);

//                                    transactionNo = trnsaction.getString("transaction_id");

                                    dTime = trnsaction.getString("drawtime");
                                    currentTime = trnsaction.getString("time");
                                    String currentSplit[]=currentTime.split(" ");
                                    if(!dTime.equals("")){
                                        Calendar cal = Calendar.getInstance();
                                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                                        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss aa");
                                        String t=df.format(cal.getTime());
                                        Date d1=df.parse(dTime);
                                        Date d2=df1.parse(currentSplit[1]+" "+currentSplit[2]);

                                        if(currentSplit[0].equals(formattedDate)) {
                                            if (d1.compareTo(d2) < 0) {
                                                Log.i("if time", "" + d1.compareTo(d2));
                                                drawTime = dTime;
                                                result = trnsaction.getString("lucky_number");
                                            } else {
                                                Log.i("else time", "" + dTime);
                                            }
                                        }
                                        else{
                                            drawTime = dTime;
                                            result = trnsaction.getString("lucky_number");
                                        }

                                    }
                                    if (trnsaction.getString("total_wins").equals("0.00")) {
                                        flag=0;
                                        payout = trnsaction.getString("total_bet");
                                    } else {
                                        flag=1;
                                        payout = trnsaction.getString("total_wins");
                                    }
                                    tTime = trnsaction.getString("trans_time");

                                    //String digit,String chip,String tno,String dTime,String tTime,String result



                                }
                                showDetails(transList,transaction_id,drawTime,tTime,result,whichBet,payout);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No transaction present to display!",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "Request Timeout!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "History Not Present!!!", Toast.LENGTH_SHORT).show();
                    }
                    error.printStackTrace();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });
            strReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
            AppControler.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"please check internet connetion!!!",Toast.LENGTH_SHORT).show();
        }
    }
}
