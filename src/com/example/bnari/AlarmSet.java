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
 
    // �˶� �޴���
    private AlarmManager mManager;
    // ���� �Ͻ�
    private GregorianCalendar mCalendar;
    //���� ���� Ŭ����
    private DatePicker mDate;
    //���� ���� Ŭ����
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
     * ���� ���� �ɹ� ����
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

			Cursor c2 = sqlitedb.rawQuery("select date,time from temp where weather='��'", null);
			
			while(c2.moveToNext()){
				tday = c2.getString(c2.getColumnIndex("date"));
				ptime = c2.getString(c2.getColumnIndex("time"));
				
				h = Integer.parseInt(ptime);
			}
			c2.close();
         
         
   	 	}catch (SQLiteException e){
   	 				Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
   	 		}

        //���� �Ŵ����� ���
        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
 
        //�˶� �Ŵ����� ���
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //���� �ð��� ���
        mCalendar = new GregorianCalendar();
        Log.i("HelloAlarmActivity",mCalendar.getTime().toString());
        //�� ��ư, ���¹�ư�� �����ʸ� ���
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
 
        //�Ͻ� ���� Ŭ������ ���� �ð��� ����
        mDate = (DatePicker)findViewById(R.id.date_picker);
        mDate.init (mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        mTime = (TimePicker)findViewById(R.id.time_picker);
        mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        mTime.setOnTimeChangedListener(this);
    }
 
    //�˶��� ����
    private void setAlarm() {
    	
        int i = mCalendar.getTime().getHours();
        
        if(h > i && tday != null){
        	mCalendar.set(mCalendar.SECOND, 0);
        	mManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent());
        }
        else
        {
        	resetAlarm();
        	Toast.makeText(this, "����",  Toast.LENGTH_LONG).show();
        }
        	Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }
 
	//�˶��� ����
    private void resetAlarm() {
        mManager.cancel(pendingIntent());
    }
    
    //�˶��� ���� �ð��� �߻��ϴ� ����Ʈ �ۼ�
    private PendingIntent pendingIntent() {
        Intent i = new Intent(this,pop.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
  }
 
    //���� ���� Ŭ������ ���º�ȭ ������
    public void onDateChanged (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getCurrentHour(), mTime.getCurrentMinute());
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }
    //�ð� ���� Ŭ������ ���º�ȭ ������
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