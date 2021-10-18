package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.RecurrentUserDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.Convert;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonatype.guice.bean.binders.ParameterKeys;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {
    private static final Convert convert = new Convert();
    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static RecurrentUserDAO recurrentUserDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static final String vehicleRegNumberTest = "1-RSV-008"; //PUT A NUMBER THAT ISN'T IN THE PROD DB
    private static  ParkingService parkingService;
    private static Date outTime;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        recurrentUserDAO = new RecurrentUserDAO();
        recurrentUserDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        outTime = new Date();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
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
        //given
        parkingService.processIncomingVehicle();
        outTime.setTime(System.currentTimeMillis()+(120*60*1000));
        //When
        parkingService.processExitingVehicle(outTime);
        //Then
        assertTrue(recurrentUserDAO.checkIfRecurrentUser(vehicleRegNumberTest, 1));
    }
    
}
