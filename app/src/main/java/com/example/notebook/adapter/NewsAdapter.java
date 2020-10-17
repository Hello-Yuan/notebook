package com.example.notebook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notebook.R;
import com.example.notebook.bean.NewsBean;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private List<NewsBean> newsList;
    private static final int TYPE_ONE = 1;
    private static final int TYPE_SECOND = 2;

    public  NewsAdapter(List<NewsBean>  newsList){
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){
        if(newsList.get(position).getType() == 1){
            return TYPE_ONE;
        }else if(newsList.get(position).getType() == 2){
            return TYPE_SECOND;
        }else {
            return TYPE_ONE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderOne holder = null;
        ViewHolderTwo holdertwo = null;
        int type = getItemViewType(position);
        if(type == TYPE_ONE){
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.listview_news,null);
                holder = new ViewHolderOne();
                holder.news_img =  convertView.findViewById(R.id.news_img);
                holder.news_name = convertView.findViewById(R.id.news_title);
                holder.news_content = convertView.findViewById(R.id.news_content);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolderOne) convertView.getTag();
            }
        }else {
            if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.listview_news_sec, null);
                holdertwo = new ViewHolderTwo();
                holdertwo.news_img1 = convertView.findViewById(R.id.news_img1);
                holdertwo.news_img2 = convertView.findViewById(R.id.news_img2);
                holdertwo.news_img3 = convertView.findViewById(R.id.news_img3);
                holdertwo.news_name = convertView.findViewById(R.id.news_title_sec);
                convertView.setTag(holdertwo);
            }else{
                holdertwo = (ViewHolderTwo) convertView.getTag();
            }
        }




        /**
         *  图片的三级缓存：      内存中（Map）   -->   存储卡    --->   网络
         *                        只要下载下来后，就需要把图片缓存到内存中和存储卡中
         *
         *  Glide 是一个带缓存图片加载框架
         *
         */
        String imgUrl =  newsList.get(position).getImg1();
        String imgUrl2 =  newsList.get(position).getImg2();
        String imgUrl3 =  newsList.get(position).getImg3();
        if(type == TYPE_ONE){
            Glide.with(parent.getContext()).load(imgUrl).into(holder.news_img);
            holder.news_name.setText(newsList.get(position).getNewsName());
            holder.news_content.setText(newsList.get(position).getNewsContent());
        }else {
            Glide.with(parent.getContext()).load(imgUrl).into(holdertwo.news_img1);
            Glide.with(parent.getContext()).load(imgUrl2).into(holdertwo.news_img2);
            Glide.with(parent.getContext()).load(imgUrl3).into(holdertwo.news_img3);
            holdertwo.news_name.setText(newsList.get(position).getNewsName());
        }

        return convertView;
    }


    class ViewHolderOne{
        ImageView news_img;
        TextView news_name;
        TextView news_content;
    }
    class ViewHolderTwo{
        ImageView news_img1;
        ImageView news_img2;
        ImageView news_img3;
        TextView news_name;
    }
}
