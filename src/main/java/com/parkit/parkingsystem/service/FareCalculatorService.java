package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.Convert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FareCalculatorService {
    private static final Convert convert = new Convert();
    private static final Logger logger = LogManager.getLogger("TicketDAO");
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

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
        if (checkIfRecurrentUser(vehicleRegNumber, 1)){
            discount = 5;
        }
        else {
            discount = 0;
        }
        return discount;
    }

    public boolean checkIfRecurrentUser(String vehicleRegNumber, int howManyToBeRecurrent){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECCURENT_USER);
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) >= howManyToBeRecurrent;
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }
}