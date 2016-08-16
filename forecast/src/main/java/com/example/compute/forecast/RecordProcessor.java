
package com.example.compute.forecast;


import java.io.File;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import com.example.compute.forecast.DFUData;

/* Processor batch artifact.
 * Calculate the price of every call.
 */
@Dependent
@Named("RecordProcessor")
public class RecordProcessor implements ItemProcessor {

    @Inject
    JobContext jobCtx;

    public RecordProcessor() { }

    @Override
    public Object processItem(Object obj) throws Exception {

        List<DFUData> dfuDatas = (List) obj;
        if (dfuDatas != null && dfuDatas.size() > 0) {

            Iterator iter = dfuDatas.iterator();
            while (iter.hasNext()) {
                DFUData dfuData = (DFUData) iter.next();

               double[] baseHistory = dfuData.getBaseHistory();
               //main algorithm code is removed and a sample of taking the average of the data is used.
                double average = 0.0;
                for(int i = 0; i < baseHistory.length; i++) {
                   average = average + baseHistory[i];
                }
                average = average/baseHistory.length;
                double[] forecast = new double[baseHistory.length];
                for(int i = 0; i < baseHistory.length; i++) {
                    forecast[i] = baseHistory[i] * (baseHistory[i]/average);
                }

                dfuData.setForecast(forecast);
            }

        }
        return dfuDatas;

    }



}
