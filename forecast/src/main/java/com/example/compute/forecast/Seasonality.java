// ==========================================================================
//                     Copyright 1995-2003, Manugistics, Inc.
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

/**
 * Created by IntelliJ IDEA.
 * User: cluff
 * Date: Jul 14, 2003
 * Time: 6:27:55 PM
 * To change this template use Options | File Templates.
 */
package com.example.compute.forecast;

import com.manu.maeintegration.publicapi.forecast.common.SeasonalityIF;
import com.example.compute.forecast.HoltWintersParam;

import java.util.*;


public class Seasonality implements SeasonalityIF, Cloneable
{

    private double[] inputValues, normalizedValues, optimizedValues,originalInputValues;
    private int seasonType = 1;
    /*Moving Holidays*/
    private int periodicity;
    private int forward = 1,backward = 2,noshift = 3, eventDirection = forward,shiftYearDirection = 1;
    private int posOfActualProf = 1, noOfPrdsSpanned = 1, profileYear = 1970, posOfShiftedProf=1;
    private int firstEventPrd = 1, LastEventPrd = 1;
    private double[] shiftedNormalValues;
    double[] shiftedVals;
    private TreeMap seasonalYears;
    private TreeMap yearwiseNormalValues;
    private HashMap basePrd;
    private double[] deseasonalizedHist;
    private int intIndex;
    private boolean isMovHolEffected = false;
    private int seasonalityOpt=1;
    private HashMap noOfPeriodsSpanned;
    /*Moving Holidays*/
    /**
     * Constructor
     */
    public Seasonality(int periodicity) {
        initialize(periodicity, HoltWintersParam.SEASONALITYOPT_MULT);
    }

    public Seasonality(int periodicity, int seasonalityType) {
        initialize(periodicity, seasonalityType);
    }

    private void initialize(int periodicity, int seasonalityType) {
        this.inputValues = new double[periodicity];
        this.normalizedValues = new double[periodicity];
        this.optimizedValues = new double[periodicity];
        this.originalInputValues=new double[periodicity]; 
        this.seasonType = seasonalityType;
        /*VKota - MH*/
        this.periodicity = periodicity;
        shiftedVals = new double[this.periodicity];
        this.basePrd = new HashMap();
        this.noOfPeriodsSpanned = new HashMap();
        /*VKota - MH*/

        double defaultInput = 1.0;
        if (seasonalityType == HoltWintersParam.SEASONALITYOPT_ADD) {
            defaultInput = 0.0;
        }

        for (int i=0; i<periodicity; i++)
        {
            this.inputValues[i] = defaultInput;
            this.normalizedValues[i] = 1.0;
            this.optimizedValues[i] = 0.0;
        }
    }
    public double[] getInputValues() {
        return inputValues;
    }

    public void setInputValues(double[] inputValues) {
        this.inputValues = inputValues;
    }
    /*Start of Moving Holidays methods*/
    public int getSeasonalityOpt() {
        return seasonalityOpt;
    }

    public void setSeasonalityOpt(int seasonalityOpt) {
        this.seasonalityOpt = seasonalityOpt;
    }

    public int getIntIndex() {
           return this.intIndex;
       }

       public void setIntIndex(int intIndex) {
           this.intIndex = intIndex;
       }


      public double[] getDeseasonalizedHist() {
        return this.deseasonalizedHist;
    }



    public double[] getShiftedNormalValues()
    {
        return this.shiftedNormalValues;
    }
    public void setShiftedNormalValues(double[] normalizedAjusters)
    {
        this.shiftedNormalValues = normalizedAjusters;
    }
    public void setProfileYear(int yearOfProfile)
    {
        this.profileYear = yearOfProfile;
    }
     public int getProfileYear()
    {
        return this.profileYear;
    }


   public boolean isMovHolEffected() {
        return this.isMovHolEffected;
    }

    public void setMovHolEffected(boolean movHolEffected) {
        this.isMovHolEffected = movHolEffected;
    }
    /*End of Moving Holidays methods*/
    public double[] getNormalizedValues() {
        return this.normalizedValues;
    }

    public void setNormalizedValues(double[] normalizedValues) {
        this.normalizedValues = normalizedValues;
   }

    public double[] getOptimizedValues() {
        return this.optimizedValues;
    }

    public void setOptimizedValues(double[] optimizedValues) {
        this.optimizedValues = optimizedValues;
    }
    //this will be used once optimized vals calculated
    public void setYearwiseOptimizedValues(TreeMap optimizedValues) {
        this.yearwiseNormalValues = optimizedValues;
    }
    public TreeMap getYearwiseOptimizedValues()
    {
        return this.yearwiseNormalValues;
    }

    public void setValuesForPeriod(int period, double inputValue, double normalizedValue, double optimizedValue) {
        inputValues[period-1]=inputValue;
        normalizedValues[period-1]=normalizedValue;
        optimizedValues[period-1]=optimizedValue;
        originalInputValues[period-1]=inputValue;
    }

    public double[] getOriginalInputValues() {
           return originalInputValues;
       }

       public void setOriginalInputValues(double[] originalInputValues) {
           this.originalInputValues = originalInputValues;
       }


    /** normalize() - Each seasonality record has an input value   */
    /*                           and a normalized value.  The user enters     */
    /*                           the input value, and we calculate the        */
    /*                           normalized values here.  The sum of the      */
    /*                           normalized values must equal the periodicity,*/
    /*                           and each normalized value must have the same */
    /*                           ratio its corresponding input value.         */
    public void normalize()
    {
        double seasonTotal = 0.0;
        for (int i=0; i<this.inputValues.length; i++) {
            seasonTotal += this.inputValues[i];
        }

        switch(seasonType)
        {
            case 1:
                for(int i=0;i<this.inputValues.length;i++)
                    this.normalizedValues[i] = (this.inputValues.length /( seasonTotal!=0 ? seasonTotal : 1))* this.inputValues[i];
                break;
            case 2:
                for(int i=0;i<this.inputValues.length;i++)
                    this.normalizedValues[i] =  this.inputValues[i] - (seasonTotal / this.inputValues.length) ;
                break;
        }
    }



    public int getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(int seasonType) {
        this.seasonType = seasonType;
    }
    public Seasonality createCopy() {
        try {
            Seasonality newSeason=(Seasonality)this.clone();
            if(this.inputValues!=null)
                System.arraycopy(this.inputValues, 0, newSeason.getInputValues(), 0, this.inputValues.length);
            if(this.optimizedValues!=null)
                System.arraycopy(this.optimizedValues, 0, newSeason.getOptimizedValues(), 0, this.optimizedValues.length);
            if(this.normalizedValues!=null)
                System.arraycopy(this.normalizedValues, 0, newSeason.getNormalizedValues(), 0, this.normalizedValues.length);
            if(this.originalInputValues!=null)
                System.arraycopy(this.originalInputValues, 0, newSeason.getOriginalInputValues(), 0, this.originalInputValues.length);
            if(this.shiftedNormalValues!=null)
                System.arraycopy(this.shiftedNormalValues, 0, newSeason.getShiftedNormalValues(), 0, this.shiftedNormalValues.length);
            if(this.deseasonalizedHist!=null)
                System.arraycopy(this.deseasonalizedHist, 0, newSeason.getDeseasonalizedHist(), 0, this.deseasonalizedHist.length);
            //TreeMap newYearwiseNormalValues;
            return newSeason;
        } catch (Exception e) {
            e.printStackTrace();
           return null;
        }
    }
}

