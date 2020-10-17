package com.example.notebook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notebook.R;
import com.example.notebook.bean.list_note;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List<list_note> list;
    private AdapterListener listener;
    public ListViewAdapter(List<list_note> list,AdapterListener listener) {
        this.list = list;
        this.listener=listener;
    }


    @Override//获取item个数
    public int getCount() {
        return list.size();
    }

    @Override//指定索引处的数据项item
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override//获取指定item对应得id
    public long getItemId(int position) {
        return position;
    }

    @Override//每绘制一个item就调用一次
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.listview_note,null);
            viewHolder = new ViewHolder();
            viewHolder.notename = convertView.findViewById(R.id.note_title);
            viewHolder.layout = convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.notename.setText(list.get(position).getNotename());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.setOnclickListener(position);
           }
       });
        return convertView;
    }
    class ViewHolder{
        private TextView notename;
        private RelativeLayout layout;
    }
    public interface  AdapterListener{
        void setOnclickListener(int poistion);
    }
    public void setListener(AdapterListener listener){
        this.listener=listener;
    }

}
