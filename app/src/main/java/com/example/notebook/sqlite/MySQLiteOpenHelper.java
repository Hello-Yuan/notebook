package com.example.notebook.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/*
 * SQLiteOpenHelper 是一个管理数据库的帮助类*/
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    /*数据库名字*/
    private static final String DB_NAME = "notebookSys.db";
    /*版本号*/
    private static final int DB_VERSION = 1;

    private static MySQLiteOpenHelper helper;

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public synchronized static MySQLiteOpenHelper getHelper(Context context){
        if(helper==null){
            helper = new MySQLiteOpenHelper(context,DB_NAME,null,DB_VERSION);
        }
        return helper;
    }

    /*
     * 创建数据库时调用
     * 这个方法里面会创建表的操作
     * @param db*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_user(name text(16),user integer(11) primary key,pass text(64))");
        db.execSQL("create table t_notelist(lid integer primary key autoincrement,"+
                "notename text(64),content text(1024),user integer(11))");
    }


    /**
     * 数据库版本升级时调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
