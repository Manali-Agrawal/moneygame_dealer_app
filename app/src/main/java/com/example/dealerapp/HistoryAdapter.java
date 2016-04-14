package com.example.dealerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sandesh on 02-Dec-15.
 */
public class HistoryAdapter extends BaseAdapter
{
    Context context;
    List<HistoryGetSet> historyList;
    public HistoryAdapter(Context context,List<HistoryGetSet> historyList)
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
        TextView mDate,mTotalBet,mTotalWin,mPL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        Holder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_history,null);
            viewHolder = new Holder();
            viewHolder.mDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.mTotalBet = (TextView) convertView.findViewById(R.id.bets);
            viewHolder.mTotalWin = (TextView) convertView.findViewById(R.id.wins);
            viewHolder.mPL = (TextView) convertView.findViewById(R.id.p_l);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }
        HistoryGetSet item = historyList.get(position);

        viewHolder.mDate.setText(item.getDate());
        viewHolder.mTotalBet.setText(item.getAmount());
        if(item.getPayout().equals("null")){
            viewHolder.mTotalWin.setText("0");
        }
        else{
            viewHolder.mTotalWin.setText(item.getPayout());
        }

        viewHolder.mPL.setText(item.getProftlos());

       /* if(item.getResult().equals("0"))
        {
            convertView.setBackgroundColor(Color.GRAY);

        }
*/
        return convertView;
    }
}
