package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessAdaptor extends FragmentPagerAdapter {

    public TabAccessAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                MerchantRequestFragment merchantRequest=new MerchantRequestFragment();
                return merchantRequest;

            case 1:
                MerchantListFragment merchantList =new MerchantListFragment();
                return merchantList;


            default:
                return null;

        }
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "MerChant Requests";


            case 1:
                return "Current Merchants";

            default:
                return null;

        }

    }
}
