package com.example.notebook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.sqlite.MySQLiteOpenHelper;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_title;
    private EditText et_content;
    private Button btn_change;
    private Button btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

    }

    public void initView(){

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);

        btn_change = findViewById(R.id.btn_change);
        btn_del = findViewById(R.id.btn_del);

        btn_change.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        String lid = getLid();
        showdata(lid);
    }

    public void showdata(String lid){
        String notename = null;
        String content = null;
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from t_notelist where lid ="+lid;
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                notename = cursor.getString(cursor.getColumnIndex("notename"));
                content = cursor.getString(cursor.getColumnIndex("content"));

            }
            cursor.close();
            db.close();
        }
        et_title.setText(notename);
        et_content.setText(content);
    }




    public String getLid(){
        SharedPreferences sp = getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        return sp.getString("lid","");
    }

    @Override
    public void onClick(View v) {
        String whereClause = "lid = ?";
        String[] whereArgs = {getLid()};
        switch (v.getId()){
            case R.id.btn_change:
                MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("notename",et_title.getText().toString());
                contentValues.put("content",et_content.getText().toString());
                try{
                    //开启事务
                    db.beginTransaction();
                    //修改
                    db.update("t_notelist",contentValues,whereClause,whereArgs);
                    //设置成功
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }catch (Exception e){

                }finally {
                    helper.close();
                }
                Toast.makeText(EditActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_del:
                MySQLiteOpenHelper helper1 = MySQLiteOpenHelper.getHelper(this);
                SQLiteDatabase db1 = helper1.getWritableDatabase();
                try{
                    //开启事务
                    db1.beginTransaction();
                    //修改
                    db1.delete("t_notelist",whereClause,whereArgs);
                    //设置成功
                    db1.setTransactionSuccessful();
                    db1.endTransaction();
                }catch (Exception e){
                }finally {
                    helper1.close();
                }
                Toast.makeText(EditActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }
}
