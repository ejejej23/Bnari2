package com.example.bnari;

import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 날씨전달 어플 실습
 *
 */
public class Setting extends Activity  {

	Button b;
	Button b2;
	Button b3;
	
    
    String dongname;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		Intent it1 = getIntent();
		
		b = (Button)findViewById(R.id.back);
		b2 = (Button)findViewById(R.id.region);
		b3 = (Button)findViewById(R.id.alarm);
		
		dongname = getIntent().getStringExtra("dong");
		
	
		
	}
	
	public void back(View v){
		int i = v.getId();
	
		finish();
	}
	
	public void region(View v){
		int i = v.getId();
		Intent it = new Intent(this,Region.class);
		startActivity(it);
		finish();
	}
	
	public void alarm(View v){
		int i = v.getId();
		Intent it2 = new Intent(this,AlarmSet.class);
		startActivity(it2);
		finish();
		
	}
	
}
