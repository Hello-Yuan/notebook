package com.example.notebook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.notebook.R;
import com.example.notebook.adapter.MyFragmentAdapter;
import com.example.notebook.fragment.ContentFragment;
import com.example.notebook.fragment.NewsFragment;
import com.example.notebook.fragment.NoteListFragment;
import com.example.notebook.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    private RadioGroup rg;
    private ViewPager viewPager;
    private List<Fragment> fragmentsView =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initView();
        initEvent();
    }

    public void initView(){
        rg = findViewById(R.id.rg);
        viewPager = findViewById(R.id.vp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewsFragment newsFragment = new NewsFragment();
        NoteListFragment notelistFragment = new NoteListFragment();
        ContentFragment contentFragment = new ContentFragment();
        SettingFragment settingFragment = new SettingFragment();
        fragmentsView.add(newsFragment);
        fragmentsView.add(notelistFragment);
        fragmentsView.add(contentFragment);
        fragmentsView.add(settingFragment);
        MyFragmentAdapter adapter = new MyFragmentAdapter(fragmentManager,fragmentsView);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(1);
    }

    public void initEvent(){
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbtn_news:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rbtn_notelist:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rbtn_write:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.rbtn_setting:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rg.check(R.id.rbtn_news);
                        break;
                    case 1:
                        rg.check(R.id.rbtn_notelist);
                        break;
                    case 2:
                        rg.check(R.id.rbtn_write);
                        break;
                    case 3:
                        rg.check(R.id.rbtn_setting);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
