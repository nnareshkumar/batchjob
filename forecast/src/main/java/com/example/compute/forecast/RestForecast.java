
package com.example.compute.forecast;

import javax.batch.runtime.BatchRuntime;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;

import javax.batch.runtime.JobExecution;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Root resource (exposed at "RestForecast" path)
 */
@Path("forecast")
public class RestForecast {
    @Context
    private UriInfo context;

    private long execID;
    private JobOperator jobOperator;
    private final static Logger LOGGER = Logger.getLogger(RestForecast.class.getCanonicalName());


    /**
     * Creates a new instance of RestForecast
     */
    public RestForecast() {
    }

    /**
     * Retrieves representation of an instance of RestForecast.RestForecast
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("start")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String startBatchJob() {

        jobOperator = BatchRuntime.getJobOperator();
        execID = jobOperator.start("forecast", null);

        return  "started";
    }

    @GET
    @Path("status")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getJobStatusStr() {

        jobOperator = BatchRuntime.getJobOperator();
        List<Long> jobs = jobOperator.getRunningExecutions("forecast");
        String statusStr = jobOperator.getJobExecution(jobs.get(0)).getBatchStatus().name();

        return statusStr;
    }

    @GET
    @Path("report")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<String> getReport() {

        List<String> list = new ArrayList<>();
        try {
            list.add(showResults(false));
        } catch (IOException e) {
            e.printStackTrace();
            list.add("Exception in the while processing");
        }
        return list;
    }

    public String getJobStatus() {
        jobOperator = BatchRuntime.getJobOperator();
        return jobOperator.getJobExecution(execID).getBatchStatus().toString();
    }

    public boolean isCompleted() {
        return (getJobStatus().compareTo("COMPLETED") == 0);
    }

    /* Show the results */
    public String showResults(boolean isData) throws IOException {

        String currentLine = "", resultStr = "";
        try (BufferedReader breader = new BufferedReader(new FileReader(filePathName()))) {
            while( (currentLine = breader.readLine())!= null) {
                if(!currentLine.startsWith(" {            \"chart\": { ")) {
                    resultStr = resultStr + currentLine;
                }
            }
        }
        return resultStr;
    }

    @GET
    @Path("data")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getResults() {

        String results = new String();
        try {
            results = showResults(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    @GET
    @Path("graph")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String getGraphResults() {

        String currentLine = "", resultStr ="";
        try( BufferedReader breader = new BufferedReader(new FileReader(filePathName()))){
            while( (currentLine = breader.readLine())!= null) {
                if (currentLine.startsWith(" {            \"chart\": { ")) {
                    resultStr = resultStr + currentLine;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  resultStr;
    }

    public String filePathName() {

        String filePath = "C:/Users/j1008526/Desktop/template/";//"/home/buranpur_laxman/temp/";//"/home/buranpur_laxman/temp/";//"C:/Users/j1008526/Desktop/template/";
        filePath = filePath + "forecast.csv";

        LOGGER.log(Level.SEVERE, "File {0} is being used for Restful WebService from path {1}",   new Object[]{"forecast.csv", filePath});
        return filePath;
    }


    public String prepareHtml() {
        String str =
        "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Forecasting Algorithm</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/default.css\" />\n" +
                "    <script type=\"text/javascript\" src=\"scripts/fusioncharts.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "\n" +
                "    FusionCharts.ready(function(){\n" +
                "    var fusioncharts = new FusionCharts({\n" +
                "    type: 'msline',\n" +
                "    renderAt: 'chart-container',\n" +
                "    width: '1500',\n" +
                "    height: '700',\n" +
                "    dataFormat: 'json',\n" +
                "    dataSource: {";


             return str;
    }

    public String prepareFooter() {
        String str = " });\n" +
                "    fusioncharts.render();\n" +
                "    });\n" +
                "\n" +
                "  </script>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"chart-container\">FusionCharts XT will load here!</div>\n" +
                "<h1>Forecasting graph</h1>\n" +
                "<table>\n" +
                " <tr>\n" +
                "        <td style=\"font-size:18pt;font-weight:bold;width:100px\"><input type=\"button\" value=\"Display Chart\" onClick=\"callRequest();\">  </td>\n" +
                "        <td id=\"volume\" align=\"right\">--</td>\n" +
                " </tr>" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        return str;
    }


}
