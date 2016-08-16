/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package com.example.compute.forecast;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.api.chunk.ItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.compute.forecast.RecordCheckPoint;
import com.example.compute.forecast.DBUtils;

/* Reader batch artifact.
 * Reads call records from the input log file.
 */
@Dependent
@Named("RecordReader")
public class RecordReader implements ItemReader {

    private String logFileName;
    private BufferedReader breader;
    private RecordCheckPoint checkPoint;
    private Properties partParams;

    public Connection conn = null;
    private String startTime;

    @Inject
    JobContext jobCtx;

    public RecordReader() { }

    @Override
    public void open(Serializable ckpt) throws Exception {


                /* Get the parameters for this partition */
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long execID = jobCtx.getExecutionId();
//        partParams = jobOperator.getParameters(execID);
//
//        /* Get the range of items to work on in this partition */
        int firstItem0 = 1;//((Integer) partParams.get("firstItem")).intValue();
        int numItems0 = 10;//((Integer) partParams.get("numItems")).intValue();

        if (ckpt == null) {
            /* Create a checkpoint object for this partition */
            checkPoint = new RecordCheckPoint();
            checkPoint.setItemNumber(firstItem0);
            checkPoint.setNumItems(numItems0);
        } else {
            checkPoint = (RecordCheckPoint) ckpt;
        }

        /* Adjust range for this partition from the checkpoint */
        int firstItem = checkPoint.getItemNumber();
        int numItems = numItems0 - (firstItem - firstItem0);



        Properties properties = jobCtx.getProperties();
        startTime = properties.getProperty("start_time");
//        logFileName = jobCtx.getProperties().getProperty("log_file_name");

        properties.setProperty("start_time", String.valueOf(System.currentTimeMillis()));
//        breader = new BufferedReader(new FileReader(logFileName));
//        for (int i=0; i<checkPoint.getItemNumber(); i++)
//            breader.readLine();

        DBUtils dbUtils = new DBUtils();
        conn = dbUtils.getConnection();
        

    }

    @Override
    public void close() throws Exception {
        DBUtils dbUtils = new DBUtils();
        if(conn != null)
            dbUtils.closeConnection(conn);

    }

    @Override
    public Object readItem() throws Exception {

        DBUtils dbUtils = new DBUtils();

        if(checkPoint.getItemNumber() < 5) {

            List<DFUData> dfusData = dbUtils.fetchDFUDetails(conn, checkPoint.getItemNumber(), checkPoint.getNumItems());
            this.checkPoint.nextItem();
            return dfusData;

        } else {
            return null;
        }

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return checkPoint;
    }

}
