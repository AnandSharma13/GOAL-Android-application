package com.ph.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ph.R;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anup on 2/22/2016   .
 */
public class NutritionHistoryViewAdapter extends RecyclerView.Adapter<NutritionHistoryViewAdapter.MyViewHolder> {

    private final LayoutInflater inflater;

    List<NutritionEntry> list = Collections.emptyList();
    DBOperations dbOperations;

    public NutritionHistoryViewAdapter(Context context, List<NutritionEntry> list) {
        inflater = LayoutInflater.from(context);
        dbOperations = new DBOperations(context);
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nutrition_goal_history_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NutritionEntry nutritionEntry = list.get(position);

        String text = nutritionEntry.getNotes();
        holder.text.setText(text);

        String type = nutritionEntry.getNutrition_type();
        holder.type.setText(type);

        int attic = nutritionEntry.getAttic_food(),diary = nutritionEntry.getDairy(),fruit = nutritionEntry.getFruit(), protein = nutritionEntry.getProtein(), grain = nutritionEntry.getGrain(),vegetable = nutritionEntry.getVegetable();

        String details = "";

        details+= (attic == 0?"":" Attic "+attic) + ((diary == 0)?"":" Diary "+diary) + ((fruit == 0)?"":" Fruit "+fruit) + ((protein==0)?"":" Protein "+protein) + ((grain == 0)?"":" Grain "+grain) + ((vegetable==0)?"":" Vegetable "+vegetable);

        holder.details.setText(details);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
}
