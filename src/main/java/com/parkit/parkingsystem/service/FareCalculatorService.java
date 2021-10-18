package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.RecurrentUserDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.Convert;

public class FareCalculatorService {
    private static final Convert convert = new Convert();
    public static TicketDAO ticketDAO = new TicketDAO();
    public static RecurrentUserDAO recurrentUserDAO = new RecurrentUserDAO();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        int discount = getDiscountRecurrentUser(ticket.getVehicleRegNumber());
        float duration = (outHour - inHour) / 3_600_000.0f;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(calculatePrice(duration,Fare.CAR_RATE_PER_HOUR, discount));
                break;
            }
            case BIKE: {
                ticket.setPrice(calculatePrice(duration,Fare.BIKE_RATE_PER_HOUR, discount));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public double calculatePrice(float durationInHours, double pricing, int discount ){
        double finalPrice;
        if (durationInHours <= Fare.FREE_PARKING_TIME_IN_HOURS){
            finalPrice = 0;
        }
        else {
            finalPrice = (durationInHours * pricing)*(((float)100-discount)/100);
        }
        return convert.roundDoubleToHundred(finalPrice);
    }

    public int getDiscountRecurrentUser (String vehicleRegNumber){
        int discount;
        if (recurrentUserDAO.checkIfRecurrentUser(vehicleRegNumber, 1)){
            discount = 5;
        }
        else {
            discount = 0;
        }
        return discount;
    }
}