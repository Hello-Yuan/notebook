package com.example.notebook.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.notebook.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomFragment extends Fragment {

    private RadioButton rbtn_notelist;
    private RadioButton rbtn_write;
    private RadioButton rbtn_setting;

    public BottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);
        /*initView(view);*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
        initView();
    }

    public void initView() {
        rbtn_notelist =getActivity().findViewById(R.id.rbtn_notelist);
        rbtn_write = getActivity().findViewById(R.id.rbtn_write);
        rbtn_setting = getActivity().findViewById(R.id.rbtn_setting);

    }
}
