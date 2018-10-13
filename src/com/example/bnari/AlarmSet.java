package com.example.bnari;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
 
public class AlarmSet extends Activity implements OnDateChangedListener, OnTimeChangedListener {
 
    // 알람 메니저
    private AlarmManager mManager;
    // 설정 일시
    private GregorianCalendar mCalendar;
    //일자 설정 클래스
    private DatePicker mDate;
    //시작 설정 클래스
    private TimePicker mTime;
    
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;
    
    boolean israin = false;
    public String mrain;
    String tday;
    String ptime;
    String w;

    String weather;
    int hour;
    int h;
    long l2;
    
    TextView t;
    
    /*
     * 통지 관련 맴버 변수
     */
    private NotificationManager mNotification;
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        
        mrain = getIntent().getStringExtra("weather"); 
        
        Calendar aCalendar = Calendar.getInstance();
        hour = aCalendar.get(Calendar.HOUR_OF_DAY);

        
        
   	 try{
         dbmanager=new DBManager(this);
         sqlitedb=dbmanager.getReadableDatabase();

			Cursor c2 = sqlitedb.rawQuery("select date,time from temp where weather='비'", null);
			
			while(c2.moveToNext()){
				tday = c2.getString(c2.getColumnIndex("date"));
				ptime = c2.getString(c2.getColumnIndex("time"));
				
				h = Integer.parseInt(ptime);
			}
			c2.close();
         
         
   	 	}catch (SQLiteException e){
   	 				Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
   	 		}

        //통지 매니저를 취득
        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
 
        //알람 매니저를 취득
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //현재 시각을 취득
        mCalendar = new GregorianCalendar();
        Log.i("HelloAlarmActivity",mCalendar.getTime().toString());
        //셋 버튼, 리셋버튼의 리스너를 등록
        setContentView(R.layout.alarmset);
        Button b = (Button)findViewById(R.id.set);
        b.setOnClickListener (new View.OnClickListener() {
            public void onClick (View v) {
            		setAlarm();
            	
            }
        });
 
        b = (Button)findViewById(R.id.reset);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetAlarm();
            }
        });
 
        //일시 설정 클래스로 현재 시각을 설정
        mDate = (DatePicker)findViewById(R.id.date_picker);
        mDate.init (mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        mTime = (TimePicker)findViewById(R.id.time_picker);
        mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        mTime.setOnTimeChangedListener(this);
    }
 
    //알람의 설정
    private void setAlarm() {
    	
        int i = mCalendar.getTime().getHours();
        
        if(h > i && tday != null){
        	mCalendar.set(mCalendar.SECOND, 0);
        	mManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent());
        }
        else
        {
        	resetAlarm();
        	Toast.makeText(this, "해제",  Toast.LENGTH_LONG).show();
        }
        	Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }
 
	//알람의 해제
    private void resetAlarm() {
        mManager.cancel(pendingIntent());
    }
    
    //알람의 설정 시각에 발생하는 인텐트 작성
    private PendingIntent pendingIntent() {
        Intent i = new Intent(this,pop.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
  }
 
    //일자 설정 클래스의 상태변화 리스너
    public void onDateChanged (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getCurrentHour(), mTime.getCurrentMinute());
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }
    //시각 설정 클래스의 상태변화 리스너
    public void onTimeChanged (TimePicker view, int hourOfDay, int minute) {
        mCalendar.set (mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
        Log.i("HelloAlarmActivity",mCalendar.getTime().toString());
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
			return true;
		}
		if(id==android.R.id.home){
			Intent i = new Intent(this,MainActivity.class);
			startActivity(i);
			finish();
		//	return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

}