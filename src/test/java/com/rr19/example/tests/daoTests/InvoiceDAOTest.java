package com.rr19.example.tests.daoTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.rr19.example.tests.BaseDbTest;
import com.rr19.example.tests.dao.InvoiceDAO;
import com.rr19.example.tests.dataTypes.Invoice;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class InvoiceDAOTest extends BaseDbTest {
    @BeforeTest
    public void init(){
        ds = super.getDataSource();
    }
    @Test
    public void testInvoiceInsertWithRollBack() throws SQLException {
        Connection con;
            con = ds.getConnection();
            con.setAutoCommit(false);
            final InvoiceDAO invoiceDAO = new InvoiceDAO();
            invoiceDAO.insert(con, 50, 49, new BigDecimal("100.18"));
            final List<Invoice> invoices = invoiceDAO.selectByInvoiceId(con, 50);
            con.close();
            System.out.println(invoices.size());
            assertEquals(invoices.size(), 1);
            assertEquals(invoices.get(0).getId().intValue(), 50);
    };

    @Test
    public void testGetMaxInvoiceId(){
        final Integer expectedRecord = 49;
        try {
            final InvoiceDAO invoiceDAO = new InvoiceDAO();
            final Integer actualRecord = invoiceDAO.getMaxInvoiceNumber();
            System.out.println(actualRecord);
            assertEquals(actualRecord, expectedRecord);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertAndDeleteNoRollBack() throws SQLException {
        final InvoiceDAO invoiceDAO = new InvoiceDAO();
            invoiceDAO.insert(50, 49, new BigDecimal("100.18"));
            final List<Invoice> invoices = invoiceDAO.selectByInvoiceId(50);
            System.out.println(invoices.size());
            assertEquals(invoices.size(), 1);
            assertEquals(invoices.get(0).getId().intValue(), 50);
            invoiceDAO.delete(50);
            final List<Invoice> remainingInvoices = invoiceDAO.selectByInvoiceId(50);
            assertEquals(remainingInvoices.size(), 0);

    }
    
}