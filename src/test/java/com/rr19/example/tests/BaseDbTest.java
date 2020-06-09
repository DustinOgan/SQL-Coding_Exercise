package com.rr19.example.tests;

import javax.sql.DataSource;

import org.testng.annotations.BeforeSuite;

public class BaseDbTest {
    protected DataSource ds;
    @BeforeSuite
    public DataSource getDataSource() {

        final String db = "jdbc:hsqldb:hsql://localhost/Test";
        final String user = "SA";
        final String password = "";
        return PoolingDataSourceTestImpl.setupDataSource(db, user, password);
    }
}