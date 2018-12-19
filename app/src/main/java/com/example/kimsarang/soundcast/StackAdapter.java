package com.example.kimsarang.soundcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StackAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ViewHolder viewHolder;

    public StackAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.stack_view,parent,false);
        viewHolder = new ViewHolder(convertView);
        return null;
    }

    public class ViewHolder{
        ImageView trackImage;
        TextView trackTitle;
        public ViewHolder(View view){
            trackImage = view.findViewById(R.id.trackImage);
            trackTitle = view.findViewById(R.id.trackTitle);
        }
    }
}
