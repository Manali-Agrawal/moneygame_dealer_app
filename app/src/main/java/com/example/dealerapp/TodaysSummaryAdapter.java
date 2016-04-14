package com.example.dealerapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sandesh on 22-Dec-15.
 */
public class TodaysSummaryAdapter extends BaseAdapter {
    Context context;
    List<HistoryGetSet> historyList;

    public TodaysSummaryAdapter(Context context,List<HistoryGetSet> historyList)
    {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return historyList.get(i).hashCode();
    }

    class Holder
    {
        TextView mTransactionNo,mAmt,mBetTime,mResult;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        Holder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.today_summary_child,null);
            viewHolder = new Holder();
            viewHolder.mTransactionNo = (TextView) convertView.findViewById(R.id.transactionNo);
            viewHolder.mAmt = (TextView) convertView.findViewById(R.id.transactionAmt);
            viewHolder.mBetTime = (TextView) convertView.findViewById(R.id.time);
            viewHolder.mResult = (TextView) convertView.findViewById(R.id.result);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }
        HistoryGetSet item = historyList.get(position);

        //viewHolder.mTransactionNo.setText(item.getNumber());
        viewHolder.mBetTime.setText(item.getTime());
        viewHolder.mAmt.setText(item.getAmount());
        if(item.getResult().equals("null")){
            viewHolder.mResult.setText("0");
        }
        else {
            viewHolder.mResult.setText(item.getResult());
        }
       /* if(item.getResult().equals("0"))
        {
            viewHolder.mResult.setBackgroundColor(Color.parseColor("#EF9A9A"));
        }
        else
        {*/
            viewHolder.mResult.setBackgroundColor(Color.parseColor("#9CCC65"));
//        }

        return convertView;
    }
}
