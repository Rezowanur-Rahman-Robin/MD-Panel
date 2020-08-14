package com.robin.mdpanel;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryManFragment extends Fragment {

    private View dmView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAccessAdapterDm tabAccessAdapterDm;


    public DeliveryManFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dmView = inflater.inflate(R.layout.fragment_delivery_man, container, false);

        viewPager=(ViewPager)dmView.findViewById(R.id.main_tab_pager_dm);
        tabAccessAdapterDm=new TabAccessAdapterDm(getChildFragmentManager(),0);
        viewPager.setAdapter(tabAccessAdapterDm);

        tabLayout=(TabLayout)dmView.findViewById(R.id.main_tabs_dm);
        tabLayout.setupWithViewPager(viewPager);



        return dmView;
    }

}
