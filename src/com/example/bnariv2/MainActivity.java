package com.example.bnariv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView ondoM;
	ImageView alarmM;
	TextView regionM;
	ImageView ottM;
	Drawable nowImage;
	int ottflag = 1;
	int wflag = 1; // 현재온도만 받아오는 플래그

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ondoM = (TextView) findViewById(R.id.ondo);
		alarmM = (ImageView) findViewById(R.id.alarm);
		regionM = (TextView) findViewById(R.id.region);
		ottM = (ImageView) findViewById(R.id.otimg);

		WeatherParsing wp = new WeatherParsing(wflag, ondoM);

		ondoM.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int i = v.getId();
				Intent it = new Intent(getApplicationContext(),
						OndoActivity.class);
				startActivity(it);
			}
		});

		alarmM.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int i = v.getId();
				Intent it = new Intent(getApplicationContext(),
						AlarmActivity.class);
				startActivity(it);
				// finish();
			}
		});

		regionM.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int i = v.getId();
				Intent it = new Intent(getApplicationContext(),
						LocalActivity.class);
				startActivity(it);
			}
		});

		ottM.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int i = v.getId();
				if (ottflag == 1) {
					nowImage = getResources().getDrawable(R.drawable.shirt1);
					ottflag++;
				} else if (ottflag == 2) {
					nowImage = getResources().getDrawable(R.drawable.shirt2);
					ottflag++;
				} else {
					nowImage = getResources().getDrawable(R.drawable.shirt);
					ottflag = 1;
				}

				ottM.setImageDrawable(nowImage);
				ottM.postInvalidate();

			}
		});

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

	// --------------------------------------------------------------------------
	// 온도 체크 함수
	public void checkOndo() {
	}

}
