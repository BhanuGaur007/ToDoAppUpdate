package com.example.to_do_app_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do_app_final.R;
import com.example.to_do_app_final.model.TaskAddRecyclerModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class TaskAddRecyclerAdapter extends RecyclerView.Adapter <TaskAddRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<TaskAddRecyclerModel> arrayList;

    public TaskAddRecyclerAdapter(Context context, ArrayList<TaskAddRecyclerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TaskAddRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAddRecyclerAdapter.ViewHolder holder, int position) {
        holder.taskName.setText(arrayList.get(position).getTask());
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            taskName = itemView.findViewById(R.id.taskDesign);
        }
    }
}
