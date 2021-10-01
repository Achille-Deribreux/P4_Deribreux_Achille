package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.Convert;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import static java.lang.Double.parseDouble;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {
    private static final Convert convert = new Convert();
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static final String vehicleRegNumberTest = "012345";
    private static final Integer parkedTimeInMinutes = 60;
    private static final Integer parkedTimeInMillis = parkedTimeInMinutes*60*1000;


    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumberTest);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        Date inTime = new Date(System.currentTimeMillis() - parkedTimeInMillis);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle(inTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        assertNotNull(ticketTest);
        assertEquals(vehicleRegNumberTest, ticketTest.getVehicleRegNumber());
        assertFalse(ticketTest.getParkingSpot().isAvailable());
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        Date outTime = new Date();
        parkingService.processExitingVehicle(outTime);
        Ticket ticketTest = ticketDAO.getTicket(vehicleRegNumberTest);
        assertNotNull(ticketTest);
        assertEquals(ticketTest.getPrice(), calculateFar(ticketTest));
        assertEquals(convert.convertDateToShortString(ticketTest.getOutTime()), convert.convertDateToShortString(outTime));
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

    private double calculateFar(Ticket ticket){
        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        float duration = (outHour - inHour) / 3_600_000.0f;
        return convert.roundDoubleToHundred(duration*Fare.CAR_RATE_PER_HOUR);
    }
}
