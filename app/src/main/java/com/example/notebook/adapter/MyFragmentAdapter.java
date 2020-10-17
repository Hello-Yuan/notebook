package com.example.notebook.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    public MyFragmentAdapter(FragmentManager manager, List<Fragment> mFragmentList){
        super(manager);
        this.mFragmentList = mFragmentList;
    }
    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position){
        return  mFragmentList.get(position);
    }
}
