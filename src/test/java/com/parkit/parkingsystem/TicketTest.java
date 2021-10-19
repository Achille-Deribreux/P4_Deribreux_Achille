package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.Convert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TicketTest {
    private static Ticket ticketTest;
    private static Convert convert;

    @BeforeEach
    private void setUpPerTest() throws Exception {
        ticketTest = new Ticket();
    }

    @BeforeAll
    private static void setUp() throws Exception{
        convert = new Convert();
    }

    @Test
    public void ticketIdSetAndGetTest(){
        //Given
        int getId;
        int setId = 122;
        //When
        ticketTest.setId(setId);
        getId = ticketTest.getId();
        //Then
        assertEquals(122, getId);
    }

    @Test
    public void ticketParkingSpotSetAndGetTest(){
        //Given
        ParkingSpot parkingSpot = new ParkingSpot(122, ParkingType.CAR, true);
        //When
        ticketTest.setParkingSpot(parkingSpot);
        //Then
        assertSame(parkingSpot, ticketTest.getParkingSpot());
    }

    @Test
    public void ticketVehicleRegNumberSetAndGetTest(){
        //Given
        String setVehicleRegNumber = "012345";
        String getVehicleRegNumber;
        //When
        ticketTest.setVehicleRegNumber(setVehicleRegNumber);
        getVehicleRegNumber = ticketTest.getVehicleRegNumber();
        //Then
        assertEquals(setVehicleRegNumber,getVehicleRegNumber );
    }

    @Test
    public void ticketPriceSetAndGetTest(){
        //Given
        int setPrice = 129;
        double getPrice;
        //When
        ticketTest.setPrice(setPrice);
        getPrice = ticketTest.getPrice();
        //Then
        assertEquals(setPrice, getPrice);
    }

    @Test
    public void ticketInTimeSetAndGetTest(){
        //Given
        Date setInTime = new Date();
        Date getInTime;
        //When
        ticketTest.setInTime(setInTime);
        getInTime = ticketTest.getInTime();
        //Then
        assertEquals(convert.convertDateToShortString(setInTime),convert.convertDateToShortString(getInTime));
    }

    @Test
    public void ticketOutTimeSetAndGetTest(){
        //Given
        Date setOutTime = new Date();
        Date getOutTime;
        //When
        ticketTest.setOutTime(setOutTime);
        getOutTime = ticketTest.getOutTime();
        //Then
        assertEquals(convert.convertDateToShortString(setOutTime),convert.convertDateToShortString(getOutTime));

    }
}
