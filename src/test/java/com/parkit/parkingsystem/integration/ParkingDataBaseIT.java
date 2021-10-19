package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.Convert;
import com.parkit.parkingsystem.util.InputReaderUtil;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {
    private static final Convert convert = new Convert();
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static final String vehicleRegNumberTest = "0123456";
    private static  ParkingService parkingService;
    private static Date outTime;
    private static FareCalculatorService fareCalculatorService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.dataBaseConfig = dataBaseTestConfig;
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        outTime = new Date();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, fareCalculatorService);
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumberTest);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        //Given
        parkingService.processIncomingVehicle();
        //When
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertNotNull(ticketTest);
        assertEquals(vehicleRegNumberTest, ticketTest.getVehicleRegNumber());
        assertFalse(ticketTest.getParkingSpot().isAvailable());
    }

    @Test
    public void testParkingLotExit(){
        //Given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(120*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertNotNull(outTime);
        assertEquals((((float) 120/60)*Fare.CAR_RATE_PER_HOUR),ticketTest.getPrice());
        assertEquals(convert.convertDateToShortString(outTime),convert.convertDateToShortString(ticketTest.getOutTime()));
    }

    @Test
    public void checkIfRecurrentUserTest(){
        //Given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(120*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        //Then
        assertTrue(fareCalculatorService.checkIfRecurrentUser(vehicleRegNumberTest, 1));
    }

    @Test
    public void calculateFarRecurrentUserTest() throws Exception {
        //Given
            //(first in and out)
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(120*60*1000));
        parkingService.processExitingVehicle(outTime);
            //(second in)
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(60*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertEquals(convert.roundDoubleToHundred((Fare.CAR_RATE_PER_HOUR) * 0.95), ticketTest.getPrice());
    }

    @Test
    public void calculateFarNonRecurrentUserTest(){
        //Given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(180*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertEquals(convert.roundDoubleToHundred((3 * Fare.CAR_RATE_PER_HOUR)), ticketTest.getPrice());
    }

    @Test
    public void calculateFarFreeTimeParkingTest(){
        //Given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(20*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertEquals(0, ticketTest.getPrice());
    }

    @Test
    public void calculateFarMoreThanADayParkingTest(){
        //Given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(30*60*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertEquals(30*Fare.CAR_RATE_PER_HOUR, ticketTest.getPrice());
    }

    @Test
    public void calculateFarAYearParkingTimeTest(){
        //Given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(365L*24*60*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        //Then
        assertEquals(365*24*Fare.CAR_RATE_PER_HOUR, ticketTest.getPrice());
    }
}
