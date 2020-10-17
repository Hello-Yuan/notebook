package com.example.notebook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.sqlite.MySQLiteOpenHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText et_user;
    private EditText et_password;

    private CheckBox ck_pass;

    private Button btn_login;
    private Button btn_register;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!getState()){
            Intent intent = new Intent(MainActivity.this,HomepageActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        initView();
    }

    public void initView(){
        et_user = (EditText)findViewById(R.id.et_user);
        et_password = (EditText)findViewById(R.id.et_password);

        ck_pass=findViewById(R.id.ck_pass);


        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        ck_pass.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String user = et_user.getText().toString();
                String pass = et_password.getText().toString();
                MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(this);
                SQLiteDatabase db =helper.getReadableDatabase();
                String sql = "select * from t_user";
                Cursor cursor = db.rawQuery(sql,null);
                if(cursor!=null&&cursor.getCount()>0){
                    boolean flag = false;
                    while (cursor.moveToNext()){
                        String duser = cursor.getString(cursor.getColumnIndex("user"));
                        String dpass = cursor.getString(cursor.getColumnIndex("pass"));
                        if(user.equals(duser)&&pass.equals(dpass)){
                            flag = true;
                            putUser(user);
                            isFirst = false;
                            putState();
                            Intent intent = new Intent(MainActivity.this,HomepageActivity.class);

                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                    }
                    if(flag == false){
                        Toast.makeText(MainActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.ck_pass:
                if(ck_pass.isChecked()==true){
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }

    public void putUser(String user){
        SharedPreferences sp = getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user",user);
        editor.commit();
    }

    public void putState(){
        SharedPreferences sp = getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst",isFirst);
        editor.commit();
    }



    public boolean getState(){
        SharedPreferences sp = getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
        return sp.getBoolean("isFirst",true);
    }

}
