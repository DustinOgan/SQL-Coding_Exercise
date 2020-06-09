package com.rr19.example.tests.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.rr19.example.tests.dao.InvoiceDAO;
import com.rr19.example.tests.dao.ItemDAO;
import com.rr19.example.tests.dataTypes.Invoice;
import com.rr19.example.tests.dataTypes.Item;

public class InvoiceUtils {

    /**
     * 
     * @param con
     * @param invoice
     * @param items
     * @throws SQLException
     */
    public static void insertCustomerInvoiceWithLineItems(Connection con,
        Invoice invoice, List<Item> items) throws SQLException {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        ItemDAO itemDAO = new ItemDAO();
        invoiceDAO.insert(con,invoice.getId(), invoice.getCustomerId(), invoice.getTotal());
        items.forEach(item ->{
            try {
                itemDAO.insert(con, item.getInvoiceId(), item.getItem(), item.getProductId(), item.getQuantity(),
                        item.getCost());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}