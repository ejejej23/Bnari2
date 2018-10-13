package com.example.bnari;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

	public DBManager(Context context) {
		super(context, "myDB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// 어제와 오늘 온도 저장 테이블
		db.execSQL("create table temp(no integer,date text,time text,ondo text,weather text);");
		// 내 설정 테이블
		db.execSQL("create table myset(no integer, region text);");

		//내 설정 기본 와부읍으로 하기 
		db.execSQL("insert into myset(no, region) values(1,'와부읍');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
