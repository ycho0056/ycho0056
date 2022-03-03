package com.example.assignment.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.assignment.databinding.ReportsFragmentBinding;
import com.example.assignment.roomdatabase.entity.PainRecord;
import com.example.assignment.viewmodel.SharedViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReportsFragment extends Fragment {
    private SharedViewModel model;
    private ReportsFragmentBinding addBinding;
    private List<PainRecord> painRecordList;
    private String weatherType;
    ArrayList<Entry> painLevel = new ArrayList<>();
    ArrayList<Entry> weather = new ArrayList<>();
    ArrayList<Integer> color = new ArrayList<>();
    public ReportsFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = ReportsFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        painLocationChart();

//        lineChart();

        addBinding.startDateSelect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectDate(addBinding.startDateSelect,"start");
            }
        });

        addBinding.endDateSelect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectDate(addBinding.endDateSelect,"end");
            }
        });
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList("temperature","humidity","pressure"));
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBinding.weatherTypeSpinner.setAdapter(spinnerAdapter);
        addBinding.weatherTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectLocation = parent.getItemAtPosition(position).toString();
                if(selectLocation!=null)
                {
                    weatherType = selectLocation;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addBinding.showChart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(addBinding.startDateSelect.getText().toString().isEmpty()||addBinding.endDateSelect.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please complete the select",Toast.LENGTH_SHORT).show();
                }
                else{
                    painRecordList.forEach( painRecord -> {
                        Date selectDate = new Date(painRecord.date);
                        if(selectDate.after(startDate) && selectDate.before(endDate)){
                            // line chart x,y float
                            float pain = (float)painRecord.painLevel;
                            float temp = Float.parseFloat(painRecord.temperature);
                            float humidity = Float.parseFloat(painRecord.humidity);
                            float pressure = Float.parseFloat(painRecord.pressure);
                            float date = (float) new Date(painRecord.date).getTime();
                            painLevel.add(new Entry(date,pain));
                            if(weatherType.equals("temperature")){
                                weather.add(new Entry(date,temp));
                            }
                            if(weatherType.equals("humidity")){
                                weather.add(new Entry(date,humidity));
                            }
                            if(weatherType.equals("pressure")){
                                weather.add(new Entry(date,pressure));
                            }


                        }
                    });
                    displayLineChart(painLevel,weather);
                }

            }
        });
        addBinding.cleanSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBinding.startDateSelect.setText("");
                addBinding.endDateSelect.setText("");
                addBinding.weatherType.setText("");
            }
        });
        addBinding.correlationTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (painLevel.isEmpty() || weather.isEmpty()) {
                    Toast.makeText(view.getContext(), "Intensity level and weather cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    // collect data of pain and weather
                    double data[][] = new double[painLevel.size()][2];
                    for (int i = 0; i < painLevel.size(); i++) {
                        data[i][0] = painLevel.get(i).getY();
                        data[i][1] = weather.get(i).getY();
                    }
                    RealMatrix realMatrix = MatrixUtils.createRealMatrix(data);
                    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(realMatrix);
                    // correlation test (another method): x-y
                    RealMatrix corM = pearsonsCorrelation.getCorrelationMatrix();
                    // significant test of the correlation coefficient (p-value)
                    RealMatrix pM = pearsonsCorrelation.getCorrelationPValues();
                    addBinding.value.setText("Significance is: " + pM.getEntry(0, 1) +
                            "\n" + "R value is:: " + corM.getEntry(0, 1));
                }

            }
        });

        return view;
    }

    public void painLocationChart(){
        HashMap<String,Integer> painLocationValue = new HashMap<>();
        //initialization hashmap
        painLocationValue.put("Back",0);
        painLocationValue.put("Neck",0);
        painLocationValue.put("Head",0);
        painLocationValue.put("Knees",0);
        painLocationValue.put("Hips",0);
        painLocationValue.put("abdomen",0);
        painLocationValue.put("Elbows",0);
        painLocationValue.put("Shoulders",0);
        painLocationValue.put("Shins",0);
        painLocationValue.put("Jaw",0);
        painLocationValue.put("Facial",0);
        List<PieEntry> painLocationEntries = new ArrayList<>();
        List<PieEntry> stepTakenEntries = new ArrayList<>();


        //get all data
        HomeFragment.painRecordViewModel.getAllPainRecord().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                painRecordList = painRecords;
                ArrayList<PainRecord> currentDayRecord = new ArrayList<>();
                //get current day
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
                String todayTime = df.format(new Date());

                painRecords.forEach( painRecord ->{
                    //caculate key
                    if(painLocationValue.containsKey(painRecord.painLocation)){
                        painLocationValue.put(painRecord.painLocation,painLocationValue.get(painRecord.painLocation)+1);
                    }
                    //get current day data
                    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
                    String recordTime = date.format(new Date(painRecord.date));
                    if(todayTime.equals(recordTime)){
                        currentDayRecord.add(painRecord);

                    }
                });
                //set color
                color.add(Color.rgb(252, 186, 3));
                color.add(Color.rgb(119, 3, 252));
                color.add(Color.rgb(3, 194, 252));
                color.add(Color.rgb(252, 3, 219));
                color.add(Color.rgb(252, 11, 3));
                color.add(Color.rgb(252, 157, 3));
                color.add(Color.rgb(57, 252, 3));
                // pie chart 1
                painLocationValue.forEach((key,value) ->{
                    if(value > 0){
                        painLocationEntries.add(new PieEntry(value,key));
                    }
                });
                PieDataSet painLocationDataSet = new PieDataSet(painLocationEntries,"");
                painLocationDataSet.setColors(color);
                painLocationDataSet.setValueTextSize(20);


                PieData painLocationData = new PieData(painLocationDataSet);
                PieChart painLocationPieChart = addBinding.barChart;
                painLocationPieChart.setUsePercentValues(true);
                painLocationData.setValueFormatter(new PercentFormatter(painLocationPieChart));
                painLocationPieChart.setData(painLocationData);
                painLocationPieChart.setEntryLabelTextSize(20);
                painLocationPieChart.invalidate();
                // pie chart 2
                if(!currentDayRecord.isEmpty()){
                    stepTakenEntries.add(new PieEntry(currentDayRecord.get(0).stepTaken,"Step Taken"));
                    int remainStep = 0;
                    if(currentDayRecord.get(0).stepGoal - currentDayRecord.get(0).stepTaken >= 0){
                        remainStep = currentDayRecord.get(0).stepGoal - currentDayRecord.get(0).stepTaken;
                    }
                    stepTakenEntries.add(new PieEntry(remainStep, "Step remain"));
                    PieDataSet stepTakenDataSet = new PieDataSet(stepTakenEntries,"");
                    stepTakenDataSet.setColors(color);
                    stepTakenDataSet.setValueTextSize(20);

                    PieData stepTakenData = new PieData(stepTakenDataSet);
                    PieChart stepTakenPieChart = addBinding.barChart2;
                    stepTakenPieChart.setData(stepTakenData);
                    stepTakenPieChart.invalidate();
                }



            }
        });
    }

    private Date startDate;
    private Date endDate;

    public void selectDate(TextView textView, String point){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //calendar.set(year, month, dayOfMonth);
                Date tempDate = new Date(year-1900, month, dayOfMonth);
                textView.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                if (point.equals("start")){
                    startDate = tempDate;
                }
                else {
                    endDate = tempDate;
                }
            }
        }, year, month, day);

        pickerDialog.show();
    }

    public void displayLineChart(ArrayList<Entry> valuesPain, ArrayList<Entry> valuesWeather){
        LineChart lineChart = addBinding.LineChart1;
        // there is no description
        lineChart.getDescription().setEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scale and drag
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setLabelCount(valuesPain.size(), true);
        xAxis.setLabelRotationAngle(-40f);
        xAxis.setTextColor(Color.YELLOW);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                return dateFormat.format(new Date((long) value));
            }
        });

        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setEnabled(true);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setDrawGridLines(true);
        axisLeft.setGranularityEnabled(true);
        axisLeft.setTextColor(ColorTemplate.getHoloBlue());
        axisLeft.setTextColor(Color.BLUE);
        axisLeft.setYOffset(-8f);
        axisLeft.setAxisMinimum(0f);
        axisLeft.setAxisMaximum(10f);


        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setEnabled(true);
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRight.setDrawGridLines(true);
        axisRight.setGranularityEnabled(true);
        axisRight.setTextColor(ColorTemplate.getHoloBlue());
        axisRight.setTextColor(Color.BLUE);
        axisRight.setYOffset(-8f);


        LineDataSet lineDataSetPain = new LineDataSet(valuesPain, "values of pain");
        lineDataSetPain.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetPain.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        lineDataSetPain.setDrawCircles(false);
        lineDataSetPain.setDrawValues(false);
        lineDataSetPain.setLineWidth(2.2f);
        lineDataSetPain.setFillAlpha(70);
        lineDataSetPain.setDrawCircleHole(false);
        // color difference
        lineDataSetPain.setHighLightColor(Color.BLUE);

        LineDataSet lineDataSetWeather = new LineDataSet(valuesWeather, "values of weather");
        lineDataSetWeather.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineDataSetWeather.setColor(ColorTemplate.JOYFUL_COLORS[2]);
        lineDataSetWeather.setDrawCircles(false);
        lineDataSetWeather.setDrawValues(false);
        lineDataSetWeather.setLineWidth(2.2f);
        lineDataSetWeather.setFillAlpha(70);
        lineDataSetWeather.setDrawCircleHole(false);


        // display
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSetPain);
        iLineDataSets.add(lineDataSetWeather);
        LineData linedata = new LineData(iLineDataSets);
        linedata.setValueTextSize(10f);
        linedata.setValueTextColor(Color.BLACK);

        lineChart.setData(linedata);
        lineChart.invalidate();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}