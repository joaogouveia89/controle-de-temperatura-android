package com.example.joogouveia.controletemperatura.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.joogouveia.controletemperatura.SumTransition;
import com.example.joogouveia.controletemperatura.fragments.Summary;

import java.util.Calendar;

/**
 * Created by João Gouveia on 31/10/2017.
 */

public class FragmentPager extends FragmentPagerAdapter {

    private Summary summaryFragment;

    private Calendar cal = Calendar.getInstance();


    public FragmentPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 1){
            summaryFragment = new Summary();
        }else{
            return new SumTransition();
        }
        return summaryFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public Summary getSummaryFragment() {
        return summaryFragment;
    }
}
