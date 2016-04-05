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
public class ActivityHistoryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;

    List<ActivityEntry> list = Collections.emptyList();
    DBOperations dbOperations;
    private enum displayViewType {
        DATEVIEW,
        INFOVIEW
    }
    displayViewType prevType;
    int dateViewCounter = 0;
    private int listSize = -1;
    Context context;


    public int getListSize()
    {
        if(listSize == -1)
        {
            listSize = findAdapterCount();
        }

        return listSize;

    }

    public ActivityHistoryViewAdapter(Context context, List<ActivityEntry> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        dbOperations = new DBOperations(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == displayViewType.INFOVIEW.ordinal()) {
            View view = inflater.inflate(R.layout.activity_goal_history_item, parent, false);
            ActivityEntryInfoHolder holder = new ActivityEntryInfoHolder(view);
            return holder;
        }
        else
        {
            View view = inflater.inflate(R.layout.activity_history_date_view, parent, false);
            ActivityEntryDateHolder holder = new ActivityEntryDateHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityEntry activityEntry = list.get(position==0?position:(prevType == displayViewType.DATEVIEW)?((position-dateViewCounter)+1):(position-dateViewCounter));
        Activity current = dbOperations.getActivityById(activityEntry.getActivity_id());
        String activityName = current.getName(),text = activityEntry.getNotes(),rpe = String.valueOf(activityEntry.getRpe()),time = String.valueOf(activityEntry.getActivity_length())+" mins";

        if(holder instanceof ActivityEntryInfoHolder) {
            ActivityEntryInfoHolder infoHolder = (ActivityEntryInfoHolder) holder;
            infoHolder.text.setText(text);
            infoHolder.activityName.setText(activityName);
            infoHolder.rpe.setText(rpe);
            infoHolder.time.setText(time);
            if (text.equals(""))
                infoHolder.text.setVisibility(View.GONE);

        }
        else if(holder instanceof ActivityEntryDateHolder)
        {
            ActivityEntryDateHolder dateHolder = (ActivityEntryDateHolder) holder;

            String date = activityEntry.getDate();
            dateHolder.date.setText(date);
        }



    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) //First record. Return date view
        {
            dateViewCounter++;
            prevType = displayViewType.DATEVIEW;
            return displayViewType.DATEVIEW.ordinal();
        }
        if(prevType == displayViewType.DATEVIEW)
        {
            prevType = displayViewType.INFOVIEW;
            return displayViewType.INFOVIEW.ordinal();
        }

        ActivityEntry activityEntry = list.get((position - dateViewCounter));
        String currentDate = activityEntry.getDate();
        activityEntry = list.get((position-dateViewCounter)-1);
        String prevDate = activityEntry.getDate();

        if(currentDate.equals(prevDate))
        {
            prevType = displayViewType.INFOVIEW;
            return displayViewType.INFOVIEW.ordinal();
        }
        else
        {
            prevType = displayViewType.DATEVIEW;
            dateViewCounter++;
            return displayViewType.DATEVIEW.ordinal();
        }
/*
        DateOperations dateOperations = new DateOperations(context);
        Date currentDateObj =  dateOperations.getMysqlDateFormat().parse(currentDate);
        Date prevDateObj = dateOperations.getMysqlDateFormat().parse(prevDate);*/
    }

    @Override
    public int getItemCount() {
        return getListSize();
    }

    private int findAdapterCount() {
        if(list.size() == 0)
            return 0;
        int count = 1;
        for(int i =1; i<list.size();i++)
        {
            String curDate = list.get(i).getDate();
            String prevDate = list.get(i-1).getDate();

            if(curDate.equals(prevDate))
                continue;
            count++;

        }
        return list.size()+count;
    }


    public class ActivityEntryDateHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.date_tv)
        TextView date;

        public ActivityEntryDateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ActivityEntryInfoHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.goal_activity_history_text)
        TextView text;
        @Bind(R.id.goal_activity_history_rpe)
        TextView rpe;
        @Bind(R.id.goal_activity_history_name)
        TextView activityName;
        @Bind(R.id.goal_activity_history_time)
        TextView time;



        public ActivityEntryInfoHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);


        }
    }


}
