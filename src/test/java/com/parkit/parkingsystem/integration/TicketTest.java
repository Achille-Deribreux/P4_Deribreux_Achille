package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.Convert;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {
    Ticket ticket = new Ticket();
    Convert convert = new Convert();

    //Tout dans le même en test Integration ou 1 par 1 en test Unitaire ?
    @Test
    public void ticketSetAndGetTest(){
        ticket.setId(12);
        ticket.setVehicleRegNumber("012345");
        ParkingSpot parkingSpot = new ParkingSpot(10, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(10);
        Date inTime = new Date(System.currentTimeMillis());
        ticket.setInTime(inTime);
        Date outTime = new Date(System.currentTimeMillis());
        ticket.setOutTime(outTime);
        assertEquals(12, ticket.getId());
        assertSame(parkingSpot, ticket.getParkingSpot());
        assertEquals("012345", ticket.getVehicleRegNumber());
        assertEquals(10, ticket.getPrice());
        assertEquals(convert.convertDateToShortString(inTime),convert.convertDateToShortString(ticket.getInTime()));
        assertEquals(convert.convertDateToShortString(outTime),convert.convertDateToShortString(ticket.getOutTime()));
    }

}
