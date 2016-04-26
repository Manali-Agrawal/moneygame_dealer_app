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
import java.util.List;

public class CricketTransactionDetails extends AppCompatActivity {

    private ListView mList;
    private List<HistoryGetSet> list = new ArrayList<>();
    private TimeSlotAdapter adapter;
    List<TransactionDetailsGetSet> transList;
    String transaction_id, tr_time, reslt, mtch_nm, team1, team2, win, loss;
    private static int flag=0;
    String formattedDate, gamenm, matchid, mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_transaction_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mList= (ListView) findViewById(R.id.transactionHistory);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        matchid = this.getIntent().getStringExtra("match_id");
        mid = this.getIntent().getStringExtra("mid");
        team1 = this.getIntent().getStringExtra("teama");
        team2 = this.getIntent().getStringExtra("teamb");
        gamenm = this.getIntent().getStringExtra("game");
        try {
            if (this.getIntent().hasExtra("date")) {

                formattedDate=this.getIntent().getStringExtra("date");
            } else {
                formattedDate = df.format(cal.getTime());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.i("url", "" + getString(R.string.game_wise) + getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("player_id", "") + "&date=" + formattedDate + "&match_id=" + matchid + "&m_id=" + mid);
        getTransactionDetails(getString(R.string.game_wise) + getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("player_id", "") + "&date=" + formattedDate + "&match_id=" + matchid + "&m_id=" +mid);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HistoryGetSet item = list.get(i);
                transaction_id = item.getTransactionNo();
                Log.i("url", "" + getString(R.string.cricket_transaction_details) + item.getTransactionNo());
                getDetails(getString(R.string.cricket_transaction_details) + item.getTransactionNo());
            }
        });
    }


    private void showDetails(List<TransactionDetailsGetSet> List,String mnm,String tno, String time, String res, String whichBet,String payout, String wins, String los)
    {
// custom dialog
        final Dialog dialog = new Dialog(CricketTransactionDetails.this);
        dialog.setContentView(R.layout.cricket_transaction);
        dialog.setTitle("Transaction Details");


        TextView mDigit,mMtch,mTransNo,mDrawTime,mTransTime,mResult,mTotal,mBetText,mWin,mLoss;
        ListView mListView;

//        mDigit = (TextView) dialog.findViewById(R.id.digit);
        mMtch = (TextView) dialog.findViewById(R.id.mtch_nm);
        mTransNo = (TextView) dialog.findViewById(R.id.trans_id);
        mDrawTime = (TextView) dialog.findViewById(R.id.draw_time);
        mTransTime = (TextView) dialog.findViewById(R.id.trans_time);
        mResult = (TextView) dialog.findViewById(R.id.result);
        mTotal =  (TextView) dialog.findViewById(R.id.total);
        mBetText = (TextView) dialog.findViewById(R.id.betText);
        mWin = (TextView) dialog.findViewById(R.id.win);
        mLoss = (TextView) dialog.findViewById(R.id.loss);

        mListView = (ListView) dialog.findViewById(R.id.list);

        CricketTransactionAdapter adapter = new CricketTransactionAdapter(getApplicationContext(),List);
        adapter.notifyDataSetChanged();
        mListView.setAdapter(adapter);

        mMtch.setText(mnm);
        mTransNo.setText(tno);
        mTransTime.setText(time);
        if(res.equals("1")){
            mResult.setText("Win");
        }
        else{
            mResult.setText("Loss");
        }


        int chip=0;
        for(TransactionDetailsGetSet item:transList)
        {
            chip+=(int) Math.round(Double.parseDouble(item.getChip()));
        }
        mTotal.setText(""+chip);

        mBetText.setText(whichBet);
        //double amt = Integer.parseInt(Chip)*8.5;

        mWin.setText(wins);
        mLoss.setText(los);

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

            Log.i("url", url);
            final ProgressDialog pDialog = new ProgressDialog(CricketTransactionDetails.this);
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
                        Log.i("res", "" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("true")) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = object.getJSONArray("data_weekly");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    HistoryGetSet item = new HistoryGetSet();
                                    JSONObject trnsaction = jsonArray.getJSONObject(i);


//                                    team1 = trnsaction.getString("team_a");
//                                    team2 = trnsaction.getString("team_b");
                                    String game = trnsaction.getString("game_name");
                                    game = game.replace("_", " ");
                                    if (game.contains("team1")) {
                                        game = game.replace("team1", team1);
                                    } else if (game.contains("team2")) {
                                        game = game.replace("team2", team2);
                                    }

//                                    if (game.equals(gamenm)) {
                                        item.setGame(game);
                                        item.setTransactionNo(trnsaction.getString("transaction_id"));

                                        item.setAmount(trnsaction.getString("bet_amount"));
                                        if (trnsaction.getString("payout").equals("null")) {
                                            item.setTime("0");
                                        } else {
                                            item.setTime(trnsaction.getString("payout"));
                                        }
                                    list.add(item);
//                                }
                                }
                                adapter = new TimeSlotAdapter(getApplicationContext(), list);
                                mList.setAdapter(adapter);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No transaction present to display!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "something went wrong please try again!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Please check internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getDetails(String url)
    {

        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            Log.i("url", url);
            final ProgressDialog pDialog = new ProgressDialog(CricketTransactionDetails.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            final String TAG = "login";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    String payout="",digit,bAmt,wamt,whichBet="",transactionNo = null;
                    String dTime = "", tTime = "", result ="";
                    transList = new ArrayList<>();
                    try
                    {
                        Log.i("res", "" + response);

                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("true")) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = object.getJSONArray("data_weekly");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject trnsaction = jsonArray.getJSONObject(i);

                                    TransactionDetailsGetSet item = new TransactionDetailsGetSet();

                                    mtch_nm=trnsaction.getString("match_name");
                                    reslt=trnsaction.getString("result");
                                    item.setChip(trnsaction.getString("chips"));
                                    team1=trnsaction.getString("team_a");
                                    team2=trnsaction.getString("team_b");
                                    tr_time=trnsaction.getString("transaction_time");
                                    String per=trnsaction.getString("perticulars");
                                    per=per.replace("_"," ");
                                    if(per.contains("team 1")){
                                        per=per.replace("team 1",team1);
                                    }
                                    else if(per.contains("team 2")) {
                                        per=per.replace("team 2",team2);
                                    }
                                    item.setParticular(per);
                                    item.setOdds(trnsaction.getString("odds"));
                                    whichBet = trnsaction.getString("game_name");
                                    whichBet = whichBet.replace("_"," ");
                                    if(whichBet.contains("team1")){
                                        whichBet=whichBet.replace("team1",team1);
                                    }
                                    else if(whichBet.contains("team2")){
                                        whichBet=whichBet.replace("team2",team2);
                                    }

                                    win= trnsaction.getString("win");
                                    loss= trnsaction.getString("loss");
                                    transList.add(item);

//                                    transactionNo = trnsaction.getString("transaction_id");
                                    if (trnsaction.getString("total_wins").equals("0.00")) {
                                        flag=0;
                                        payout = trnsaction.getString("total_bet");
                                    } else {
                                        flag=1;
                                        payout = trnsaction.getString("total_wins");
                                    }

                                }
                                showDetails(transList,mtch_nm, transaction_id,tr_time, reslt,  whichBet,payout, win, loss);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No transaction present to display!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "please check internet connetion!!!", Toast.LENGTH_SHORT).show();
        }
    }
}




