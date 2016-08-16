
package com.example.compute.forecast;

import java.io.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.compute.forecast.DFUData;
import com.example.compute.forecast.DBUtils;

import javax.batch.api.chunk.ItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/* Writer batch artifact.
 * Add every call to a bill entity.
 */
@Dependent
@Named("RecordWriter")
public class RecordWriter implements ItemWriter {

    private final static Logger LOGGER = Logger.getLogger(RecordWriter.class.getCanonicalName());

    private Connection con;

    public RecordWriter() {}
    private String startTime="0";
    private String fileName;

    @Inject
    JobContext jobCtx;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        Properties properties = jobCtx.getProperties();
        startTime = properties.getProperty("start_time");
        fileName = properties.getProperty("results_file_name");
    }

    @Override
    public void close() throws Exception { }

    @Override
    public void writeItems(List<Object> dfus) throws Exception {

        ArrayList dfusData = (ArrayList) dfus;

        if(dfusData != null && dfusData.size() > 0) {

            insertRecordsDB(dfusData);
            long startTiming = Long.parseLong(startTime);
            long totalTime = (System.currentTimeMillis() - startTiming);

            String noOfDfus = " Number of DFUs processed:" + ((ArrayList) dfusData).size();
            String timeTaken = " Total time taken to complete the batch process:" + (totalTime / 1000l) + " in secs , in msecs: " + totalTime;

            String filePath = "C:/Users/j1008526/Desktop/template/";//"/home/buranpur_laxman/temp/";//"C:/Users/j1008526/Desktop/template/";
            filePath = filePath + "forecast.csv";//"/home/buranpur_laxman/temp/results.csv";

            LOGGER.log(Level.SEVERE, "File {0} being uploaded to {1}", new Object[]{fileName, filePath});


            try (BufferedWriter bwriter = new BufferedWriter(new FileWriter(filePath))) {


                //output to be written to csv file
                boolean firstDfu = false;
                Iterator iter = dfusData.iterator();
                while (iter.hasNext()) {

                    List<DFUData> dfuData = (ArrayList) iter.next();
                    if (!firstDfu)
                        firstDfu = true;

                    for (int dfuDataIndex = 0; dfuDataIndex < dfuData.size(); dfuDataIndex++) {
                        DFUData dfu = (DFUData) dfuData.get(dfuDataIndex);
                        bwriter.write(dfu.getDmdUnit() + "," + dfu.getLoc() + "," + dfu.getDmdGroup() + "," + dfu.getModel());

                        double history[] = dfu.getBaseHistory();
                        for (int i = 0; i < history.length; i++) {
                            bwriter.write("," + history[i]);
                        }

                        double forecast[] = dfu.getForecast();
                        for (int i = 0; i < forecast.length; i++) {
                            bwriter.write("," + forecast[i]);
                        }
                        bwriter.write("\n");
                    }
                }

                String dataSource = writeJson(dfusData);
                bwriter.write(dataSource);
                bwriter.write("\n");
                bwriter.write(noOfDfus);
                bwriter.write("\n");
                bwriter.write(timeTaken);
                bwriter.write("\n");


            }
        }
   }

    public void insertRecordsDB(ArrayList dfusData) {

        DBUtils dbUtils = new DBUtils();
        con = dbUtils.getConnection();
        dbUtils.insertRecords(con, dfusData);
    }

    public String writeJson(ArrayList dfusData) {

        String datSource =  " {" +
                "            \"chart\": {" +
                "                \"caption\": \"Forecast generation\"," +
                "                \"captionFontSize\": \"14\"," +
                "                \"subcaptionFontSize\": \"14\"," +
                "                \"subcaptionFontBold\": \"0\"," +
                "                \"paletteColors\": \"#0075c2,#1aaf5d\"," +
                "                \"bgcolor\": \"#ffffff\"," +
                "                \"showBorder\": \"0\"," +
                "                \"showShadow\": \"0\"," +
                "                \"showCanvasBorder\": \"0\"," +
                "                \"usePlotGradientColor\": \"0\"," +
                "                \"legendBorderAlpha\": \"0\"," +
                "                \"legendShadow\": \"0\"," +
                "                \"showAxisLines\": \"0\"," +
                "                \"showAlternateHGridColor\": \"0\"," +
                "                \"divlineThickness\": \"1\"," +
                "                \"divLineIsDashed\": \"1\"," +
                "                \"divLineDashLen\": \"1\"," +
                "                \"divLineGapLen\": \"1\"," +
                "                \"xAxisName\": \"Weekly\"," +
                "                \"showValues\": \"0\"" +
                "            }," +
                "            \"categories\": [" +
                "                {";

        String timeSeries = "\"category\": [";String seriesName = "";

        boolean firstDfu = false;
        Iterator iter  = dfusData.iterator();
        while(iter.hasNext()) {

            List<DFUData> dfuData = (ArrayList) iter.next();
            if(!firstDfu)
                firstDfu = true;

            for(int dfuDataIndex = 0; dfuDataIndex < dfuData.size(); dfuDataIndex++) {
                DFUData dfu = (DFUData)dfuData.get(dfuDataIndex);
                seriesName = seriesName + ((dfuDataIndex == 0 || dfuDataIndex == dfuData.size())? " " : ", " ) + getSeries(dfu);
                if(firstDfu && dfuDataIndex == 0)
                    timeSeries = timeSeries + getTimeSeries(dfu);
            }
        }

      datSource = datSource+ timeSeries +
                "                }" +
                "            ]," +
                "            \"dataset\": [" +
                seriesName +
                "            ]," +
                "            \"trendlines\": [" +
                "                {" +
                "                    \"line\": [" +
                "                        {" +
                "                            \"startvalue\": \"17022\"," +
                "                            \"color\": \"#6baa01\"," +
                "                            \"valueOnRight\": \"1\"," +
                "                            \"displayvalue\": \"Average\"" +
                "                        }" +
                "                    ]" +
                "                }" +
                "            ]" +
                "        }";


    return datSource;

    }

    private String getTimeSeries(DFUData dfu) {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = new Date(dfu.getHistStartDate());
        String date = format.format(startDate);
        String timeSeries  = "  { \"label\": \"" + date + "\" }," ;
        TreeSet<Date> dates = new TreeSet();
        //TreeSet<Date> futureDates = new TreeSet();

        int i=0;
        for(; i < dfu.getBaseHistory().length-1; i=i+10) {
            dates.add(new Date(dfu.getHistStartDate() + (10080L * 60 * 1000 * (i+1))));
        }

        for(Iterator iter = dates.iterator() ; iter.hasNext(); ) {
            timeSeries = timeSeries +  " { \"label\": \"" + format.format ((Date) iter.next()) + "\" }," ;
        }

        Date lastHistoryDate = new Date(dfu.getHistStartDate() + (10080L * 60 * 1000 * (i+1)));
        dates.add(lastHistoryDate);
        long lastHistDate =  ((Date)lastHistoryDate).getTime();
        timeSeries = timeSeries +  "{ \"label\": \"" + format.format ( new Date(lastHistDate))+ "\" }," ;

        //demand post date
        timeSeries = timeSeries+
                "                        {" +
                "                            \"vline\": \"true\"," +
                "                            \"lineposition\": \"0\"," +
                "                            \"color\": \"#6baa01\"," +
                "                            \"labelHAlign\": \"center\"," +
                "                            \"labelPosition\": \"0\"," +
                "                            \"label\": \"Demand Post Date\"," +
                "                            \"dashed\":\"1\"" +
                "                        }," ;

                //future periods
        double[] forecast = dfu.getForecast();
        Date futureDate = null;
        for( int j=0 ; j < forecast.length; j++) {
            futureDate = new Date(lastHistDate + (10080L * 60 * 1000 * (i+j+1)));
            dates.add(futureDate);
            timeSeries = timeSeries +  " { \"label\": \"" + format.format(futureDate)+ "\" }," ;
        }

        timeSeries = timeSeries +  " { \"label\": \"" + format.format (futureDate )+ "\" }" + "  ]";

        return timeSeries;
    }

    private String getSeries(DFUData dfu) {

        String seriesName = "{" +
                "                    \"seriesname\": \" " + dfu.getDmdUnit()+ dfu.getLoc()+dfu.getDmdGroup()+dfu.getModel()  + "\"," +
                "                    \"data\": [" ;

                double[] history = dfu.getBaseHistory();
                String fStr = "";
                for(int i = 0; i < history.length; i++) {
                    fStr = fStr +  " { \"value\": \""+ history[i] +"\" }," ;
                }

                double[] forecast = dfu.getForecast();
                //fStr = "";
                for(int i = 0; i < forecast.length-1; i++) {
                  fStr = fStr +  " { \"value\": \""+ forecast[i] +"\" }," ;
                }
                fStr = fStr + "{ \"value\": \"" + forecast[forecast.length-1] +"\" }" ;

                seriesName = seriesName + fStr +
                        "                    ]" +
                "                }";
        return seriesName;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }


}
