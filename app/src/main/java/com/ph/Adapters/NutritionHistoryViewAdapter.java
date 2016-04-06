package com.ph.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anup on 2/22/2016   .
 */
public class NutritionHistoryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;

    List<NutritionEntry> list = Collections.emptyList();
    DBOperations dbOperations;
    DateOperations dateOperations;

    private enum displayViewType {
        DATEVIEW,
        INFOVIEW
    }

    private class displayViewEntry{

        displayViewType type;
        int listPosition;
        private displayViewEntry(displayViewType type,int listPosition)
        {
            this.type = type;
            this.listPosition = listPosition;
        }

    }
    List<displayViewEntry> displayViewList = new ArrayList<>();
    displayViewType prevType;
    int dateViewCounter = 0;
    private int listSize = -1;
    Context context;


    public int getListSize() {
        if (listSize == -1) {
            listSize = populateList();
        }

        return listSize;

    }


    private int populateList() {
        if (list.size() == 0)
            return 0;


        for (int i = 0; i < list.size(); i++) {
            if(i == 0)
            {
                displayViewList.add(new displayViewEntry(displayViewType.DATEVIEW,i));
                displayViewList.add(new displayViewEntry(displayViewType.INFOVIEW,i));
                continue;
            }
            String curDate = list.get(i).getDate();
            String prevDate = list.get(i - 1).getDate();

            if (curDate.equals(prevDate))
            {
                displayViewList.add(new displayViewEntry(displayViewType.INFOVIEW,i));
            }
            else
            {
                displayViewList.add(new displayViewEntry(displayViewType.DATEVIEW,i));
                displayViewList.add(new displayViewEntry(displayViewType.INFOVIEW,i));
            }


        }
        return displayViewList.size();
    }


    public NutritionHistoryViewAdapter(Context context, List<NutritionEntry> list) {
        inflater = LayoutInflater.from(context);
        dbOperations = new DBOperations(context);
        dateOperations = new DateOperations(context);
        this.context = context;
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == displayViewType.INFOVIEW.ordinal()) {
            View view = inflater.inflate(R.layout.nutrition_goal_history_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        else
        {
            View view = inflater.inflate(R.layout.activity_history_date_view, parent, false);
            NutritionEntryDateHolder holder = new NutritionEntryDateHolder(view);
            return holder;
        }
    }

        @Override
        public void onBindViewHolder (RecyclerView.ViewHolder holder,int position){
           // NutritionEntry nutritionEntry = list.get(position == 0 ? position : (prevType == displayViewType.DATEVIEW) ? ((position - dateViewCounter) + 1) : (position - dateViewCounter));
            NutritionEntry nutritionEntry = list.get(displayViewList.get(position).listPosition);

            if(holder instanceof MyViewHolder) {

                MyViewHolder infoHolder = (MyViewHolder) holder;

                String text = nutritionEntry.getNotes();
                infoHolder.text.setText(text);

                String type = nutritionEntry.getNutrition_type();
                infoHolder.type.setText(type);

                int attic = nutritionEntry.getAttic_food(), diary = nutritionEntry.getDairy(), fruit = nutritionEntry.getFruit(), protein = nutritionEntry.getProtein(), grain = nutritionEntry.getGrain(), vegetable = nutritionEntry.getVegetable();

                String details = "";

                details += (attic == 0 ? "" : " Attic " + attic) + ((diary == 0) ? "" : " Diary " + diary) + ((fruit == 0) ? "" : " Fruit " + fruit) + ((protein == 0) ? "" : " Protein " + protein) + ((grain == 0) ? "" : " Grain " + grain) + ((vegetable == 0) ? "" : " Vegetable " + vegetable);

                infoHolder.details.setText(details);
            }
            else if(holder instanceof NutritionEntryDateHolder)
            {
                NutritionEntryDateHolder dateHolder = (NutritionEntryDateHolder) holder;
                Date dateObj;
                try {
                    dateObj = dateOperations.getMysqlDateFormat().parse(nutritionEntry.getDate());
                    String date = dateOperations.getUniformDateFormat().format(dateObj);
                    dateHolder.date.setText(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }


    @Override
    public int getItemViewType(int position) {

        return displayViewList.get(position).type.ordinal();
        /*if(position == 0) //First record. Return date view
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


        if((position-dateViewCounter)>=list.size())
        {
            Log.i("Huh?","position="+position+" "+"date counter = "+dateViewCounter+" "+"list size= "+list.size()+" "+"View list size = "+getListSize());
            prevType = displayViewType.INFOVIEW;
            return displayViewType.INFOVIEW.ordinal();
        }

        NutritionEntry nutritionEntry = list.get((position - dateViewCounter));
        String currentDate = nutritionEntry.getDate();
        nutritionEntry = list.get((position-dateViewCounter)-1);
        String prevDate = nutritionEntry.getDate();

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
        }*/
/*
        DateOperations dateOperations = new DateOperations(context);
        Date currentDateObj =  dateOperations.getMysqlDateFormat().parse(currentDate);
        Date prevDateObj = dateOperations.getMysqlDateFormat().parse(prevDate);*/
    }

        @Override
        public int getItemCount() {
            return getListSize();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.nutrition_history_text)
            TextView text;
            @Bind(R.id.nutrition_history_type)
            TextView type;
            @Bind(R.id.nutrition_history_details)
            TextView details;

            public MyViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);


            }

        }

        public class NutritionEntryDateHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.date_tv)
            TextView date;

            public NutritionEntryDateHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
