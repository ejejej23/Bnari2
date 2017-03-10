package com.example.bnariv2;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity {
	static final String[] LIST_MENU = { "time", "day" };
	int cYear, cMonth, cDay, cHour, cMinute;

	GregorianCalendar calendar = new GregorianCalendar();
	// �˶� �޴���
	private AlarmManager mManager;
	// ���� ���� �ɹ� ����
	private NotificationManager mNotification;
	// ���� ���� Ŭ����
	private DatePicker mDate;
	Button ng, no, ok;
	TextView tvTime, tvRain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		

		ng = (Button) findViewById(R.id.NG);
		no = (Button) findViewById(R.id.NO);
		ok = (Button) findViewById(R.id.OK);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvRain = (TextView) findViewById(R.id.tvRain);

		// ���� �Ŵ����� ���
		mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// �˶� �Ŵ����� ���
		mManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		WeatherParsing wpRain = new WeatherParsing(4, tvRain);

		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, LIST_MENU);

		cYear = calendar.get(calendar.YEAR);
		cMonth = calendar.get(calendar.MONTH);
		cDay = calendar.get(calendar.DAY_OF_MONTH);
		cHour = calendar.get(calendar.HOUR_OF_DAY);
		cMinute = calendar.get(calendar.MINUTE);

		Toast.makeText(this, calendar.HOUR_OF_DAY + "" + calendar.MINUTE,
				Toast.LENGTH_LONG).show();

		ListView listview = (ListView) findViewById(R.id.AlarmL);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(mItemClickListener);

		ng.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		no.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// �˶�����
				resetAlarm();
			}
		});

		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// �˶��ѱ�
				setAlarm();
			}
		});
	}

	private TimePickerDialog.OnTimeSetListener timeSetListner = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			String msg = String.format("%d : %d", hourOfDay, minute);

			cHour = hourOfDay;
			cMinute = minute;

			calendar.set(GregorianCalendar.YEAR, cYear);
			calendar.set(GregorianCalendar.MONTH, cMonth);
			calendar.set(GregorianCalendar.DATE, cDay);
			calendar.set(GregorianCalendar.HOUR_OF_DAY, cHour);
			calendar.set(GregorianCalendar.MINUTE, cMinute);
			calendar.set(GregorianCalendar.SECOND, 0);

			tvTime.setText(msg);

		}
	};

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		final String[] arr = { "������", "ȭ����", "������", "�����", "�ݿ���", "�����", "�Ͽ���" };
		final boolean[] arr2 = { false, false, false, false, false, false,
				false };

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long l_position) {

			String tv = (String) parent.getAdapter().getItem(position);
			// Toast.makeText(getApplicationContext(), tv,
			// Toast.LENGTH_SHORT).show();

			switch (position) {
			case 0:
				new TimePickerDialog(AlarmActivity.this, timeSetListner, cHour,
						cMinute, false).show();
				break;

			case 1:
				new AlertDialog.Builder(AlarmActivity.this)
						.setTitle("���� ����")
						.setMultiChoiceItems(
								arr,
								arr2,
								new DialogInterface.OnMultiChoiceClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton, boolean isChecked) {
										/*
										 * User clicked on a check box do some
										 * stuff
										 */
									}
								})
						.setPositiveButton("Ȯ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										/* User clicked Yes so do some stuff */
										Toast.makeText(
												getApplicationContext(),
												arr2[0] + "" + arr2[1] + ""
														+ arr2[2] + ""
														+ arr2[3] + ""
														+ arr2[4] + ""
														+ arr2[5] + ""
														+ arr2[6],
												Toast.LENGTH_SHORT).show();
									}
								}).create().show();
				break;
			}
		}

	};

	// �˶��� ����
	private void setAlarm() {
		int i = calendar.get(calendar.HOUR_OF_DAY);

		
		
		
		// �˶��� ������ �����ֱ�!!!!!-------�̿�
		// if�� �ȿ��� �׳� �� ������ �˻�
		if (true) {
			calendar.set(calendar.SECOND, 0);
			Toast.makeText(this, calendar.HOUR_OF_DAY + "" + calendar.MINUTE,
					Toast.LENGTH_LONG).show();
			mManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					pendingIntent());

		} else {
			resetAlarm();
			Toast.makeText(this, "�� �ȿ�", Toast.LENGTH_LONG).show();
		}
	}

	// �˶��� ����
	private void resetAlarm() {
		mManager.cancel(pendingIntent());
		Toast.makeText(this, "�˶�����", Toast.LENGTH_LONG).show();
	}

	// �˶��� ���� �ð��� �߻��ϴ� ����Ʈ �ۼ�
	private PendingIntent pendingIntent() {
		Intent i = new Intent(this, pop.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		return pi;
	}

}
