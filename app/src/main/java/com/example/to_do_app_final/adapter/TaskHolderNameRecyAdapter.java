package com.example.to_do_app_final.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do_app_final.Activity.AssignTask;
import com.example.to_do_app_final.R;
import com.example.to_do_app_final.model.TaskHolderNameRecy;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class TaskHolderNameRecyAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<TaskHolderNameRecy> arrayList;


    public TaskHolderNameRecyAdapter(Context context, ArrayList<TaskHolderNameRecy> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0 || position==0){
            return 0;
        }
        else return 1;

    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType==0){
            view = inflater.inflate(R.layout.recycler_design,parent,false);
            return new ViewHolderOne(view);
     }
       view = LayoutInflater.from(context).inflate(R.layout.recycler_design_diffrent,parent,false);
         return new ViewHolderTwo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position %2==0 || position==0){
            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            viewHolderOne.taskName.setText(arrayList.get(position).getTaskHolder());
            viewHolderOne.taskName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AssignTask.class);
                    intent.putExtra("docId",arrayList.get(position).getDocId());
                    Log.i("document ID",arrayList.get(position).getDocId());
                    context.startActivity(intent);
                    Toast.makeText(context, "This One"+position, Toast.LENGTH_SHORT).show();
                }
            });



        }
        else {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.taskDesignDiffrent.setText(arrayList.get(position).getTaskHolder());
            viewHolderTwo.taskDesignDiffrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AssignTask.class);
                    intent.putExtra("docId",arrayList.get(position).getDocId());
                    Log.i("document ID",arrayList.get(position).getDocId());
                    context.startActivity(intent);
                  //  Intent intent = new Intent(context, AssignTask.class);



                    Toast.makeText(context, "This is TEsting for testydjb"+position, Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView taskName;
        public ViewHolderOne(@NonNull View itemView) {

            super(itemView);

            taskName = itemView.findViewById(R.id.taskDesign);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        TextView taskDesignDiffrent;
        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            taskDesignDiffrent = itemView.findViewById(R.id.taskDesignDiffrent);


        }
    }



}
