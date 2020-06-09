package com.rr19.example.tests.invoicePriceCalculatorTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.rr19.example.application.InvoicePriceCalculator;
import com.rr19.example.tests.BaseDbTest;
import com.rr19.example.tests.dao.InvoiceDAO;
import com.rr19.example.tests.dataTypes.Invoice;

import org.testng.annotations.BeforeSuite;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class InvoicePriceCalculatorTest extends BaseDbTest {
    @BeforeSuite
    public void init(){
        ds = super.getDataSource();
    }
        
    

    @DataProvider(name = "top10Customers")
    public String[] getCustomerIds() throws SQLException {
        String[] customerIds = new String[10];
        Connection con = ds.getConnection();
        final String preparedString = String.join("\n", "SELECT TOP 10 ID", "FROM CUSTOMER");
        final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        final ResultSet rs = ps.executeQuery();
        int index = 0;
        while (rs.next()) {
            customerIds[index] = rs.getString("ID");
            index++;
        }
        con.close();
        return customerIds;
    }

    @Test(dataProvider = "top10Customers")
    public void testInvoicePriceCalculatorWithExistingRows(String customerId) {
        final InvoicePriceCalculator invoicePriceCalculator = new InvoicePriceCalculator();
        try {
            final Double totalAmountOfInvoicesById = invoicePriceCalculator.totalAmountOfInvoicesById(customerId);
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            List<Invoice> invoices = invoiceDAO.selectInvoicesByCustomerId(new Integer(customerId));
            final BigDecimal invoiceTotal = invoices.stream().map(Invoice::getTotal).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            assertEquals(totalAmountOfInvoicesById.doubleValue(), invoiceTotal.doubleValue());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test 
    public void testInvoicePriceCalculatorWithExtraRow() throws Exception {
        //customer 0 has 1 invoice with id 49 for 3583.50
        //add a second row for $100 to this table to make sure the total updates
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        
        InvoicePriceCalculator invoicePriceCalculator = new InvoicePriceCalculator();
        assertEquals(invoicePriceCalculator.totalAmountOfInvoicesById("0"), 3583.50d);
        invoiceDAO.insert(51, 0, new BigDecimal("100.00"));
        assertEquals(invoicePriceCalculator.totalAmountOfInvoicesById("0"), 3683.50d);
        invoiceDAO.delete(51);
        assertEquals(invoicePriceCalculator.totalAmountOfInvoicesById("0"), 3583.50d); 
    }

    
    @Test 
    public void testCustomerWithNoInvoicesSumsToZero() throws Exception {
        //customer 37 Sylvia Sommer does not have any invoices
        InvoicePriceCalculator invoicePriceCalculator = new InvoicePriceCalculator();
        assertEquals(invoicePriceCalculator.totalAmountOfInvoicesById("37"), 0d);


    }
}