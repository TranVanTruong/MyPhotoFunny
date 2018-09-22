package com.btech.funnyphoto.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.btech.funnyphoto.R;


public class SpinnerAdapter extends BaseAdapter {

    String[] array;
    Context mContext;
    LayoutInflater inflater;
    Typeface typeface;

    public SpinnerAdapter(Context context, String[] ar) {
        mContext = context;
        array = ar;
        inflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return array[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_spinner, null);
            holder.textFont = (TextView) convertView
                    .findViewById(R.id.textSpinnerRow);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            switch (position) {
                case 0:
                    holder.textFont.setTypeface(null);
                    break;
                case 1:
                    typeface = Typeface.createFromAsset(mContext.getAssets(),
                            "atmostsphere.ttf");
                    holder.textFont.setTypeface(typeface);
                    break;
                case 2:
                    typeface = Typeface.createFromAsset(mContext.getAssets(),
                            "bunga_melati_putih.ttf");
                    holder.textFont.setTypeface(typeface);

                    break;
                case 3:
                    typeface = Typeface.createFromAsset(mContext.getAssets(),
                            "earth_aircraft_universe.ttf");
                    holder.textFont.setTypeface(typeface);

                    break;
                case 4:
                    typeface = Typeface.createFromAsset(mContext.getAssets(),
                            "jumping_running.ttf");
                    holder.textFont.setTypeface(typeface);
                    break;

                default:
                    break;
            }
            holder.textFont.setText(array[position]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        TextView textFont;
    }

}
