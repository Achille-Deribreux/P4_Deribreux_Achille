package com.parkit.parkingsystem.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import static java.lang.Double.parseDouble;

public class Convert {

    public double roundFloatToHundred(float floatToRound){
        BigDecimal bd = new BigDecimal(floatToRound);
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public double roundDoubleToHundred(Double doubleToRound){
        BigDecimal bd = new BigDecimal(doubleToRound);
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public String convertDateToShortString(Date dateToFormat){
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return shortDateFormat.format(dateToFormat);
    }

    public double roundToHundred(final double nb) {
        BigDecimal bd = new BigDecimal(nb);
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
