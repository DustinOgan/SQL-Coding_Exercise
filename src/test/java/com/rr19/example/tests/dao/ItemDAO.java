package com.rr19.example.tests.dao;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rr19.example.tests.dataTypes.Item;

public class ItemDAO extends BaseDAO {
    public ItemDAO() {

        super();
    }
    
    /**
     * 
     * @param invoiceId
     * @param itemNo
     * @param productId
     * @param quantity
     * @param cost
     * @return
     * @throws SQLException
     */
    public boolean insert(Integer invoiceId, Integer itemNo, Integer productId, Integer quantity, BigDecimal cost)
            throws SQLException {
        Connection con = ds.getConnection();
        boolean insertStatus = insert(con, invoiceId, itemNo, productId, quantity, cost);
        con.close();
        return insertStatus;
    }

    public boolean insert(Connection con, Integer invoiceId, Integer itemNo, Integer productId, Integer quantity,
            BigDecimal cost) throws SQLException {
        final String preparedString = String.join("\n",
                "INSERT INTO ITEM(\"INVOICEID\",\"ITEM\",\"PRODUCTID\",\"QUANTITY\",\"COST\")",
                "VALUES ( ? ,? , ?,? ,? )");
        final PreparedStatement ps = con.prepareStatement(preparedString);
        ps.setInt(1, invoiceId);
        ps.setInt(2, itemNo);
        ps.setInt(3, productId);
        ps.setInt(4, quantity);
        ps.setBigDecimal(5, cost);
        return ps.execute();
    }

    /**
     * 
     * @param invoiceId
     * @return List of line item records for a given invoiceID
     * @throws SQLException
     */
    public List<Item> selectItemsByInvoiceId(Integer invoiceId) throws SQLException {
        Connection con = ds.getConnection();
        List<Item> selectedItems = selectItemsByInvoiceId(con, invoiceId);
        con
        .close();
        return selectedItems;
    }
    
    public List<Item> selectItemsByInvoiceId(Connection con, Integer invoiceId) throws SQLException {
        List<Item> selectedItems = new ArrayList<Item>();
        final String preparedString = String.join("\n",
                "SELECT ITEM.INVOICEID, ITEM.ITEM, ITEM.PRODUCTID, ITEM.QUANTITY, ITEM.COST", "FROM ITEM",
                "WHERE ITEM.INVOICEID = ?");
        final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE);
        ps.setInt(1, invoiceId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Item item = new Item();
            item.setInvoiceId(rs.getInt("ITEM.INVOICEID"));
            item.setItem(rs.getInt("ITEM.ITEM"));
            item.setProductId(rs.getInt("ITEM.PRODUCTID"));
            item.setQuantity(rs.getInt("ITEM.QUANTIY"));
            item.setCost(rs.getBigDecimal("ITEM.COST"));
            selectedItems.add(item);
        }
        return selectedItems;
    }

}
