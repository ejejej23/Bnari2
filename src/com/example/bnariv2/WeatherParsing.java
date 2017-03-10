package com.example.bnariv2;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WeatherParsing extends Activity {
	TextView tv;
	TextView week0;
	TextView week1;
	TextView week2;
	TextView day0;
	TextView day1;
	TextView day2;
	TextView day3;
	TextView day4;

	int flag;

	String[] sHour= new String[20]; // �����ð�(�� 15������ �޾ƿ� 3��*5��)
	String[] sDay= new String[20]; // ��¥(���°��??)
	String[] sTemp= new String[20]; // ����µ�
	String[] sWfKor= new String[20]; // ����
	String[] sPty= new String[20]; // ��������

	boolean bHour;
	boolean bDay;
	boolean bTemp1;
	boolean bTemp2;
	boolean bWfKor;
	boolean bPty;
	boolean isRainTomo;
	String Test = "empty";

	public WeatherParsing(int w, View v) {
		flag = w;
		tv = (TextView) v;
		isRainTomo = false;
		// ZONE �ڵ带 ���� ���� ������.
		String strurl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159068000";

		new DownloadWebpageTask().execute(strurl);
	}

	public WeatherParsing(int w, View v0, View v1, View v2) {
		flag = w;
		week0 = (TextView) v0;
		week1 = (TextView) v1;
		week2 = (TextView) v2;

		// ZONE �ڵ带 ���� ���� ������.
		String strurl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159068000";

		new DownloadWebpageTask().execute(strurl);
	}

	public WeatherParsing(int w, View v0, View v1, View v2, View v3, View v4) {
		flag = w;
		day0 = (TextView) v0;
		day1 = (TextView) v1;
		day2 = (TextView) v2;
		day3 = (TextView) v3;
		day4 = (TextView) v4;

		// ZONE �ڵ带 ���� ���� ������.
		String strurl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159068000";

		new DownloadWebpageTask().execute(strurl);
	}

	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				return (String) downloadUrl((String) urls[0]);
			} catch (IOException e) {
				return "���ͳ� ���� & �ٿ�ε� ����";
			}
		}

		// �ٿ�ε� ����� ������
		protected void onPostExecute(String result) {

			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();

				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				xpp.setInput(new StringReader(result));
				int eventType = xpp.getEventType();
				boolean bSet = false;
				String tag_name;
				int i = 0;

				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
						;
					} else if (eventType == XmlPullParser.START_TAG) {
						tag_name = xpp.getName();
						// ã������ ������ ����
						if (tag_name.equals("temp") && flag == 1) {
							bTemp1 = true;
						} else if (flag != 1) {

							if (xpp.getName().equals("hour")) { // �����ð�
								bHour = true;
							}
							if (xpp.getName().equals("day")) { // ������(���� ���� ��)
								bDay = true;
							}
							if (tag_name.equals("temp")) {
								bTemp2 = true;
							}
							if (tag_name.equals("wfKor")) {
								bWfKor = true;
							}
							if (tag_name.equals("pty")) {
								bPty = true;
							}
						}

					} else if (eventType == XmlPullParser.TEXT) {
						if (bTemp1) {
							String content = xpp.getText();
							tv.setText(content);
							bTemp1 = false;
							flag = 11;
						} else if (flag >= 2 && flag <= 4) {
							if (bHour) { // �����ð�
								sHour[i] = xpp.getText();
								bHour = false;
							}
							if (bDay) { // ������¥
								sDay[i] = xpp.getText();
								bDay = false;
							}
							if (bTemp2) { // ����µ�
								sTemp[i] = xpp.getText();
								bTemp2 = false;
							}
							if (bWfKor) { // ����
								sWfKor[i] = xpp.getText();
								bWfKor = false;
							}
							if (bPty) { // ����
								//sPty[i] = Integer.parseInt(xpp.getText());
								sPty[i] = xpp.getText();
								bPty = false;
							}
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						if (xpp.getName().equals("data")) { // data�±״� �����ð�����
															// ���������� �ϳ����̴�.
							if (flag == 2) {
								week0.setText(" ���� " + "\n �µ� : " + sTemp[0]
										+ "\n ���� : " + sWfKor[0]);
								week1.setText(" ���� " + "\n �µ� : " + sTemp[8]
										+ "\n ���� : " + sWfKor[8]);
								week2.setText(" ��" + "\n �µ� : " + sTemp[16]
										+ "\n ���� : " + sWfKor[16]);
							} else if (flag == 3) {
								day0.setText(" �����ð� : " + sHour[0] + "\n �µ� : "
										+ sTemp[0] + "\n ���� : " + sWfKor[0]);
								day1.setText(" �����ð� : " + sHour[1] + "\n �µ� : "
										+ sTemp[1] + "\n ���� : " + sWfKor[1]);
								day2.setText(" �����ð� : " + sHour[2] + "\n �µ� : "
										+ sTemp[2] + "\n ���� : " + sWfKor[2]);
								day3.setText(" �����ð� : " + sHour[3] + "\n �µ� : "
										+ sTemp[3] + "\n ���� : " + sWfKor[3]);
								day4.setText(" �����ð� : " + sHour[4] + "\n �µ� : "
										+ sTemp[4] + "\n ���� : " + sWfKor[4]);
								
							} else if (flag == 4) {
								if(Integer.parseInt(sDay[i]) == 1 && Integer.parseInt(sPty[i]) != 0){
									isRainTomo = true;
								}
							}
							i++; // �� data�±� == ���� ���� �׷��Ƿ� �̶� array�� ����������
						}
					}
					

					if (flag == 11) {
						break;
					}else if(flag == 4 ){
						tv.setText(isRainTomo +"");
					}
					eventType = xpp.next();

				}
			} catch (Exception e) {
				Log.getStackTraceString(e);
			}
		}

		private String downloadUrl(String myurl) throws IOException {
			HttpURLConnection conn = null;
			try {
				URL url = new URL(myurl);
				conn = (HttpURLConnection) url.openConnection();
				BufferedInputStream buf = new BufferedInputStream(
						conn.getInputStream());
				BufferedReader bufreader = new BufferedReader(
						new InputStreamReader(buf, "utf-8"));
				String line = null;
				String page = " ";

				// <? xml version="1.0" encoding="UTF-8" ?> �� ���� ���� �ڵ� ����
				// �׷��� ���� ���� ��Ų ���� �� ���� �ڵ� ����
				line = bufreader.readLine();
				while ((line = bufreader.readLine()) != null) {
					page += line;
				}
				return page;
			} finally {
				conn.disconnect();
			}
		}
	}
}