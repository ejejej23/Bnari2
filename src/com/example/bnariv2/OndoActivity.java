package com.example.bnariv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OndoActivity extends Activity {

	int wflag = 2;
	int dflag = 3;
	TextView week0;
	TextView week1;
	TextView week2;
	TextView day0;
	TextView day1;
	TextView day2;
	TextView day3;
	TextView day4;
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ondo);
		
			week0 = (TextView)findViewById(R.id.week0);
			week1 = (TextView)findViewById(R.id.week1);
			week2 = (TextView)findViewById(R.id.week2);
			day0 = (TextView)findViewById(R.id.dayTime0);
			day1 = (TextView)findViewById(R.id.dayTime1);
			day2 = (TextView)findViewById(R.id.dayTime2);
			day3 = (TextView)findViewById(R.id.dayTime3);
			day4 = (TextView)findViewById(R.id.dayTime4);
		
		WeatherParsing wpWeek = new WeatherParsing(wflag, week0, week1, week2);
		WeatherParsing wpDay = new WeatherParsing(dflag, day0, day1, day2, day3, day4);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
		
}
