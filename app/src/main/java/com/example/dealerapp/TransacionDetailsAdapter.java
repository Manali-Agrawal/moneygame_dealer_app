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
public class TransacionDetailsAdapter extends BaseAdapter {
    Context context;
    List<TransactionDetailsGetSet> historyList;

    public TransacionDetailsAdapter(Context context,List<TransactionDetailsGetSet> historyList)
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
        TextView mDigit,mChip;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        Holder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_layout_child,null);
            viewHolder = new Holder();
            viewHolder.mDigit = (TextView) convertView.findViewById(R.id.digit);
            viewHolder.mChip = (TextView) convertView.findViewById(R.id.chip);


            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }
        TransactionDetailsGetSet item = historyList.get(position);

        //viewHolder.mTransactionNo.setText(item.getNumber());
        viewHolder.mDigit.setText(item.getDigit());
        viewHolder.mChip.setText(item.getChip());

        return convertView;
    }
}

