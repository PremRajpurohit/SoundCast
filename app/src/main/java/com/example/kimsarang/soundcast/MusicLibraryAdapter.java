package com.example.kimsarang.soundcast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicLibraryAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Music> musicList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public  MusicLibraryAdapter(Context context, ArrayList<Music> musicList){
        this.mContext = context;
        this.musicList = musicList;
        this.listener = (OnItemClickListener) mContext;
    }

    @Override
    public void onClick(View v) {
        listener.onItemClicked(v);
    }

    public interface OnItemClickListener
    {
        public void onItemClicked(View view);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.stack_view,viewGroup,false);
        return new MusicItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MusicItemHolder musicItemHolder = ((MusicItemHolder)viewHolder);
        Music music = musicList.get(i);
        Picasso.with(mContext).load(music.getImage()).into(musicItemHolder.trackImage);
        musicItemHolder.trackTitle.setText(music.getTitle());
        musicItemHolder.itemView.setOnClickListener(this);
        musicItemHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class MusicItemHolder extends RecyclerView.ViewHolder
    {
        TextView trackTitle;
        ImageView trackImage;

        public MusicItemHolder(@NonNull View itemView) {
            super(itemView);
            trackTitle = itemView.findViewById(R.id.trackTitle);
            trackImage = itemView.findViewById(R.id.trackImage);
        }
    }
}
