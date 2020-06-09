package com.rr19.example.tests.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rr19.example.tests.dataTypes.Customer;

public class CustomerDAO extends BaseDAO {;

    /**
     * 
     * @param firstName
     * @param lastName
     * @return a unique customer Id
     * @throws SQLException
     */
    public Integer getCustomerIdByName(final String firstName, final String lastName) throws SQLException {
        final Customer customer = new Customer();
        final Connection con = ds.getConnection();
        final String preparedString = String.join("\n", 
        "SELECT CUSTOMER.ID from CUSTOMER",
        "WHERE CUSTOMER.LASTNAME = ?",
        "AND CUSTOMER.FIRSTNAME = ?");
        final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, lastName);
        ps.setString(2, firstName);
        final ResultSet rs = ps.executeQuery();
        while(rs.next()){
            
            customer.setId(rs.getInt("CUSTOMER.ID"));
                       
        }
        return customer.getId();
        
    }


    public CustomerDAO() {
        super();
    }
}