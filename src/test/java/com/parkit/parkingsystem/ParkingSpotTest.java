package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParkingSpotTest {
    ParkingSpot parkingSpot;

    @BeforeEach
    private void setUpPerTest() throws Exception {
        parkingSpot = new ParkingSpot(0, null, false);
    }

    @Test
    public void parkingSpotSetAndGetIdTest(){
        //Given
        int idToSet = 123;
        int idGet;
        //When
        parkingSpot.setId(idToSet);
        idGet = parkingSpot.getId();
        //Then
        assertEquals(idToSet, idGet);
    }

    @Test
    public void parkingSpotSetAndGetTest(){
        //Given
        ParkingType parkingTypeToSet = ParkingType.BIKE;
        ParkingType parkingTypeToGet;
        //When
        parkingSpot.setParkingType(parkingTypeToSet);
        parkingTypeToGet = parkingSpot.getParkingType();
        //Then
        assertEquals(parkingTypeToSet,parkingTypeToGet);
    }
}
