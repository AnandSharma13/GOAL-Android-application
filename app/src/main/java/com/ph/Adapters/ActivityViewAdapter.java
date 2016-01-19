package com.ph.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ph.R;
import com.ph.model.Activity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anup on 1/18/2016 .
 */
public class ActivityViewAdapter extends RecyclerView.Adapter<ActivityViewAdapter.MyViewHolder> {
    private final LayoutInflater inflater;

    List<Activity> list = Collections.emptyList();


    public ActivityViewAdapter(Context context, List<Activity> list)
    {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Activity current = list.get(position);
        holder.title.setText(current.getName());
        holder.id.setText(String.valueOf(current.getActivity_id()));
        //TODO: Handle images once they're available
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView id;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.activity_name);
            image = (ImageView) itemView.findViewById(R.id.activity_image);
            id = (TextView) itemView.findViewById(R.id.activity_id);
        }
    }
}
