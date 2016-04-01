package com.zhoubenliang.mykebiao.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.view.customview.ScheduleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeBiaoFragment extends Fragment {


    @Bind(R.id.scheduleView)
    ScheduleView scheduleView;

    public KeBiaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_biao, container, false);
        ButterKnife.bind(this, view);
        scheduleView.setclassTotal(8);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
