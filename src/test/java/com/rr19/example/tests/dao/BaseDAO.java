package com.rr19.example.tests.dao;

import javax.sql.DataSource;



import com.rr19.example.tests.PoolingDataSourceTestImpl;

public class BaseDAO {
    DataSource ds = null;
    public BaseDAO() {

        final String db = "jdbc:hsqldb:hsql://localhost/Test";
        final String user = "SA";
        final String password = "";
        this.ds = PoolingDataSourceTestImpl.setupDataSource(db, user, password);
    }
}