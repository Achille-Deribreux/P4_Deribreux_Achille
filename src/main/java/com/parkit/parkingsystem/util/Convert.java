package com.parkit.parkingsystem.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import static java.lang.Double.parseDouble;

public class Convert {

    public double roundFloatToHundred(float floatToRound){
        DecimalFormat roundTwoAfter = new DecimalFormat();
        roundTwoAfter.setMaximumFractionDigits ( 2 ) ;
        return parseDouble(roundTwoAfter.format(floatToRound));
    }

    public double roundDoubleToHundred(Double doubleToRound){
        DecimalFormat roundTwoAfter = new DecimalFormat();
        roundTwoAfter.setMaximumFractionDigits ( 2 ) ;
        return parseDouble(roundTwoAfter.format(doubleToRound));
    }

    public String convertDateToShortString(Date dateToFormat){
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return shortDateFormat.format(dateToFormat);
    }
}
