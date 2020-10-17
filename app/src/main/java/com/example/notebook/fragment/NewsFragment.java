package com.example.notebook.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.notebook.R;
import com.example.notebook.adapter.NewsAdapter;
import com.example.notebook.bean.NewsBean;
import com.example.notebook.util.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private ListView lv_news;
    private static final String TAG = "NewsFragment";
    private  final int NEWS_WHAT = 100;

    public NewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("tag", "onActivityCreated: news");
        initView();
        getData(Constant.baseUrl+ Constant.newsUrl2);
    }

    public void initView(){
        lv_news =getActivity().findViewById(R.id.lv_news);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEWS_WHAT:
                    List<NewsBean> newsList = (List<NewsBean>)msg.obj;
                    NewsAdapter adapter = new NewsAdapter(newsList);
                    lv_news.setAdapter(adapter);
                    break;
            }
        }
    };

    /**
     * OkHttp     获取新闻信息
     * @param url
     */
    public void getData(String url){
        // 1.创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 2.创建Request对象
        Request request =  new Request.Builder()
                .get()
                .url(url)
                .build();
        // 3.创建Call对象
        Call call =  client.newCall(request);
        //4.发送异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /*Log.d(TAG, "onResponse: "+response.code());*/
                /*Log.d(TAG, "onResponse: "+response.message());*/
                if(response.isSuccessful()){
                    String result = response.body().string();
                    /*Log.d(TAG, "onResponse: "+result);*/

                    Gson gson = new Gson();
                    //获得要转换成对象的类型，集合需要借助类型转换器
                    Type type = new TypeToken<List<NewsBean>>(){}.getType();
                    List<NewsBean> newList = gson.fromJson(result,type);
                    Message message = Message.obtain();
                    message.what = NEWS_WHAT;
                    message.obj = newList;
                    handler.sendMessage(message);


                }
            }
        });
    }
}
