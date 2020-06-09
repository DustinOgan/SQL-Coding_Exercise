package com.rr19.example.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

import com.rr19.example.application.InvoicePriceCalculator;
import com.rr19.example.tests.dao.CustomerDAO;
import com.rr19.example.tests.dao.InvoiceDAO;
import com.rr19.example.tests.dao.ProductDAO;
import com.rr19.example.tests.dataTypes.Invoice;
import com.rr19.example.tests.dataTypes.InvoiceLineItem;
import com.rr19.example.tests.dataTypes.Item;
import com.rr19.example.tests.dataTypes.Product;
import com.rr19.example.tests.utils.InvoiceUtils;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UtilMethodTests {
    DataSource ds = null;

    @BeforeSuite
    public void getDataSource() {

        final String db = "jdbc:hsqldb:hsql://localhost/Test";
        final String user = "SA";
        final String password = "";
        ds = PoolingDataSourceTestImpl.setupDataSource(db, user, password);
    }

    @Test
    public void testLineItemsByInvoiceIdQuery() {
        final Integer customerId = 36;
        try {
            final List<InvoiceLineItem> lineItems = getLineItemsByInvoiceId(10);
            final Map<String, Integer> actualItems = new HashMap<String, Integer>();
            lineItems.forEach(item -> {
                assertEquals(item.getFirst(), "Janet");
                assertEquals(item.getLast(), "King");
                assertEquals(item.getCustomerId(), customerId);
                actualItems.put(item.getProductName(), item.getQuantity());
            });
            final Map<String, Integer> expectedItems = new HashMap<String, Integer>();
            expectedItems.put("Clock Clock", 24);
            expectedItems.put("Shoe Iron", 8);
            expectedItems.put("Telephone Chair", 4);
            expectedItems.put("Iron Telephone", 22);
            expectedItems.put("Shoe Ice Tea", 4);

            assertEquals(actualItems, expectedItems);

        } catch (final SQLException e) {

            e.printStackTrace();
        }
    }

    @Test
    public void testInsertInvoiceRecord() {
        try {

            // Initialize DAOs
            final CustomerDAO customerDAO = new CustomerDAO();
            final InvoiceDAO invoiceDAO = new InvoiceDAO();
            final ProductDAO productDAO = new ProductDAO();

            // Test Data Prep
            // Given name only, derive customerId
            final Integer testCustomerId = customerDAO.getCustomerIdByName("Susanne", "Smith");

            // invoiceId is not an IDENTITY, manually find Max
            final Integer nextInvoiceId = invoiceDAO.getMaxInvoiceNumber() + 1;

            // prepare Product List Data
            final List<Product> products = new ArrayList<Product>();
            products.add(new Product(null, "Iron Clock", new BigDecimal("6.60")));
            products.add(new Product(null, "Iron Telephone", new BigDecimal("23.60")));
            products.add(new Product(null, "Shoe Shoe", new BigDecimal("5.20")));
            products.forEach(product -> {
                try {
                    product.setId(productDAO.getProductIdByProductNameAndPrice(product.getName(), product.getPrice()));
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
                System.out.println(product.getId());
            });

            // Sum of the Current Product List For Invoice
            final BigDecimal invoiceTotal = products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO,
                    BigDecimal::add);

            // Connection does not Autocommit so the test is repeatable
            Connection con = ds.getConnection();
            con.setAutoCommit(false);
            Invoice testInvoice = new Invoice(nextInvoiceId, testCustomerId, invoiceTotal);

            // Convert our Product List into Line Items, This conversion is neccesary for
            // passing Quantity
            List<Item> items = new ArrayList<Item>();
            int quantity = 1;
            for (int item = 0; item < products.size(); item++) {
                Item insertItem = new Item(nextInvoiceId, item, products.get(item).getId(), quantity,
                        (products.get(item).getPrice().multiply(new BigDecimal(quantity))));
                items.add(insertItem);
            }

            // Insert Statements
            InvoiceUtils.insertCustomerInvoiceWithLineItems(con, testInvoice, items);

            final List<Invoice> invoices = invoiceDAO.selectByInvoiceId(con, nextInvoiceId);
            con.close();
            assertEquals(invoices.size(), 1);
            Invoice actualInvoice = invoices.get(0);
            assertEquals(actualInvoice.getId(), testInvoice.getId());
            assertEquals(actualInvoice.getCustomerId(), testInvoice.getCustomerId());
            assertEquals(actualInvoice.getTotal(), testInvoice.getTotal());

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    

    }

    // Exercise 1
    /**
     * Returns a list of Items along with associated item invoice, product and
     * customer information
     * 
     * @param invoiceId uniquely identifies the invoice the items were a part of;
     * @return line items associated with the invoice id
     * @see InvoiceLineItem
     * @throws SQLException
     */
    private List<InvoiceLineItem> getLineItemsByInvoiceId(final Integer invoiceId) throws SQLException {
        final List<InvoiceLineItem> invoiceLineItems = new ArrayList<InvoiceLineItem>();
        final String preparedString = String.join("\n",
             "SELECT CUSTOMER.ID, CUSTOMER.FIRSTNAME,", "CUSTOMER.LASTNAME,",
            "ITEM.INVOICEID, ITEM.ITEM,  ITEM.QUANTITY, PRODUCT.NAME", "FROM ITEM",
            "INNER JOIN PRODUCT ON ITEM.PRODUCTID = PRODUCT.ID",
            "INNER JOIN INVOICE ON ITEM.INVOICEID = INVOICE.ID",
            "INNER JOIN CUSTOMER ON INVOICE.CUSTOMERID = CUSTOMER.ID", "WHERE ITEM.INVOICEID=?");
        final Connection con = ds.getConnection();
        final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, invoiceId.toString());
        final ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            final InvoiceLineItem lineItem = new InvoiceLineItem();
            lineItem.setCustomerId(rs.getInt("CUSTOMER.ID"));
            lineItem.setFirst(rs.getString("CUSTOMER.FIRSTNAME"));
            lineItem.setLast(rs.getString("CUSTOMER.LASTNAME"));
            lineItem.setInvoiceId(rs.getInt("ITEM.INVOICEID"));
            lineItem.setItemNumber(rs.getInt("ITEM.ITEM"));
            lineItem.setQuantity(rs.getInt("ITEM.QUANTITY"));
            lineItem.setProductName(rs.getString("PRODUCT.NAME"));
            invoiceLineItems.add(lineItem);
        }
        con.close();
        return invoiceLineItems;
    }

}