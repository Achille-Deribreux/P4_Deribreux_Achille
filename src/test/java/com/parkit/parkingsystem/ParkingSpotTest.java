package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingSpotTest {

    //Integration comme ça ou TU indépendamment ?
    @Test
    public void ParkingSpotMethodTest(){
        ParkingSpot parkingSpot = new ParkingSpot(0, null, true);
        parkingSpot.setId(11);
        parkingSpot.setParkingType(ParkingType.CAR);
        parkingSpot.setAvailable(false);
        assertEquals(11, parkingSpot.getId());
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
    }
}
