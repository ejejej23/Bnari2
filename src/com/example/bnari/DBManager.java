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
		
		// ������ ���� �µ� ���� ���̺�
		db.execSQL("create table temp(no integer,date text,time text,ondo text,weather text);");
		// �� ���� ���̺�
		db.execSQL("create table myset(no integer, region text);");

		//�� ���� �⺻ �ͺ������� �ϱ� 
		db.execSQL("insert into myset(no, region) values(1,'�ͺ���');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
