package com.robin.mdpanel;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfficeManagerFragment extends Fragment {

    private View omView;
    private ViewPager myviewPager;
    private TabLayout tabLayout;
    private TabAccessAdapterOM tabAccessAdapterOM;



    public OfficeManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        omView = inflater.inflate(R.layout.fragment_office_manager, container, false);


        myviewPager=(ViewPager) omView.findViewById(R.id.main_tab_pager_om);
        tabAccessAdapterOM= new TabAccessAdapterOM(getChildFragmentManager(),0);
        myviewPager.setAdapter(tabAccessAdapterOM);

        tabLayout=(TabLayout) omView.findViewById(R.id.main_tabs_om);
        tabLayout.setupWithViewPager(myviewPager);



        return omView;
    }

}
