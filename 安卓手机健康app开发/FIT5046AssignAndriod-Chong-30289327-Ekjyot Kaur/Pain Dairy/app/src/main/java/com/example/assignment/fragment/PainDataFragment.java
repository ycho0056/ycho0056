package com.example.assignment.fragment;

import android.app.Activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.assignment.Alarm.MyReceiver;
import com.example.assignment.R;
import com.example.assignment.Signin;
import com.example.assignment.databinding.PainDataFragmentBinding;
import com.example.assignment.roomdatabase.entity.PainRecord;
import com.example.assignment.viewmodel.SharedViewModel;
import com.example.assignment.weather.Main;
import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class PainDataFragment extends Fragment {
    private SharedViewModel model;
    private PainDataFragmentBinding addBinding;
    public PainDataFragment(){}

    int mHour,mMin;
    MyReceiver myReceiver=new MyReceiver();


    private int painLevel;
    private String painLocation;
    private String mood;
    private int stepGoal;
    private int stepTaken;

    private int returnId;
    private String editTemp;
    private String editHumity;
    private String editPressure;
    private String editEmail;
    private String editDate;
    private PainRecord editPainRecord;
    private String todayTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = PainDataFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        // get today record
        HomeFragment.painRecordViewModel.getAllPainRecord().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                ArrayList<PainRecord> currentDayRecord = new ArrayList<>();
                //get current day
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
                String todayTime = df.format(new Date());

                painRecords.forEach( painRecord ->{
                    //get current day data
                    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
                    String recordTime = date.format(new Date(painRecord.date));
                    if(todayTime.equals(recordTime)){
                        currentDayRecord.add(painRecord);
                    }
                });
                if(!currentDayRecord.isEmpty()){
                    addBinding.painLevelSilder.setValue(currentDayRecord.get(0).painLevel);
                    List<String> list = new ArrayList<String>();
                    list.addAll(Arrays.asList("Back","Neck","Head","Knees","Hips","abdomen","Elbows","Shoulders","Shins","Jaw","Facial"));
                    int index = 0;
                    for (int i =0;i<list.size();i++){
                        if (currentDayRecord.get(0).painLocation.equals(list.get(i))){
                            index = 0;
                        }
                    }
                    addBinding.painLocationSpinner.setSelection(index);
                    int position = 0;
                    switch (currentDayRecord.get(0).moodLevel){
                        case "very good":
                            position=R.id.very_good;break;
                        case "good":
                            position=R.id.good;break;
                        case "average":
                            position=R.id.average;break;
                        case "low":
                            position=R.id.low;break;
                        case "very_low":
                            position=R.id.very_low;break;
                    }
                    addBinding.radioGroup.check(position);
                    addBinding.stepGoalEd.setText(String.valueOf(currentDayRecord.get(0).stepGoal));
                    addBinding.stepTakenEd.setText(String.valueOf(currentDayRecord.get(0).stepTaken));
                    returnId=currentDayRecord.get(0).id;
                    editDate=currentDayRecord.get(0).date;
                    editEmail=currentDayRecord.get(0).email;
                    editTemp=currentDayRecord.get(0).temperature;
                    editHumity=currentDayRecord.get(0).humidity;
                    editPressure=currentDayRecord.get(0).pressure;
                    addBinding.save.setEnabled(false);
                    addBinding.stepGoalEd.setEnabled(false);
                    addBinding.stepTakenEd.setEnabled(false);
                    addBinding.painLevelSilder.setEnabled(false);
                    addBinding.painLocationSpinner.setEnabled(false);
                    addBinding.veryGood.setEnabled(false);
                    addBinding.good.setEnabled(false);
                    addBinding.average.setEnabled(false);
                    addBinding.low.setEnabled(false);
                    addBinding.veryLow.setEnabled(false);
                }
            }
        });
        // pain  level silder
        addBinding.painLevelSilder.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                painLevel = (int) value;
            }
        });
        // pain location
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList("Back","Neck","Head","Knees","Hips","abdomen","Elbows","Shoulders","Shins","Jaw","Facial"));
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBinding.painLocationSpinner.setAdapter(spinnerAdapter);
        addBinding.painLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectLocation = parent.getItemAtPosition(position).toString();
                if(selectLocation!=null)
                {
                    painLocation = selectLocation;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // mood
        addBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.very_good:
                        mood = "very good";break;
                    case R.id.good:
                        mood = "good";break;
                    case R.id.average:
                        mood = "average";break;
                    case R.id.low:
                        mood = "low";break;
                    case R.id.very_low:
                        mood = "very_low";break;
                }
            }
        });

        addBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(TextUtils.isEmpty(addBinding.stepTakenEd.getText().toString())) && mood!=null && painLocation!=null){
                    stepGoal=Integer.parseInt(addBinding.stepGoalEd.getText().toString());
                    stepTaken=Integer.parseInt(addBinding.stepTakenEd.getText().toString());
                    String date = new Date().toString();
                    Main main = HomeFragment.mainWeather;
                    PainRecord painRecord = new PainRecord(Signin.Email,painLevel,painLocation,mood,stepTaken,stepGoal,date,main.getTemp(),main.getHumidity(),main.getPressure());
                    HomeFragment.painRecordViewModel.insert(painRecord);
                    Toast.makeText(view.getContext(),"Successfully save!!!", Toast.LENGTH_SHORT).show();
                    addBinding.stepGoalEd.setEnabled(false);
                    addBinding.stepTakenEd.setEnabled(false);
                    addBinding.painLevelSilder.setEnabled(false);
                    addBinding.painLocationSpinner.setEnabled(false);
                    addBinding.veryGood.setEnabled(false);
                    addBinding.good.setEnabled(false);
                    addBinding.average.setEnabled(false);
                    addBinding.low.setEnabled(false);
                    addBinding.veryLow.setEnabled(false);
                }
                else{
                    Toast.makeText(view.getContext(),"The value not be empty!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        addBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBinding.stepGoalEd.setEnabled(true);
                addBinding.stepTakenEd.setEnabled(true);
                addBinding.painLevelSilder.setEnabled(true);
                addBinding.painLocationSpinner.setEnabled(true);
                addBinding.veryGood.setEnabled(true);
                addBinding.good.setEnabled(true);
                addBinding.average.setEnabled(true);
                addBinding.low.setEnabled(true);
                addBinding.veryLow.setEnabled(true);
                addBinding.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stepGoal=Integer.parseInt(addBinding.stepGoalEd.getText().toString());
                        stepTaken=Integer.parseInt(addBinding.stepTakenEd.getText().toString());
                        editPainRecord =new PainRecord(editEmail,painLevel,painLocation,mood,stepTaken,stepGoal,editDate,editTemp,editHumity,editPressure);
                        editPainRecord.setId(returnId);
                        HomeFragment.painRecordViewModel.update(editPainRecord);
                        Toast.makeText(view.getContext(),"Successfully update!!!", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        IntentFilter filter=new IntentFilter();
        filter.addAction("com.test.alarmData");
        getActivity().registerReceiver(myReceiver,filter);
        Intent intent = new Intent("com.test.alarmData");
        intent.putExtra("data","Time up! After 2 minutes you need to enter today pain data!");
//
        addBinding.timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMin = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute-2);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        calendar.getTimeInMillis();
                        Toast.makeText(view.getContext(), "Time set up to:" + ""+hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();

                        PendingIntent sender = PendingIntent.getBroadcast(view.getContext(), 0, intent, 0);
                        AlarmManager am;
                        am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),sender);
                    }
                },mHour,mMin, false);
                timePickerDialog.show();
            }
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
        getActivity().unregisterReceiver(myReceiver);
    }
}