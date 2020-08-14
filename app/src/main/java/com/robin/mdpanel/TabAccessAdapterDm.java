package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessAdapterDm extends FragmentPagerAdapter {

    public TabAccessAdapterDm(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DmRequestsFragment dmRequestsFragment=new DmRequestsFragment();
                return dmRequestsFragment;
            case 1:
                DmListFragment dmListFragment=new DmListFragment();
                return dmListFragment;

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
                return "DM Requests";


            case 1:
                return "Current DM";

            default:
                return null;

        }

    }


}
