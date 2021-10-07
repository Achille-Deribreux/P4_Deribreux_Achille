package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.Convert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertTest {
    private static final Convert convert = new Convert();
    double doubleToRound = 1.2299999999;

    @Test
    public void roundDoubleToHundredTest(){
        assertEquals( 1.23 , convert.roundDoubleToHundred(doubleToRound));
    }

    @Test
    public void convertDateToShortStringTest(){
        Date testDate = new Date();
        testDate.setTime(1000000000);
        assertEquals("12/01/70 14:46",convert.convertDateToShortString(testDate));
    }
}
