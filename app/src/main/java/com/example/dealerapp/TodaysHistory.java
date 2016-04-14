package com.example.dealerapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodaysHistory extends Fragment {

    ListView listView;
    TextView total_bets, winnings, profit_loss;
    int bet=0, win=0, prftlos=0;
    List<HistoryGetSet> historyList = new ArrayList<>();
    public TodaysHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todays_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.list);
        total_bets = (TextView) view.findViewById(R.id.txttotalbets);
        winnings = (TextView) view.findViewById(R.id.txtwinnings);
        profit_loss = (TextView) view.findViewById(R.id.txtprftlos);

        getHistory();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HistoryGetSet item = historyList.get(i);
                //item.getTimeSlotId();
                Log.i("userid", "" + item.getTimeSlotId() + " id " + getActivity().getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("player_id", ""));
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TodaysSummary fragment = new TodaysSummary();
                Bundle args = new Bundle();
                args.putString("date", item.getDate());
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.containar1, fragment);
                fragmentTransaction.commit();
            }
        });

    }

    private void getHistory()
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if(connectionDetector.isConnectingToInternet()) {
            String tag_string_req = "string_req";
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
            //calendar.setFirstDayOfWeek(Calendar.MONDAY);
            //calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            String[] days = new String[15];
            for (int i = 0; i < 15; i++)
            {
                days[i] = format.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            String week= days[14]+"%20To%20"+days[0];
            try {

             String url = getString(R.string.get_history_by_week) + getActivity().getSharedPreferences(getString(R.string.prefrence), Context.MODE_PRIVATE).getString("player_id", "")+"&week="+week;
                Log.i("url", "" + url);
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for(int i=0; i < jsonArray.length(); i++)
                                {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    int pl=0;
                                    HistoryGetSet rowItem = new HistoryGetSet();
                                    rowItem.setAmount(item.getString("bet_amount"));
                                    rowItem.setDate(item.getString("date"));
                                    rowItem.setTotal_bet(item.getString("total_bet"));
                                    rowItem.setTotal_wins(item.getString("total_wins"));
                                    rowItem.setPayout(item.getString("payout"));

                                    String wins = item.getString("payout");
                                    wins=wins.replace(",","");
                                    String bets= item.getString("bet_amount");
                                    bets=bets.replace(",","");
                                    if(!wins.equals("null")) {
                                        pl = (int) Math.round(Double.parseDouble(wins)) - (int) Math.round(Double.parseDouble(bets));
                                        rowItem.setProftlos(String.valueOf(pl));
                                        String ttlbet =item.getString("bet_amount");
                                        ttlbet = ttlbet.replace(",","");
                                        bet+= (int) Math.round(Double.parseDouble(ttlbet));
                                        String ttlwin =item.getString("payout");
                                        ttlwin = ttlwin.replace(",","");
                                        win+= (int) Math.round(Double.parseDouble(ttlwin));
                                        prftlos+= pl;
                                    }


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
                                HistoryAdapter adapter = new HistoryAdapter(getActivity(),historyList);
                                listView.setAdapter(adapter);
                                total_bets.setText(""+bet);
                                winnings.setText(""+win);
                                profit_loss.setText(""+prftlos);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "something went wrong, please try again!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        pDialog.hide();
                        Toast.makeText(getActivity(), "something went wrong please try again!!!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getActivity(), "Request Timeout!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "History Not Present!!!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "please check internet connetion!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
