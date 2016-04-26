package com.example.dealerapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class CombinedAccounts extends AppCompatActivity {

    ListView listView;
    TextView total_bets, winnings, profit_loss;
    String player_id, week;
    int bet=0, win=0, prftlos=0;
    List<HistoryGetSet> historyList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cricket_accounts);


        listView = (ListView) findViewById(R.id.list);
        total_bets = (TextView) findViewById(R.id.txttotalbets);
        winnings = (TextView) findViewById(R.id.txtwinnings);
        profit_loss = (TextView) findViewById(R.id.txtprftlos);

        player_id = this.getIntent().getStringExtra("player_id");
        week = this.getIntent().getStringExtra("week");

        getHistory();


    }

    private void getHistory()
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";

            try {

                String url = getString(R.string.url_combined_weekly) + player_id+"&week="+week;
                Log.i("url", "" + url);
                final ProgressDialog pDialog = new ProgressDialog(CombinedAccounts.this);
                pDialog.setMessage("Loading...");
                pDialog.show();
                final String TAG = "login";
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        try {
                            Log.i("response", "" + response);
                            if(response != null)
                            {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getString("status").equals("true"))
                                {
                                    JSONObject jsonObject1= jsonObject.getJSONObject("data");
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data_weekly");
                                    for(int i=0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject item = jsonArray.getJSONObject(i);
                                        int pl=0;
                                        String pay;
                                        HistoryGetSet rowItem = new HistoryGetSet();
                                        rowItem.setAmount(item.getString("bet_amount"));
                                        rowItem.setDate(item.getString("date"));
                                        rowItem.setTotal_bet(item.getString("total_bet"));
                                        rowItem.setTotal_wins(item.getString("total_wins"));
                                        rowItem.setPayout(item.getString("payout"));
                                        if(item.getString("payout").equals("null")) {
                                            pay="0";
                                        }
                                        else{
                                            pay=item.getString("payout");
                                        }
                                        pl = (int) Math.round(Double.parseDouble(pay)) - (int) Math.round(Double.parseDouble(item.getString("bet_amount")));
                                        rowItem.setProftlos(String.valueOf(pl));

                                        String ttlbet =item.getString("bet_amount");
                                        ttlbet = ttlbet.replace(",","");
                                        bet+= (int) Math.round(Double.parseDouble(ttlbet));
                                        Log.i("bet", "" + bet);
                                        String ttlwin = pay;//item.getString("payout");
                                        ttlwin = ttlwin.replace(",","");
                                        win+= (int) Math.round(Double.parseDouble(ttlwin));
                                        prftlos+= pl;

                                    /*String first = item.getString("first_digit");
                                    String second = item.getString("second_digit");
                                    String third = item.getString("jodi_digit");
                                    //Toast.makeText(getActivity(),"1 = "+first+" 2 = "+second+" 3 = "+third,Toast.LENGTH_SHORT).show();
                                    if(!first.equals("null"))
                                    {
                                        if(item.getString("game_type").equals("1")) {
                                            rowItem.setNumber(first+"(I)");
                                        }
                                        else if(item.getString("game_type").equals("2"))
                                        {
                                            rowItem.setNumber(first+"(II)");
                                        }
                                        else if(item.getString("game_type").equals("3"))
                                        {
                                            rowItem.setNumber(first+"(III)");
                                        }
                                    }
                                    else if(!second.equals("null")) {

                                        if(item.getString("game_type").equals("1")) {
                                            rowItem.setNumber(second+"(I)");
                                        }
                                        else if(item.getString("game_type").equals("2"))
                                        {
                                            rowItem.setNumber(second+"(II)");
                                        }
                                        else if(item.getString("game_type").equals("3"))
                                        {
                                            rowItem.setNumber(second+"(III)");
                                        }

                                    }
                                    else if(!third.equals("null"))
                                    {
                                        if(item.getString("game_type").equals("1")) {
                                            rowItem.setNumber(third+"(I)");
                                        }
                                        else if(item.getString("game_type").equals("2"))
                                        {
                                            rowItem.setNumber(third+"(II)");
                                        }
                                        else if(item.getString("game_type").equals("3"))
                                        {
                                            rowItem.setNumber(third+"(III)");
                                        }

                                    }*/

                                  /*  String[] dateTime = item.getString("timeslot").split(" ");
                                    String[] time = dateTime[1].split(":");
                                    String[] timeSlot = time[1].split(":");
                                    rowItem.setTime(time[0]+":"+timeSlot[0]);


                                    rowItem.setResult(item.getString("result"));

                                    if(item.getString("result").equals("1")) {
                                    if(item.getString("game_type").equals("1"))
                                    {
                                        double amount = Double.parseDouble(item.getString("bet_amount")) * 8.5;

                                        rowItem.setCharge(""+amount);
                                    }
                                    else if(item.getString("game_type").equals("2"))
                                    {
                                        double amount = Double.parseDouble(item.getString("bet_amount")) * 8.5;

                                        rowItem.setCharge(""+amount);
                                    }
                                    else if(item.getString("game_type").equals("3"))
                                    {
                                        double amount = Double.parseDouble(item.getString("bet_amount")) * 85;

                                            rowItem.setCharge("" + amount);
                                        }
                                    }
                                    else{
                                        rowItem.setCharge("-");
                                    }*/

                                        historyList.add(rowItem);

                                    }
                                    HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(),historyList);
                                    listView.setAdapter(adapter);
                                    total_bets.setText(""+bet);
                                    winnings.setText(""+win);
                                    profit_loss.setText(""+prftlos);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "something went wrong, please try again!!!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            pDialog.hide();
                            Toast.makeText(getApplicationContext(), "something went wrong please try again!!!", Toast.LENGTH_SHORT).show();
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
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "please check internet connetion!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
