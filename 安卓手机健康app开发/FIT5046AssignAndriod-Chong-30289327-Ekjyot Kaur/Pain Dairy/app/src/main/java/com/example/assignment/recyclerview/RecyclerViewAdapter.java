package com.example.assignment.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.databinding.RecyclerviewLayoutBinding;
import com.example.assignment.roomdatabase.entity.PainRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<PainRecord> painRecords =new ArrayList<>();
//    private OnItemClickListener mOnItemClickListener;
    public void setListData(List<PainRecord> painRecords){

        this.painRecords=painRecords;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
//    view binding
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewLayoutBinding addBinding = RecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(addBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        PainRecord painRecord = painRecords.get(position);//position =index
        Date date =new Date(painRecord.date);
        String shortDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
//        viewHolder.addBinding.date.setText(Integer.toString(painRecord.id));
        viewHolder.addBinding.date.setText(shortDate);
        viewHolder.addBinding.painlocation.setText(painRecord.painLocation);
        viewHolder.addBinding.painLevel.setText(Integer.toString(painRecord.painLevel));
        viewHolder.addBinding.mood.setText(painRecord.moodLevel);
        viewHolder.addBinding.temp.setText(painRecord.temperature);//id


    }

    @Override
    public int getItemCount() {
        return painRecords.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewLayoutBinding addBinding;
        public ViewHolder(RecyclerviewLayoutBinding addBinding){
            super(addBinding.getRoot());
            this.addBinding=addBinding;
        }
    }

}
