package com.example.bnari;

import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.*;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
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
public class MainActivity extends Activity {

	SQLiteDatabase sqlitedb;
	DBManager dbmanager;

	Button b;
	TextView tx;
	String dongname;
	String ondo;
	String rain;

	TextView tx2;
	TextView clo1;
	TextView clo2;
	TextView clo3;
	TextView clo4;
	TextView clo5;	
	
	int t;
	Calendar calendar;

	String dong = "";
	String dong2 = "";

	double pondo=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent it = getIntent();

		dongname = getIntent().getStringExtra("dong");

		tx = (TextView) findViewById(R.id.dong);

		tx2 = (TextView) findViewById(R.id.ondo);
		clo1 = (TextView) findViewById(R.id.clo1);
		clo2 = (TextView) findViewById(R.id.clo2);
		clo3 = (TextView) findViewById(R.id.clo3);
		clo4 = (TextView) findViewById(R.id.clo4);
		clo5 = (TextView) findViewById(R.id.clo5);

		//ondo = getIntent().getStringExtra("ondo");
		rain = getIntent().getStringExtra("rain");

		//tx2.setText(ondo);


		calendar = Calendar.getInstance();
		int d = calendar.get(Calendar.DAY_OF_MONTH);
		t =  calendar.get(Calendar.HOUR_OF_DAY);
		
		if(t>=3 && t<6){
			t = 6;
		}else if(t>=6 && t<9){
			t = 9;
		}else if(t>=9 && t<12){
			t = 12;
		}else if(t>=12 && t<15){
			t = 15;
		}else if(t>=15 && t<18){
			t = 18;
		}else if(t>=18 && t<21){
			t = 21;
		}else if(t>=21 && t<24){
			t = 24;
		}else{
			t = 3;
		}
		
		try {
			dbmanager = new DBManager(this);
			sqlitedb = dbmanager.getReadableDatabase();

			Cursor c1 = sqlitedb.rawQuery("select region from myset", null);

			
			
			while (c1.moveToNext()) {
				dong = c1.getString(c1.getColumnIndex("region"));

				tx.setText(dong);
			}

			Cursor c2 = sqlitedb.rawQuery("select ondo from temp where date=0 and time='"+t+"'", null);
			
			while(c2.moveToNext()){
				dong2 = c2.getString(c2.getColumnIndex("ondo"));

				tx2.setText(dong2);
			}
			
			c1.close();
			c2.close();
			
		} catch (SQLiteException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		if(dong2 == null || dong2.equals("")){ //ondo null 검사
			clo1.setText("Not Yet");
			clo2.setText("");
			clo3.setText("");
			clo4.setText("");
			clo5.setText("");
		}else{									//ondo가 null이 아닐때 코디추천
			pondo = Double.parseDouble(dong2);
			coordi();						
		}
		
	}
	
	public void coordi(){
		if(pondo>30){
			clo1.setText("매우 더움");
			clo2.setText("");
			clo3.setText("");
			clo4.setText("");
			clo5.setText("");
		}else if(pondo>26){
			clo1.setText("나시");
			clo2.setText("반바지");
			clo3.setText("민소매 원피스");
			clo4.setText("");
			clo5.setText("");
		}else if(pondo>22){
			clo1.setText("반팔");
			clo2.setText("얇은 셔츠");
			clo3.setText("얇은 긴팔");
			clo4.setText("반바지");
			clo5.setText("면바지");
		}else if(pondo>19){
			clo1.setText("긴팔티");
			clo2.setText("가디건");
			clo3.setText("후드티");
			clo4.setText("슬랙스");
			clo5.setText("스키니");
		}else if(pondo>16){
			//외투필수
			clo1.setText("외투");
			clo2.setText("니트");
			clo3.setText("맨투맨");
			clo4.setText("청바지");
			clo5.setText("원피스");
		}else if(pondo>11){
			clo1.setText("외투");
			clo2.setText("자켓");
			clo3.setText("가디건");
			clo4.setText("간절기 야상");
			clo5.setText("살색 스타킹");
		}else if(pondo>8){
			clo1.setText("외투");
			clo2.setText("간절기 야상");
			clo3.setText("트렌치코드");
			clo4.setText("");
			clo5.setText("");
		}else if(pondo<5){
			clo1.setText("외투");
			clo2.setText("코트");
			clo3.setText("가죽자켓");
			clo4.setText("");
			clo5.setText("");
		}else{
			clo1.setText("외투");
			clo2.setText("겨울옷");
			clo3.setText("기모스타킹");
			clo4.setText("장갑");
			clo5.setText("목도리");
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, AlarmSet.class);
			i.putExtra("weather", rain);
			startActivity(i);
			finish();
			// return true;
		}
		if (id == R.id.region) {
			Intent i = new Intent(this, Region.class);
			startActivity(i);
			finish();
			// return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
