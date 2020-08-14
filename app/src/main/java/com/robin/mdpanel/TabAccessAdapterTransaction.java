package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessAdapterTransaction extends FragmentPagerAdapter {

    public TabAccessAdapterTransaction(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                GeneralParcelFragment generalParcelFragment=new GeneralParcelFragment();
                return generalParcelFragment;

            case 1:
                MerchantParcelFragment merchantParcelFragment=new MerchantParcelFragment();
                return merchantParcelFragment;

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
