package com.example.joogouveia.controletemperatura.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.joogouveia.controletemperatura.SumTransition;
import com.example.joogouveia.controletemperatura.Summary;
import com.example.joogouveia.controletemperatura.custom.calendar.CustomCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
