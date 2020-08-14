package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessAdapterOrder extends FragmentPagerAdapter {

    public TabAccessAdapterOrder(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                GeneralOrdersFragment generalOrdersFragment=new GeneralOrdersFragment();
                return generalOrdersFragment;
            case 1:
                MerchantOrdersFragment merchantOrdersFragment=new MerchantOrdersFragment();
                return merchantOrdersFragment;

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
                return "General Parcel";


            case 1:
                return "Merchant Parcel";

            default:
                return null;

        }

    }

}
