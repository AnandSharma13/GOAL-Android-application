package com.ph.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ph.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anand on 1/12/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private final LayoutInflater inflater;

    private List<DrawerListData> list = Collections.emptyList();

    public DrawerAdapter(Context context, List<DrawerListData> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DrawerListData current = list.get(position);
        holder.title.setText(current.getTitle());
        holder.image.setImageResource(current.getListId());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<DrawerListData> getList() {
        return list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            image = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
