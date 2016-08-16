package com.example.compute.forecast;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by j1008526 on 5/14/2016.
 */
public class RecordCheckPoint implements Serializable {

        private int itemNumber;
        private int numOfDfus;
        private long startTime;


        public RecordCheckPoint() {
            itemNumber = 1;
        }

        public RecordCheckPoint(int numItems) {
            this();
            this.numOfDfus = numItems;
        }

        public int getItemNumber() {
            return itemNumber;
        }

        public void setNumItems(int numItems) {
            this.numOfDfus = numItems;
        }

        public int getNumItems() {
            return numOfDfus;
        }

        public void nextItem() {
            itemNumber++;
        }

        public void setItemNumber(int item) {
            itemNumber = item;
        }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
