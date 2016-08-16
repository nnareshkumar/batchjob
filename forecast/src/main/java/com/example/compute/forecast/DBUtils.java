package com.example.compute.forecast;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.compute.forecast.DFUData;


/**
 * Created by j1008526 on 5/14/2016.
 */
public class DBUtils {

    public static Connection con = null;

    public DBUtils() {
       // con = getConnection();

    }

    public Connection getConnection() {


            String connectionURL = "jdbc:mysql://127.0.0.1:3306/mysql?user=root";
        //"url for google cloud "jdbc:mysql://104.155.136.125:3306/o12cr102?user=root";
        // url for local machine "jdbc:mysql://127.0.0.1:3306/mysql?user=root"

            try {

                // Load the Driver class.
                Class.forName("com.mysql.jdbc.Driver");
                //same driver name is used even for google cloud
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



    protected String getDfuQuery(int startValue, int endValue) {
        String retValue =
                " SELECT DMDUNIT,  DMDGROUP,  LOC,  STARTDATE,  EVENT,  HISTSTREAM,  TYPE,  PERIOD1,  PERIOD2,  PERIOD3,  PERIOD4,  PERIOD5,  PERIOD6,  PERIOD7,  PERIOD8,  PERIOD9,  PERIOD10,  PERIOD11,  PERIOD12,  PERIOD13,  PERIOD14,  PERIOD15,  PERIOD16,  PERIOD17,  PERIOD18,  PERIOD19,  PERIOD20,  PERIOD21,  PERIOD22,  PERIOD23,  PERIOD24,  PERIOD25,  PERIOD26,  PERIOD27,  PERIOD28,  PERIOD29,  PERIOD30,  PERIOD31,  PERIOD32,  PERIOD33,  PERIOD34,  PERIOD35,  PERIOD36,  PERIOD37,  PERIOD38,  PERIOD39,  PERIOD40,  PERIOD41,  PERIOD42,  PERIOD43,  PERIOD44,  PERIOD45,  PERIOD46,  PERIOD47,  PERIOD48,  PERIOD49,  PERIOD50,  PERIOD51,  PERIOD52,  PERIOD53,  PERIOD54,  PERIOD55,  PERIOD56,  PERIOD57,  PERIOD58,  PERIOD59,  PERIOD60,  PERIOD61,  PERIOD62,  PERIOD63,  PERIOD64,  PERIOD65,  PERIOD66,  PERIOD67,  PERIOD68,  PERIOD69,  PERIOD70,  PERIOD71,  PERIOD72,  PERIOD73,  PERIOD74,  PERIOD75,  PERIOD76,  PERIOD77,  PERIOD78,  PERIOD79,  PERIOD80,  PERIOD81,  PERIOD82,  PERIOD83,  PERIOD84,  PERIOD85,  PERIOD86,  PERIOD87,  PERIOD88,  PERIOD89,  PERIOD90,  PERIOD91,  PERIOD92,  PERIOD93,  PERIOD94,  PERIOD95,  PERIOD96,  PERIOD97,  PERIOD98,  PERIOD99,  PERIOD100,  PERIOD101,  PERIOD102,  PERIOD103,  PERIOD104 " +
                "FROM HIST WHERE DMDUNIT LIKE  '%-" + startValue +"%'";// for pagination query // OR  DMDUNIT LIKE  '%-" + endValue +"%'";

        return retValue;
    }

    public List<DFUData> fetchDFUDetails(Connection con, int startValue, int endValue) {

        ResultSet rs = null;
        DFUData prevDfuData = null;

        List<DFUData> dfuDatas = new ArrayList();

        try {
            if(con == null || con.isClosed()){
                con = getConnection();
            }
            DFUData dfuData = null;

            PreparedStatement preparedStatement = con.prepareStatement(getDfuQuery(startValue, endValue));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                //DMDUNIT,  DMDGROUP,  LOC,  STARTDATE,  EVENT,  HISTSTREAM,  TYPE,  PERIOD1,
                String dmdUnit = rs.getString(1);
                String dmdGroup = rs.getString(2);
                String loc = rs.getString(3);
                long date = rs.getDate(4).getTime();
                String histStream = rs.getString(6);
                dfuData = new DFUData(dmdUnit, dmdGroup, loc, "HOLTWINTER", histStream);
                dfuData.setHistStartDate(date);
                double[] history = new double[104];

                for( int index = 0; index < 104; index++) {
                    history[index] = rs.getDouble(8 + index);
                }
                if (dfuData != null) {
                    dfuData.setBaseHistory(history);
                    dfuDatas.add(dfuData);
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    return dfuDatas;

    }


    public void insertRecords(Connection con, ArrayList arrayList)  {

        try {
            if(con.isClosed()){
                con = getConnection();
            }

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Iterator arrayIter = arrayList.iterator();
            while(arrayIter.hasNext()) {
                List<DFUData> dfusData = (List<DFUData>) arrayIter.next();

                int periodLength = 12;
                for (int i = 0 ; i < dfusData.size(); i++) {
                    DFUData dfuData = (DFUData) dfusData.get(i);
                    if( dfuData != null && dfuData.getForecast().length > 0) {
                        periodLength = dfuData.getForecast().length > periodLength ? dfuData.getForecast().length : periodLength;
                    }
                }

                PreparedStatement ps = con.prepareStatement(getInsertSQL(periodLength));
                Iterator iter = dfusData.iterator();
                while (iter.hasNext()) {
                    DFUData dfuData = (DFUData) iter.next();
                    // DMDUNIT, DMDGROUP, LOC,  STARTDATE ,  TYPE  ,  FCSTID  ,  MODEL    ,  LEWMEANQTY   ,  MARKETMGRVERSIONID ,  DUR  ,  PERIOD1
                    int size = dfuData.getForecast().length;
                        ps.setString(1, dfuData.getDmdUnit());
                        ps.setString(2, dfuData.getDmdGroup());
                        ps.setString(3, dfuData.getLoc());
                        Date startDate = new Date(dfuData.getDmdPostDate() + (10080L * 60 * 1000 ));
                        ps.setDate(4, startDate);
                        ps.setInt(5, 1);
                        ps.setString(6, "BATCH_JEE7");
                        ps.setString(7, dfuData.getModel());
                        ps.setInt(8, 0);
                        ps.setInt(9, -1);
                        ps.setInt(10, 10080);

                    for (int i = 0; i < size; i++) {
                        ps.setDouble(11 + i, (dfuData.getForecast()[i]) );
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getInsertSQL(int periodLength) {


       String sql = " INSERT INTO FCST ( DMDUNIT, DMDGROUP, LOC,  STARTDATE ,  TYPE  ,  FCSTID  ,  MODEL    ,  LEWMEANQTY   ,  MARKETMGRVERSIONID ,  DUR  ,  " ;
       StringBuilder periodStr = new StringBuilder("");
       StringBuilder values = new StringBuilder("VALUES ( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?, ");
        int i = 0;
                for( ; i < periodLength-1; i++) {
                    periodStr.append("PERIOD" + (i + 1) + ", ");
                    values.append("?, ");
                }
                periodStr.append("PERIOD" + (i + 1) + " )");
                values.append("? )");
        sql = sql + periodStr.toString() + values.toString();
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
