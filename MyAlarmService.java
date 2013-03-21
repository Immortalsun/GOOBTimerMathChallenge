package com.maashes.timerproject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class MyAlarmService extends Service {
	
	public void onCreate()
	{
		Toast.makeText(this,"MyAlarmService.onCreate()"
				, Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()",
				Toast.LENGTH_LONG).show();
	}
	
	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId)
	{
		//on service start, get data from intent
		//display data with a toast
		super.onStart(intent, startId);
		Bundle extras = intent.getExtras();
		String data = extras.getString("com.maashes.timerproject.MsgKey");
		Toast.makeText(this, "MyAlarmService.onStart() data = "+data
				,Toast.LENGTH_LONG).show();
		
		//create intent to send message since Services cannot have a UI
		Intent alertIntent = new Intent();
		alertIntent.setClass(this, MathActivity.class);
		alertIntent.putExtra("com.maashes.timerproject.MsgKey", data);
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(alertIntent);
	}

}
