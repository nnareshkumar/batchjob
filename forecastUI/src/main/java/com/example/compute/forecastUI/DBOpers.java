package com.example.compute.forecastUI;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.compute.forecastUI.DFUHistData;

/**
 * Created by j1008526 on 5/14/2016.
 */
    public class DBOpers {

    public static Connection con = null;

    public DBOpers() {
       // con = getConnection();

    }

    public Connection getConnection() {


            String connectionURL = "jdbc:mysql://127.0.0.1:3306/mysql?user=root";
            //url for google cloud "jdbc:mysql://104.155.136.125:3306/o12cr102?user=root"
        // local url jdbc:mysql://127.0.0.1:3306/mysql?user=root

            try {

                // Load the Driver class.
                Class.forName("com.mysql.jdbc.Driver");
                // If you are using any other database then load the right driver here.

                //Create the connection using the static getConnection method
                con = DriverManager.getConnection(connectionURL, "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return con;

    }

    public void cleanDFUDetails(Connection con, String productMarker) {

        try {
            if(con == null || con.isClosed()){
                con = getConnection();
            }

            PreparedStatement preparedStatement = con.prepareStatement(" DELETE FROM HIST WHERE DMDUNIT LIKE '" + productMarker +"%'");
            preparedStatement.execute();

            preparedStatement = con.prepareStatement(" DELETE FROM FCST WHERE DMDUNIT LIKE '" + productMarker +"%'");
            preparedStatement.execute();

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


    public void insertRecords(Connection con, List<DFUHistData> dfusHist)  {

        PreparedStatement ps = null;
        try {
            if(con.isClosed()){
                con = getConnection();
            }

            ps = con.prepareStatement(getInsertSQL());

            Iterator arrayIter = dfusHist.iterator();
            while(arrayIter.hasNext()) {

                DFUHistData dfuHistData = (DFUHistData) arrayIter.next();

                double[] hist = dfuHistData.getBaseHistory();
                int size = hist.length;
                ps.setString(1, dfuHistData.getDmdUnit());
                ps.setString(2, dfuHistData.getDmdGroup());
                ps.setString(3, dfuHistData.getLoc());
                Date startDate = new Date(dfuHistData.getDmdPostDate() + (10080L * 60 * 1000));
                ps.setDate(4, startDate);
                ps.setString(5, "BATCHHIST");
                ps.setString(6, "POS");
                ps.setInt(7, 1);

                for (int i = 0; i < size; i++) {
                    ps.setDouble(8 + i, hist[i]);
                }
                ps.addBatch();
            }
            ps.executeBatch();


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            if(ps != null) {
                try {
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<DFUHistData> retrieveRecords(Connection con)  {

        List<DFUHistData> dfusData = new ArrayList();
        try {
            if(con.isClosed()){
                con = getConnection();
            }


            PreparedStatement ps = con.prepareStatement(getSelectSQL());
            ps.setString(1, "ITEM%");
            ResultSet rs =  ps.executeQuery();

            double[] forecast = new double[104];

            while(rs.next()) {
                DFUHistData dfu = new DFUHistData(rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4), "POS");
                dfu.setDmdPostDate(rs.getDate(5).getTime());
                for( int i = 0; i < 104; i++ ) {
                    forecast[i] = rs.getDouble(9 + i);
                }
                dfu.setForecast(forecast);
                dfusData.add(dfu);

//                if(prevDfu == null || !prevDfu.equals(dfu)){
//                    prevDfu.setForecast(forecast);
//                    dfusData.add(prevDfu);
//                    prevDfu = dfu;
//                    i=0;
//                }else if(prevDfu !=null && prevDfu.equals(dfu)) {
//                    forecast[i++] = rs.getDouble(6);
//                }

                }
            return dfusData;

        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dfusData;
    }

    public String getInsertSQL () {
        String sql = "INSERT INTO HIST(DMDUNIT,  DMDGROUP,  LOC,  STARTDATE,  EVENT,  HISTSTREAM,  TYPE,  PERIOD1,  PERIOD2,  PERIOD3,  PERIOD4,  PERIOD5,  PERIOD6,  PERIOD7,  PERIOD8,  PERIOD9,  PERIOD10,  PERIOD11,  PERIOD12,  PERIOD13,  PERIOD14,  PERIOD15,  PERIOD16,  PERIOD17,  PERIOD18,  PERIOD19,  PERIOD20,  PERIOD21,  PERIOD22,  PERIOD23,  PERIOD24,  PERIOD25,  PERIOD26,  PERIOD27,  PERIOD28,  PERIOD29,  PERIOD30,  PERIOD31,  PERIOD32,  PERIOD33,  PERIOD34,  PERIOD35,  PERIOD36,  PERIOD37,  PERIOD38,  PERIOD39,  PERIOD40,  PERIOD41,  PERIOD42,  PERIOD43,  PERIOD44,  PERIOD45,  PERIOD46,  PERIOD47,  PERIOD48,  PERIOD49,  PERIOD50,  PERIOD51,  PERIOD52,  PERIOD53,  PERIOD54,  PERIOD55,  PERIOD56,  PERIOD57,  PERIOD58,  PERIOD59,  PERIOD60,  PERIOD61,  PERIOD62,  PERIOD63,  PERIOD64,  PERIOD65,  PERIOD66,  PERIOD67,  PERIOD68,  PERIOD69,  PERIOD70,  PERIOD71,  PERIOD72,  PERIOD73,  PERIOD74,  PERIOD75,  PERIOD76,  PERIOD77,  PERIOD78,  PERIOD79,  PERIOD80,  PERIOD81,  PERIOD82,  PERIOD83,  PERIOD84,  PERIOD85,  PERIOD86,  PERIOD87,  PERIOD88,  PERIOD89,  PERIOD90,  PERIOD91,  PERIOD92,  PERIOD93,  PERIOD94,  PERIOD95,  PERIOD96,  PERIOD97,  PERIOD98,  PERIOD99,  PERIOD100,  PERIOD101,  PERIOD102,  PERIOD103,  PERIOD104) " +
                "     VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
       return sql;
    }

    public String getSelectSQL () {
        String sql = "SELECT DMDUNIT,  DMDGROUP,  LOC, MODEL,  STARTDATE,  EVENT,  HISTSTREAM,  TYPE,  PERIOD1,  PERIOD2,  PERIOD3,  PERIOD4,  PERIOD5,  PERIOD6,  PERIOD7,  PERIOD8,  PERIOD9,  PERIOD10,  PERIOD11,  PERIOD12,  PERIOD13,  PERIOD14,  PERIOD15,  PERIOD16,  PERIOD17,  PERIOD18,  PERIOD19,  PERIOD20,  PERIOD21,  PERIOD22,  PERIOD23,  PERIOD24,  PERIOD25,  PERIOD26,  PERIOD27,  PERIOD28,  PERIOD29,  PERIOD30,  PERIOD31,  PERIOD32,  PERIOD33,  PERIOD34,  PERIOD35,  PERIOD36,  PERIOD37,  PERIOD38,  PERIOD39,  PERIOD40,  PERIOD41,  PERIOD42,  PERIOD43,  PERIOD44,  PERIOD45,  PERIOD46,  PERIOD47,  PERIOD48,  PERIOD49,  PERIOD50,  PERIOD51,  PERIOD52,  PERIOD53,  PERIOD54,  PERIOD55,  PERIOD56,  PERIOD57,  PERIOD58,  PERIOD59,  PERIOD60,  PERIOD61,  PERIOD62,  PERIOD63,  PERIOD64,  PERIOD65,  PERIOD66,  PERIOD67,  PERIOD68,  PERIOD69,  PERIOD70,  PERIOD71,  PERIOD72,  PERIOD73,  PERIOD74,  PERIOD75,  PERIOD76,  PERIOD77,  PERIOD78,  PERIOD79,  PERIOD80,  PERIOD81,  PERIOD82,  PERIOD83,  PERIOD84,  PERIOD85,  PERIOD86,  PERIOD87,  PERIOD88,  PERIOD89,  PERIOD90,  PERIOD91,  PERIOD92,  PERIOD93,  PERIOD94,  PERIOD95,  PERIOD96,  PERIOD97,  PERIOD98,  PERIOD99,  PERIOD100,  PERIOD101,  PERIOD102,  PERIOD103,  PERIOD104 FROM FCST WHERE DMDUNIT LIKE '?' ";
        return sql;
    }

    public static void closeConnection(Connection con) {
            // Close the connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }


}
