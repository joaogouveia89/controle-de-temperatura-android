package com.example.joogouveia.controletemperatura.custom.calendar;

import android.app.Application;

import com.example.joogouveia.controletemperatura.R;
import com.example.joogouveia.controletemperatura.activities.Home;

/**
 * Created by João Gouveia on 01/11/2017.
 */

public class CustomCalendar{
    public static final int JANUARY             = 1;
    public static final int FEBRUARY            = 2;
    public static final int MARCH               = 3;
    public static final int APRIL               = 4;
    public static final int MAY                 = 5;
    public static final int JUNE                = 6;
    public static final int JULY                = 7;
    public static final int AUGUST              = 8;
    public static final int SEPTEMBER           = 9;
    public static final int OCTOBER             = 10;
    public static final int NOVEMBER            = 11;
    public static final int DECEMBER            = 12;

    private int currentMonth;
    private int currentYear;

    public CustomCalendar(){

    }

    public CustomCalendar(int month, int year){
        this.currentMonth = month;
        this.currentYear = year;
    }

    public void advanceOneMonth(){
        if(currentMonth < 12){
            currentMonth++;
        }else if(currentMonth == 12){
            currentMonth = 1;
            currentYear++;
        }
    }

    public void regressOneMonth(){
        if(currentMonth > 1){
            currentMonth--;
        }else if(currentMonth == 1){
            currentMonth = 12;
            currentYear--;
        }
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public String getMonthStringPtBr(){
        switch (currentMonth){
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "CustomCalendar{" +
                "currentMonth=" + currentMonth +
                ", currentYear=" + currentYear +
                '}';
    }
}
