package com.example.bnariv2;

import java.io.InputStream;

import android.widget.TimePicker.*;

import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * 날씨전달 어플 실습
 *
 */
public class pop extends Activity {
	
	Button b ;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);
		Intent it1 = getIntent();
		
		Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(2000);
		
	}
	
	public void finish(View v){
		int id = v.getId();
		finish();
	}
}