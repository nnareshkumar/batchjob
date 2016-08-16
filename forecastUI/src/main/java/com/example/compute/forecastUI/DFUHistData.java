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

package com.example.compute.forecastUI;


import java.io.Serializable;

public class DFUHistData implements Serializable {

    public static String KEY_DELIMITER = "?";
    
    private String histStream;
    private String demandCalendar;
    private long histStartDate;
    private long dmdPostDate;
    private int algorithm;
    private boolean isNPI;
    

    private String dmdUnit;
    private String loc;
    private String dmdGroup;
    private String model;

    private String dfuHistStreamKey;
    private double mean;
    
    private double[] baseHistory;
    private double[] forecast;

    public DFUHistData() {

    }

    /**
     *
     * @param dmdUnit
     * @param dmdGroup
     * @param loc
     * @param model
     * @param histStream
     */
    public DFUHistData(String dmdUnit, String dmdGroup, String loc, String model, String histStream) {

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

	public double[] getBaseHistory() {
		return baseHistory;
	}

	public void setBaseHistory(double[] baseHistory) {
		this.baseHistory = baseHistory;
	}

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

        DFUHistData DFUHistData = (DFUHistData) o;

        if (!dmdUnit.equals(DFUHistData.dmdUnit)) return false;
        if (!loc.equals(DFUHistData.loc)) return false;
        if (!dmdGroup.equals(DFUHistData.dmdGroup)) return false;
        if (!model.equals(DFUHistData.model)) return false;
        return dfuHistStreamKey.equals(DFUHistData.dfuHistStreamKey);

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
