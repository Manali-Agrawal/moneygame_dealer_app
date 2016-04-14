package com.example.dealerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 3/15/16.
 */
public class CricketTransactionAdapter extends BaseAdapter {
        Context context;
        List<TransactionDetailsGetSet> historyList;

        public CricketTransactionAdapter(Context context,List<TransactionDetailsGetSet> historyList)
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
            TextView mPaticular,mOdds,mChip;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup)
        {
            Holder viewHolder;
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_cricket_transaction,null);
                viewHolder = new Holder();
                viewHolder.mPaticular = (TextView) convertView.findViewById(R.id.prtclr);
                viewHolder.mOdds = (TextView) convertView.findViewById(R.id.odd);
                viewHolder.mChip = (TextView) convertView.findViewById(R.id.chip);


                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (Holder) convertView.getTag();
            }
            TransactionDetailsGetSet item = historyList.get(position);

            viewHolder.mPaticular.setText(item.getParticular());
            viewHolder.mOdds.setText(item.getOdds());
            viewHolder.mChip.setText(item.getChip());

            return convertView;
        }
    }

