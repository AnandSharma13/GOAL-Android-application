package com.ph.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ph.R;
import com.ph.model.Activity;

import java.util.Collections;
import java.util.List;


public class ActivityViewAdapter extends RecyclerView.Adapter<ActivityViewAdapter.MyViewHolder> {
    private final LayoutInflater inflater;

    List<Activity> list = Collections.emptyList();
    private int selectedPos = 0;
    Context context;


    public ActivityViewAdapter(Context context, List<Activity> list)
    {
        this.context = context;
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


        holder.itemView.setSelected(selectedPos == position);
        String imageName = current.getName().toLowerCase();
        imageName = imageName.replace(" ","_");
        //TODO: Handle images once they're available

        Drawable drawable = null;
        try {
             drawable = context.getDrawable(context.getResources().getIdentifier(imageName, "drawable", context.getPackageName()));
        }catch (Exception ex)
        {
            Log.w("ActivityViewAdapter", "Drawable element not found for " + imageName);
            drawable = null;
        }
        if(drawable!=null) {
            holder.image.setImageDrawable(drawable);
            holder.title.setVisibility(View.GONE);
        }
        else {
            holder.image.setImageResource(R.drawable.ic_launcher);
            holder.title.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView id;
        ImageView image;
        View parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            title = (TextView) itemView.findViewById(R.id.activity_name);
            image = (ImageView) itemView.findViewById(R.id.activity_image);
            id = (TextView) itemView.findViewById(R.id.activity_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedPos);
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);


                }
            });

        }

    }
}
