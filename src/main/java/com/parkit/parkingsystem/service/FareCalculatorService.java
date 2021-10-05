package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.Convert;
import java.text.DecimalFormat;

import static java.lang.Double.parseDouble;

public class FareCalculatorService {
    private static final Convert convert = new Convert();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        float duration = (outHour - inHour) / 3_600_000.0f;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(calculatePrice(duration,Fare.CAR_RATE_PER_HOUR));
                break;
            }
            case BIKE: {
                ticket.setPrice(calculatePrice(duration,Fare.BIKE_RATE_PER_HOUR));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public double calculatePrice(float durationInHours, double pricing ){
        double finalPrice;
        if (durationInHours <= Fare.FREE_PARKING_TIME_IN_HOURS){
            finalPrice = 0;
        }
        else {
            finalPrice = durationInHours*pricing;
        }
        return convert.roundDoubleToHundred(finalPrice);
    }
}