package com.rr19.example.tests.daoTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.rr19.example.tests.dao.ProductDAO;

import org.testng.annotations.Test;

public class ProductDAOTest {
    @Test
    public void testGetItemIdByProductNameAndPrice() {
        final Integer expectedItemId = 0;
        try {
            final ProductDAO productDAO = new ProductDAO();
            final Integer itemId = productDAO.getProductIdByProductNameAndPrice("Iron Clock", new BigDecimal("3.60"));
            assertEquals(itemId, expectedItemId);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
    
}