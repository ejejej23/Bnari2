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

	String[] sHour= new String[20]; // 예보시간(총 15개정도 받아옴 3일*5번)
	String[] sDay= new String[20]; // 날짜(몇번째날??)
	String[] sTemp= new String[20]; // 현재온도
	String[] sWfKor= new String[20]; // 날씨
	String[] sPty= new String[20]; // 강수상태

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
		// ZONE 코드를 지역 디비와 연동함.
		String strurl = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159068000";

		new DownloadWebpageTask().execute(strurl);
	}

	public WeatherParsing(int w, View v0, View v1, View v2) {
		flag = w;
		week0 = (TextView) v0;
		week1 = (TextView) v1;
		week2 = (TextView) v2;

		// ZONE 코드를 지역 디비와 연동함.
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

		// ZONE 코드를 지역 디비와 연동함.
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
				return "인터넷 연결 & 다운로드 실패";
			}
		}

		// 다운로드 결과를 실행함
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
						// 찾으려는 정보의 조건
						if (tag_name.equals("temp") && flag == 1) {
							bTemp1 = true;
						} else if (flag != 1) {

							if (xpp.getName().equals("hour")) { // 예보시간
								bHour = true;
							}
							if (xpp.getName().equals("day")) { // 예보날(오늘 내일 모레)
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
							if (bHour) { // 예보시각
								sHour[i] = xpp.getText();
								bHour = false;
							}
							if (bDay) { // 예보날짜
								sDay[i] = xpp.getText();
								bDay = false;
							}
							if (bTemp2) { // 현재온도
								sTemp[i] = xpp.getText();
								bTemp2 = false;
							}
							if (bWfKor) { // 날씨
								sWfKor[i] = xpp.getText();
								bWfKor = false;
							}
							if (bPty) { // 날씨
								//sPty[i] = Integer.parseInt(xpp.getText());
								sPty[i] = xpp.getText();
								bPty = false;
							}
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						if (xpp.getName().equals("data")) { // data태그는 예보시각기준
															// 예보정보가 하나씩이다.
							if (flag == 2) {
								week0.setText(" 오늘 " + "\n 온도 : " + sTemp[0]
										+ "\n 날씨 : " + sWfKor[0]);
								week1.setText(" 내일 " + "\n 온도 : " + sTemp[8]
										+ "\n 날씨 : " + sWfKor[8]);
								week2.setText(" 모레" + "\n 온도 : " + sTemp[16]
										+ "\n 날씨 : " + sWfKor[16]);
							} else if (flag == 3) {
								day0.setText(" 예측시간 : " + sHour[0] + "\n 온도 : "
										+ sTemp[0] + "\n 날씨 : " + sWfKor[0]);
								day1.setText(" 예측시간 : " + sHour[1] + "\n 온도 : "
										+ sTemp[1] + "\n 날씨 : " + sWfKor[1]);
								day2.setText(" 예측시간 : " + sHour[2] + "\n 온도 : "
										+ sTemp[2] + "\n 날씨 : " + sWfKor[2]);
								day3.setText(" 예측시간 : " + sHour[3] + "\n 온도 : "
										+ sTemp[3] + "\n 날씨 : " + sWfKor[3]);
								day4.setText(" 예측시간 : " + sHour[4] + "\n 온도 : "
										+ sTemp[4] + "\n 날씨 : " + sWfKor[4]);
								
							} else if (flag == 4) {
								if(Integer.parseInt(sDay[i]) == 1 && Integer.parseInt(sPty[i]) != 0){
									isRainTomo = true;
								}
							}
							i++; // 즉 data태그 == 예보 개수 그러므로 이때 array를 증가해주자
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

				// <? xml version="1.0" encoding="UTF-8" ?> 가 쓸모 없는 코드 였음
				// 그래서 먼저 실행 시킨 다음 그 다음 코드 저장
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