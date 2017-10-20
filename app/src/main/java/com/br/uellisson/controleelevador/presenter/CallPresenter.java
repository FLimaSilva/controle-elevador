package com.br.uellisson.controleelevador.presenter;

import java.util.Calendar;

/**
 * Created by uellisson on 25/09/2017.
 */

public class CallPresenter {

    public String getDate(){
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //TODO mês começa em 0 e vai até 11 por isso mont 9 == 10
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        String dd = String.valueOf(day);
        String mm = String.valueOf(month);
        String yy = String.valueOf(year);

        if (day<10){
            dd = "0"+dd;
        }
        if (month<10){
            mm = "0"+mm;
        }

        return dd+"-"+mm+"-"+yy;
    }

    public String getHour(){
        Calendar calendar = Calendar.getInstance();

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        String hourString = "" + h;
        if (h < 10){
            hourString = "0" + hourString;
        }
        String minuteString = "" + m;
        if (m < 10){
            minuteString = "0" + minuteString;
        }

        return hourString+":"+minuteString;
    }
}
