package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static FareCalculatorService fareCalculatorService;

    private void processExitingVehicleTestSetup() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest(){
        //Given
        processExitingVehicleTestSetup();
        //When
        parkingService.processExitingVehicle();
        //Then
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void getVehichleTypeThrowExceptionTest() {
        //Given
        ParkingService parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO, ticketDAO, fareCalculatorService);
        //When
        when(inputReaderUtil.readSelection()).thenReturn(46);
        //Then
        assertThrows(IllegalArgumentException.class,  () -> parkingService.getVehichleType());
    }

    @Test
    public void getVehichleTypeReturnCarTest() {
        //Given
        ParkingService parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO, ticketDAO, fareCalculatorService);
        //When
        when(inputReaderUtil.readSelection()).thenReturn(1);
        //Then
        assertEquals(ParkingType.CAR,parkingService.getVehichleType());
    }

    //getVehicleTypeBike
    @Test
    public void getVehichleTypeReturnBikeTest() {
        //Given
        ParkingService parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO, ticketDAO, fareCalculatorService);
        //When
        when(inputReaderUtil.readSelection()).thenReturn(2);
        //Then
        assertEquals(ParkingType.BIKE,parkingService.getVehichleType());
    }
}
