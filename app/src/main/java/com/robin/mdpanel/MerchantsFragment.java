package com.robin.mdpanel;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantsFragment extends Fragment {

    private View merchantView;
    private Toolbar toolbar;
    private ViewPager myViewPager;
    private TabLayout tabLayout;
    private TabAccessAdaptor mytabAccessAdapter;


    public MerchantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        merchantView = inflater.inflate(R.layout.fragment_merchants, container, false);




        myViewPager=(ViewPager) merchantView.findViewById(R.id.main_tab_pager_merchant);
        mytabAccessAdapter=new TabAccessAdaptor(getChildFragmentManager(),0);
        myViewPager.setAdapter(mytabAccessAdapter);

        tabLayout=(TabLayout) merchantView.findViewById(R.id.main_tabs_merchant);
        tabLayout.setupWithViewPager(myViewPager);





        return merchantView;
    }

}
