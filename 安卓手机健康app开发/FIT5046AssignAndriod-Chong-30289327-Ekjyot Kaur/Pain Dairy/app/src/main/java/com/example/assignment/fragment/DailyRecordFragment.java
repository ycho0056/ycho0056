package com.example.assignment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.databinding.DailyRecordFragmentBinding;
import com.example.assignment.recyclerview.RecyclerViewAdapter;
import com.example.assignment.roomdatabase.entity.PainRecord;

import java.util.List;

public class DailyRecordFragment extends Fragment {
    private DailyRecordFragmentBinding addBinding;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public DailyRecordFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = DailyRecordFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
//        adapter initialisation
        adapter = new RecyclerViewAdapter();
        addBinding.data.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        addBinding.data.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this.getContext());
        addBinding.data.setLayoutManager(layoutManager);
        HomeFragment.painRecordViewModel.getAllPainRecord().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                adapter.setListData(painRecords);
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}