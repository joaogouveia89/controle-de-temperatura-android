package com.example.joogouveia.controletemperatura;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by João Gouveia on 18/10/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    private NewData tab1;
    private Summary tab0;

    public PagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                tab0 = new Summary();
                return tab0;
            case 1:
                tab1 = new NewData();
                return tab1;
            default:
                return null;
        }
    }

    public Fragment getNewData(){
        return tab1;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
