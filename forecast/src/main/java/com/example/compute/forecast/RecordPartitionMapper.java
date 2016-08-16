/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package com.example.compute.forecast;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.example.compute.forecast.DBUtils;

/* Partition mapper artifact.
 * Determines the number of partitions (2) for the forecast processing step
 * and the range of forecast each partition should work on.
 */
@Dependent
@Named("RecordPartitionMapper")
public class RecordPartitionMapper implements PartitionMapper {

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        /* Create a new partition plan */
        return  new PartitionPlanImpl() {


    /* Auxiliary method - get the number of bills */
            public long getBillCount(Connection con) {


                PreparedStatement preparedStatement = null;
                ResultSet rs = null;
                int count = 1;

                try {

                    preparedStatement = con.prepareStatement("SELECT COUNT(*) FROM HIST");
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        count = rs.getInt(1);
                    }
                    return count;
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
                return count;
            }

    /* The number of partitions could be dynamically calculated based on
     * many parameters. In this particular example, we are setting it to
     * a fixed value for simplicity.
     */
            @Override
            public int getPartitions () {
                return 2;
            }

    /* Obtaint the parameters for each partition. In this case,
     * the parameters represent the range of items each partition
     * of the step should work on.
     */
            @Override
            public Properties[] getPartitionProperties () {

                DBUtils dbUtils = new DBUtils();
                Connection con = dbUtils.getConnection();

                /* Assign an (approximately) equal number of elements
                 * to each partition. */
                long totalItems = getBillCount(con);
                long partItems = (long) totalItems / getPartitions();
                long remItems = totalItems % getPartitions();

                /* Populate a Properties array. Each Properties element
                 * in the array corresponds to a partition. */
                Properties[] props = new Properties[getPartitions()];

                for (int i = 0; i < getPartitions(); i++) {
                    props[i] = new Properties();
                    props[i].put("firstItem", i * partItems);
                    /* Last partition gets the remainder elements */
                    if (i == getPartitions() - 1) {
                        props[i].put("numItems", partItems + remItems);
                    } else {
                        props[i].put("numItems", partItems);
                    }
                }
                return props;
            }


        };

    }



}
