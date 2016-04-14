package com.example.dealerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sandesh on 23-Dec-15.
 */
public class TimeSlotAdapter extends BaseAdapter {
    Context context;
    List<HistoryGetSet> historyList;

    public TimeSlotAdapter(Context context, List<HistoryGetSet> historyList)
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
        TextView mTransactionNo,mChip,mPayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        Holder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transaction_child,null);
            viewHolder = new Holder();
            viewHolder.mTransactionNo = (TextView) convertView.findViewById(R.id.transactionNo);
            viewHolder.mChip = (TextView) convertView.findViewById(R.id.chip);
            viewHolder.mPayout = (TextView) convertView.findViewById(R.id.payout);


            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }
        HistoryGetSet item = historyList.get(position);

        viewHolder.mTransactionNo.setText(item.getTransactionNo());
        viewHolder.mPayout.setText(item.getTime());
        viewHolder.mChip.setText(item.getAmount());


        return convertView;
    }
}
