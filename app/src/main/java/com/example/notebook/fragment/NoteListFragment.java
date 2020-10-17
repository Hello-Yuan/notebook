package com.example.notebook.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.notebook.R;
import com.example.notebook.activity.EditActivity;
import com.example.notebook.adapter.ListViewAdapter;
import com.example.notebook.bean.list_note;
import com.example.notebook.sqlite.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {

    private ListView listView;
    private List<list_note> lists;
    private ListViewAdapter listViewAdapter;
    private Button btn_refresh;


    public NoteListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tag", "onCreateView: note");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notelist, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("tag", "onActivityCreated: notelist");
        initView();
        initData();
        setAdapter();
    }


    public void initView(){
        listView = getActivity().findViewById(R.id.lv_note);
        btn_refresh = getActivity().findViewById(R.id.btn_refresh);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "onClick: ");
                initView();
                initData();
                listViewAdapter = new ListViewAdapter(lists,new ListViewAdapter.AdapterListener() {
                    @Override
                    public void setOnclickListener(int poistion) {
                        int lid = lists.get(poistion).getLid();
                        putUser(String.valueOf(lid));
                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        startActivity(intent);
                        Log.i("notelist", "setOnclickListener: "+lid);
                    }
                });
                listView.setAdapter(listViewAdapter);
            }
        });

    }

    public void initData(){
        lists =new ArrayList<>();
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(getActivity());
        SQLiteDatabase db =helper.getReadableDatabase();
        String user = getUser();
        String sql = "select * from t_notelist where user ="+user;
        Cursor cursor =db.rawQuery(sql,null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String notename = cursor.getString(cursor.getColumnIndex("notename"));
                int lid = cursor.getInt(cursor.getColumnIndex("lid"));
                list_note ln = new list_note(notename,lid);
                lists.add(ln);
            }
            cursor.close();
            db.close();
        }
    }

    public void setAdapter(){
        if(listViewAdapter == null){
            listViewAdapter = new ListViewAdapter(lists,new ListViewAdapter.AdapterListener() {
                @Override
                public void setOnclickListener(int poistion) {
                    int lid = lists.get(poistion).getLid();
                    putUser(String.valueOf(lid));
                    Intent intent = new Intent(getActivity(), EditActivity.class);
                    startActivity(intent);
                    Log.i("notelist", "setOnclickListener: "+lid);
                }
            });
            listView.setAdapter(listViewAdapter);
        }else {
            listViewAdapter.notifyDataSetChanged();
        }


    }
    public void putUser(String lid){
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lid",lid);
        editor.commit();
    }

    public String getUser(){
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        return sp.getString("user","");
    }
}
