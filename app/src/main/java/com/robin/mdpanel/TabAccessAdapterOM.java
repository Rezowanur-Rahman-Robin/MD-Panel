package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessAdapterOM extends FragmentPagerAdapter {

    public TabAccessAdapterOM(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                AddOmFragment addOmFragment=new AddOmFragment();
                return addOmFragment;
            case 1:
                OmListFragment omListFragment=new OmListFragment();
                return omListFragment;

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
                return "Add Manager";


            case 1:
                return "Current Managers";

            default:
                return null;

        }

    }



}
