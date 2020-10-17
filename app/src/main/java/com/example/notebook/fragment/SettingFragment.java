package com.example.notebook.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.R;

import com.example.notebook.activity.MainActivity;
import com.example.notebook.sqlite.MySQLiteOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private TextView tv_sname;
    private TextView tv_suser;

    private Button btn_cpass;
    private Button btn_exit;
    private Context context;

    private boolean isFirst = true;
    public SettingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("tag", "onActivityCreated: setting");
        initView();
    }

    public void initView(){

        tv_sname = getActivity().findViewById(R.id.tv_sname);
        tv_suser = getActivity().findViewById(R.id.tv_suser);

        btn_cpass = getActivity().findViewById(R.id.btn_cpass);
        btn_exit = getActivity().findViewById(R.id.btn_exit);

        btn_cpass.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        context = getActivity();
        showInfo();

    }

    public void initPopWindow(View v){
        View view = LayoutInflater.from(context).inflate(R.layout.item_cpass, null, false);
        final EditText et_npass = view.findViewById(R.id.et_npass);
        final EditText et_npass_sec = view.findViewById(R.id.et_npass_sec);
        Button btn_npass = (Button) view.findViewById(R.id.btn_npass);

        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, 800, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 0, -1200);

        //设置popupWindow里的按钮的事件
        btn_npass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whereClause = "user = ?";
                String[] whereArgs = {getUser()};
                String npass = et_npass.getText().toString();
                String npass_sec = et_npass_sec.getText().toString();
                if(npass.length()>0||npass_sec.length()>0){
                    if(npass.equals(npass_sec)){
                        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(getActivity());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("pass",npass);
                        try{
                            //开启事务
                            db.beginTransaction();
                            //修改
                            db.update("t_user",contentValues,whereClause,whereArgs);
                            //设置成功
                            db.setTransactionSuccessful();
                            db.endTransaction();
                        }catch (Exception e){

                        }finally {
                            helper.close();
                        }
                        Toast.makeText(getActivity(), "修改成功，返回登录界面...", Toast.LENGTH_SHORT).show();
                        putState();
                        popWindow.dismiss();
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else {

                        Toast.makeText(getActivity(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    popWindow.dismiss();
                    Toast.makeText(getActivity(), "取消修改", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void showInfo(){
        String luser = getUser();
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from t_user";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String duser = cursor.getString(cursor.getColumnIndex("user"));
                String dname = cursor.getString(cursor.getColumnIndex("name"));
                if (luser.equals(duser)) {
                    tv_sname.setText(dname);
                    tv_suser.setText(duser);
                }
            }
            cursor.close();
            db.close();
        }
    }

    public void putState(){
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst",isFirst);
        editor.commit();
    }

    public String getUser(){
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
        return sp.getString("user","");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cpass:
                initPopWindow(v);
                break;

            case R.id.btn_exit:
                putState();
                Intent intent1 = new Intent(getActivity(),MainActivity.class);
                startActivity(intent1);
                getActivity().finish();
                break;
        }
    }
}
