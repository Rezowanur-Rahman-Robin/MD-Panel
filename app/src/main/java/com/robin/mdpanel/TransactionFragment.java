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
public class TransactionFragment extends Fragment {

    private View transactionView;
    private ViewPager myViewPager;
    private TabLayout tabLayout;
    private TabAccessAdapterTransaction tabAccessAdapterTransaction;


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        transactionView = inflater.inflate(R.layout.fragment_transaction, container, false);

        myViewPager=(ViewPager)transactionView.findViewById(R.id.main_tab_pager_transaction);
        tabAccessAdapterTransaction=new TabAccessAdapterTransaction(getChildFragmentManager(),0);
        myViewPager.setAdapter(tabAccessAdapterTransaction);

        tabLayout=(TabLayout) transactionView.findViewById(R.id.main_tabs_transaction);
        tabLayout.setupWithViewPager(myViewPager);



        return transactionView;
    }

}
