package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RecurrentUserDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

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
