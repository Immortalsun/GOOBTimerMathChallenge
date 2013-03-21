package com.maashes.timerproject;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimerActivity extends Activity {
	Button pickTime;
	public static final String EXTRA_MESSAGE =  "com.maashes.timerproject.MsgKey";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_layout);
		
		pickTime = (Button)findViewById(R.id.buttonPickTime);
	}
	
	@SuppressWarnings("deprecation")
	public void pickTimeHandler(View target){
		Toast.makeText(TimerActivity.this, "in PickTimehandler", Toast.LENGTH_LONG).show();
		showDialog(0);
	}
	
	protected Dialog onCreateDialog(int id){
		Toast.makeText(TimerActivity.this, "- onCreateDialog(ID_TIMEPICKER) -", Toast.LENGTH_LONG).show();
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		return new TimePickerDialog(this, theTimeSetListener, hour, minute, false);
	}
	//define the listener for when the user clicks the time picker's set button
	private TimePickerDialog.OnTimeSetListener theTimeSetListener = 
			new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hour, int minute) {
					// Store user's time entry in a string
					String time = "User picked Hour: "
					+String.valueOf(hour)+"\n"
							+ "Minute: "+String.valueOf(minute);
					//show toast with string time
					Toast.makeText(TimerActivity.this, time, Toast.LENGTH_LONG).show();
					int theHour = hour;
					int theMinute = minute;
					
					//create alarm intent 
					Intent alarmIntent = new Intent(TimerActivity.this, MyAlarmService.class);
					alarmIntent.putExtra("com.maashes.timerproject.MsgKey", "My message");
					//create pending alarm intent to link to service (what does the waiting)
					PendingIntent pendingAlarmIntent = PendingIntent.getService(TimerActivity.this, 
							0, alarmIntent, 0);
					
					//create Alarm manager to let android system know about the alarm
					AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
					
					//create calendar using android system to set user's alarm
					Calendar AlarmCal = Calendar.getInstance();
					//set milliseconds to current milliseconds
					AlarmCal.setTimeInMillis(System.currentTimeMillis());
					//set alarmCal's hour and minute to user selections
					AlarmCal.set(Calendar.HOUR_OF_DAY, theHour);
					AlarmCal.set(Calendar.MINUTE, theMinute);
					AlarmCal.set(Calendar.SECOND, 0);
					
					//Tell the alarm manager when to run the service
					//RTC_WAKEUP wakes up device if it is asleep when alarm is triggered
					//INTERVAL_FIFTEEN_MINUTES is how long the alarm will wait until it is repeated
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							AlarmCal.getTimeInMillis(), 
							pendingAlarmIntent); 
				}
			};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer_layout, menu);
		return true;
	}

}
