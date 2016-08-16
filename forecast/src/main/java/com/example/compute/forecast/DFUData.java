// ==========================================================================
//                     Copyright 1995-2005, Manugistics, Inc.
//                             All Rights Reserved
//
//                THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
//                              MANUGISTICS, INC.
//
//
//      The copyright notice above does not evidence any actual
//      or intended publication of such source code.
//
// ==========================================================================

package com.example.compute.forecast;

import com.manu.maeintegration.publicapi.forecast.mlr.LinRegCoefficientIF;

import java.io.Serializable;

public class DFUData implements Serializable {

    public static String KEY_DELIMITER = "?";
    
    private String histStream;
    private String demandCalendar;
    private long histStartDate;
    private long dmdPostDate;
    private String seasonProfile;
    private int algorithm;
    private boolean isNPI;
    
    // stores flag to indicate if the batch has DFU(s) of these types
    private boolean noneDFU;
    private boolean fourierDFU;
    private boolean mlrDFU;
    private boolean genFcstDFU;
    private boolean lewandowskiDFU;
    private boolean holtWintersDFU;
    private boolean crosstonsDFU;
    private boolean movingavgDFU;
    private boolean avsGravesDFU;
    private boolean slcDFU;

    private String dmdUnit;
    private String loc;
    private String dmdGroup;
    private String model;

    private String dfuHistStreamKey;
    private double mean;
    
    private double[] baseHistory;
    private boolean processError;
    private double[] deSeasonalanddeTrendHistory;
    private double level;
    private double trend;
    private int seasonalValue;
    private boolean seasonal;
    private double[] normalizedSeasonalAdjusters;
    private LinRegCoefficientIF[] dfuCoefs;
    private double[] periodWeight;
    private int noOfHistPeriods;
    private boolean isMovEventDfu;
    private double[] forecast;
    
    /**
     *
     * @param dmdUnit
     * @param dmdGroup
     * @param loc
     * @param model
     * @param histStream
     */
    public DFUData(String dmdUnit, String dmdGroup, String loc, String model, String histStream) {

        this.histStream = histStream;
        this.dmdUnit = dmdUnit;
        this.loc = loc;
        this.dmdGroup = dmdGroup;
        this.model = model;
        this.dfuHistStreamKey = dmdUnit + KEY_DELIMITER + dmdGroup + KEY_DELIMITER + loc + KEY_DELIMITER + histStream;
    }

    public String getDmdUnit() {
        return dmdUnit;
    }

    public void setDmdUnit(String dmdUnit) {
        this.dmdUnit = dmdUnit;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getDmdGroup() {
        return dmdGroup;
    }

    public void setDmdGroup(String dmdGroup) {
        this.dmdGroup = dmdGroup;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHistStream() {
		return histStream;
	}

	public void setHistStream(String histStream) {
		this.histStream = histStream;
	}

	public String getDemandCalendar() {
		return demandCalendar;
	}

	public void setDemandCalendar(String demandCalendar) {
		this.demandCalendar = demandCalendar;
	}

	public long getHistStartDate() {
		return histStartDate;
	}

	public void setHistStartDate(long histStartDate) {
		this.histStartDate = histStartDate;
	}

	public long getDmdPostDate() {
		return dmdPostDate;
	}

	public void setDmdPostDate(long dmdPostDate) {
		this.dmdPostDate = dmdPostDate;
	}

	public String getSeasonProfile() {
		return seasonProfile;
	}

	public void setSeasonProfile(String seasonProfile) {
		this.seasonProfile = seasonProfile;
	}

	public int getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
	}

	public boolean isNoneDFU() {
		return noneDFU;
	}

	public void setNoneDFU(boolean noneDFU) {
		this.noneDFU = noneDFU;
	}

	public boolean isFourierDFU() {
		return fourierDFU;
	}

	public void setFourierDFU(boolean fourierDFU) {
		this.fourierDFU = fourierDFU;
	}

	public boolean isMlrDFU() {
		return mlrDFU;
	}

	public void setMlrDFU(boolean mlrDFU) {
		this.mlrDFU = mlrDFU;
	}

	public boolean isGenFcstDFU() {
		return genFcstDFU;
	}

	public void setGenFcstDFU(boolean genFcstDFU) {
		this.genFcstDFU = genFcstDFU;
	}

	public boolean isLewandowskiDFU() {
		return lewandowskiDFU;
	}

	public void setLewandowskiDFU(boolean lewandowskiDFU) {
		this.lewandowskiDFU = lewandowskiDFU;
	}

	public boolean isHoltWintersDFU() {
		return holtWintersDFU;
	}

	public void setHoltWintersDFU(boolean holtWintersDFU) {
		this.holtWintersDFU = holtWintersDFU;
	}

	public boolean isCrosstonsDFU() {
		return crosstonsDFU;
	}

	public void setCrosstonsDFU(boolean crosstonsDFU) {
		this.crosstonsDFU = crosstonsDFU;
	}

	public boolean isMovingavgDFU() {
		return movingavgDFU;
	}

	public void setMovingavgDFU(boolean movingavgDFU) {
		this.movingavgDFU = movingavgDFU;
	}

	public boolean isAvsGravesDFU() {
		return avsGravesDFU;
	}

	public void setAvsGravesDFU(boolean avsGravesDFU) {
		this.avsGravesDFU = avsGravesDFU;
	}

	public boolean isSlcDFU() {
		return slcDFU;
	}

	public void setSlcDFU(boolean slcDFU) {
		this.slcDFU = slcDFU;
	}

	public String getDfuHistStreamKey() {
		return dfuHistStreamKey;
	}

	public void setDfuHistStreamKey(String dfuHistStreamKey) {
		this.dfuHistStreamKey = dfuHistStreamKey;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double[] getBaseHistory() {
		return baseHistory;
	}

	public void setBaseHistory(double[] baseHistory) {
		this.baseHistory = baseHistory;
	}

	public boolean isProcessError() {
		return processError;
	}

	public void setProcessError(boolean processError) {
		this.processError = processError;
	}

	public double[] getDeSeasonalanddeTrendHistory() {
		return deSeasonalanddeTrendHistory;
	}

	public void setDeSeasonalanddeTrendHistory(double[] deSeasonalanddeTrendHistory) {
		this.deSeasonalanddeTrendHistory = deSeasonalanddeTrendHistory;
	}

	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}

	public double getTrend() {
		return trend;
	}

	public void setTrend(double trend) {
		this.trend = trend;
	}


	public int getSeasonalValue() {
		return seasonalValue;
	}

	public void setSeasonalValue(int seasonalValue) {
		this.seasonalValue = seasonalValue;
	}

	public boolean isSeasonal() {
		return seasonal;
	}

	public void setSeasonal(boolean seasonal) {
		this.seasonal = seasonal;
	}

	public double[] getNormalizedSeasonalAdjusters() {
		return normalizedSeasonalAdjusters;
	}

	public void setNormalizedSeasonalAdjusters(double[] normalizedSeasonalAdjusters) {
		this.normalizedSeasonalAdjusters = normalizedSeasonalAdjusters;
	}

	public LinRegCoefficientIF[] getDfuCoefs() {
		return dfuCoefs;
	}

	public void setDfuCoefs(LinRegCoefficientIF[] dfuCoefs) {
		this.dfuCoefs = dfuCoefs;
	}

	public double[] getPeriodWeight() {
		return periodWeight;
	}

	public void setPeriodWeight(double[] periodWeight) {
		this.periodWeight = periodWeight;
	}

	public int getNoOfHistPeriods() {
		return noOfHistPeriods;
	}

	public void setNoOfHistPeriods(int noOfHistPeriods) {
		this.noOfHistPeriods = noOfHistPeriods;
	}

	public boolean isMovEventDfu() {
		return isMovEventDfu;
	}

	public void setMovEventDfu(boolean isMovEventDfu) {
		this.isMovEventDfu = isMovEventDfu;
	}

	/**
	 * @return the isNPI
	 */
	public boolean isNPI() {
		return isNPI;
	}

	/**
	 * @param isNPI the isNPI to set
	 */
	public void setNPI(boolean isNPI) {
		this.isNPI = isNPI;
	}

    public double[] getForecast() {
        return forecast;
    }

    public void setForecast(double[] forecast) {
        this.forecast = forecast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFUData dfuData = (DFUData) o;

        if (!dmdUnit.equals(dfuData.dmdUnit)) return false;
        if (!loc.equals(dfuData.loc)) return false;
        if (!dmdGroup.equals(dfuData.dmdGroup)) return false;
        if (!model.equals(dfuData.model)) return false;
        return dfuHistStreamKey.equals(dfuData.dfuHistStreamKey);

    }

    @Override
    public int hashCode() {
        int result = dmdUnit.hashCode();
        result = 31 * result + loc.hashCode();
        result = 31 * result + dmdGroup.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + dfuHistStreamKey.hashCode();
        return result;
    }
}
/*
 * $Log$
 * Revision 1.1  2013/10/07 06:57:55  nkasavajula
 * Base Version
 *
 * Revision 1.1  2013/10/04 08:54:28  nkasavajula
 * Base Version for RCA Process
*/