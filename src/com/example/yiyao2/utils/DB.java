package com.example.yiyao2.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	public DB(Context context) {
		super(context, "MyDB.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table news(_id integer primary key autoincrement,title varchar,img varchar,tag varchar,count integer,time varchar,"
				+ "detailid varchar)");
		db.execSQL("create table lore(_id integer primary key autoincrement,title varchar,img varchar,tag varchar,count integer,time varchar,"
				+ "detailid varchar)");
		db.execSQL("create table drug(_id integer primary key autoincrement,title varchar,img varchar,ptype varchar,count integer,"
				+ "detailid varchar,factory varchar)");
		db.execSQL("create table cook(_id integer primary key autoincrement,title varchar,img varchar,tag varchar,count integer,"
				+ "detailid varchar,food varchar)");
		db.execSQL("create table symptom(_id integer primary key autoincrement,title varchar,img varchar,count integer,"
				+ "detailid varchar,place varchar)");
		// 数据库建表时要避免使用关键字作为表名 否则会报错
		db.execSQL("create table checkbody(_id integer primary key autoincrement,title varchar,img varchar,count integer,"
				+ "detailid varchar,checkmenu varchar)");
		db.execSQL("create table collect(_id integer primary key autoincrement,detailid varchar,fragment varchar,collecttitle varchar,collecttime varchar,collectimg varchar)");
	}
		
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
