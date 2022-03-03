package com.example.assignment.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: "+intent.getAction());
        if(intent.getAction().equals("com.test.alarmData")){
            Toast.makeText(context,intent.getStringExtra("data"),Toast.LENGTH_SHORT).show();

        }
    }
}