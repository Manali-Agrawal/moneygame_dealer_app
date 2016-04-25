package com.example.dealerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 4/15/16.
 */
public class WeekListAdapter extends BaseAdapter {
    Context context;
    List<WeekListGetSet> weekList;

    public WeekListAdapter(Context context,List<WeekListGetSet> weekList)
    {
        this.context = context;
        this.weekList = weekList;
    }

    @Override
    public int getCount() {
        return weekList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return weekList.get(i).hashCode();
    }

    class Holder
    {
        TextView mPlayer, mChips, mWin, mComm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        Holder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_week_list,null);
            viewHolder = new Holder();
            viewHolder.mPlayer = (TextView) convertView.findViewById(R.id.textname);
            viewHolder.mChips = (TextView) convertView.findViewById(R.id.textchips);
            viewHolder.mWin = (TextView) convertView.findViewById(R.id.textwins);
            viewHolder.mComm = (TextView) convertView.findViewById(R.id.textcomm);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (Holder) convertView.getTag();
        }
        WeekListGetSet item = weekList.get(position);

        viewHolder.mPlayer.setText(item.getPlayer_nm());
        viewHolder.mChips.setText(item.getChips());
        viewHolder.mWin.setText(item.getWins());
        viewHolder.mComm.setText(item.getComm());

        return convertView;
    }
}

