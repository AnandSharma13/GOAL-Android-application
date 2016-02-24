package com.ph.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ph.R;
import com.ph.model.Activity;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anup on 2/22/2016   .
 */
public class ActivityHistoryViewAdapter extends RecyclerView.Adapter<ActivityHistoryViewAdapter.MyViewHolder> {
    private final LayoutInflater inflater;

    List<ActivityEntry> list = Collections.emptyList();
    DBOperations dbOperations;

    public ActivityHistoryViewAdapter(Context context, List<ActivityEntry> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        dbOperations = new DBOperations(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_goal_history_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ActivityEntry activityEntry = list.get(position);
        Activity current = dbOperations.getActivityById(activityEntry.getActivity_id());
        String activityName = current.getName(),text = activityEntry.getNotes(),rpe = String.valueOf(activityEntry.getRpe()),time = String.valueOf(activityEntry.getActivity_length())+" mins";

        if(text.equals(""))
            holder.text.setVisibility(View.GONE);
        else
        {
            holder.text.setText(text);
            holder.activityName.setText(activityName);
            holder.rpe.setText(rpe);
            holder.time.setText(time);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.goal_activity_history_text)
        TextView text;
        @Bind(R.id.goal_activity_history_rpe)
        TextView rpe;
        @Bind(R.id.goal_activity_history_name)
        TextView activityName;
        @Bind(R.id.goal_activity_history_time)
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);


        }
    }


}
