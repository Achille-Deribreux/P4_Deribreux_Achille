package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.text.DecimalFormat;

import static java.lang.Double.parseDouble;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        float duration = (outHour - inHour) / 3_600_000.0f;
        double roundDuration = roundToHundred(duration);
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(roundDuration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(roundDuration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
    public double roundToHundred(float floatToRound){
        DecimalFormat roundTwoAfter = new DecimalFormat();
        roundTwoAfter.setMaximumFractionDigits ( 2 ) ;
        return parseDouble(roundTwoAfter.format(floatToRound));
    }
}