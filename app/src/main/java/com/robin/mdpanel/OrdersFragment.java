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
public class OrdersFragment extends Fragment {

    private View ordersView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAccessAdapterOrder tabAccessAdapterOrder;



    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ordersView = inflater.inflate(R.layout.fragment_orders, container, false);


        viewPager=(ViewPager) ordersView.findViewById(R.id.main_tab_pager_orders);
        tabAccessAdapterOrder=new TabAccessAdapterOrder(getChildFragmentManager(),0);
        viewPager.setAdapter(tabAccessAdapterOrder);

        tabLayout=(TabLayout)ordersView.findViewById(R.id.main_tabs_orders);
        tabLayout.setupWithViewPager(viewPager);

        return ordersView;
    }

}
