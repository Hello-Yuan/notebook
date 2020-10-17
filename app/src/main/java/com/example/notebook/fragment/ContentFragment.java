package com.example.notebook.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.sqlite.MySQLiteOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements View.OnClickListener {

    private EditText et_title;
    private EditText et_content;

    private Button btn_save;
    private Button btn_clear;

    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        Log.d("tag", "onActivityCreated: content");
    }

    public void initView(){
        et_title = (EditText)getActivity().findViewById(R.id.et_title);
        et_content = (EditText)getActivity().findViewById(R.id.et_content);

        btn_save = getActivity().findViewById(R.id.btn_save);
        btn_clear = getActivity().findViewById(R.id.btn_clear);

        btn_save.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String user = getUser();
                MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("notename",et_title.getText().toString());
                contentValues.put("content",et_content.getText().toString());
                contentValues.put("user",user);
                try{
                    //开启事务
                    db.beginTransaction();
                    //插入
                    db.insert("t_notelist",null,contentValues);
                    Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                    et_title.setText("");
                    et_content.setText("");
                    //设置成功
                    db.setTransactionSuccessful();

                    db.endTransaction();
                }catch (Exception e){

                }finally {
                    helper.close();
                }

                break;
            case R.id.btn_clear:
                et_title.setText("");
                et_content.setText("");
                break;
        }
    }

    public String getUser(){
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
        return sp.getString("user","");
    }
}
