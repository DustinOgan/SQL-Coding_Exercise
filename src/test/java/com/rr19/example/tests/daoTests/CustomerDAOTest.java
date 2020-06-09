package com.rr19.example.tests.daoTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import com.rr19.example.tests.BaseDbTest;
import com.rr19.example.tests.dao.CustomerDAO;

import org.junit.jupiter.api.Test;

public class CustomerDAOTest extends BaseDbTest {
    @Test
    public void testGetCustomerIdByName() {
        final Integer expectedCustomerId = 49;

        try {
            final CustomerDAO customerDAO = new CustomerDAO();
            final Integer customerId = customerDAO.getCustomerIdByName("Susanne", "Smith");
            assertEquals(customerId, expectedCustomerId);
        } catch (final SQLException e) {
            e.printStackTrace();
        }

    }
}