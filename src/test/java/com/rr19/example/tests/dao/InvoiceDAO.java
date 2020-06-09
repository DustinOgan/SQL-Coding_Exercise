package com.rr19.example.tests.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rr19.example.tests.dataTypes.Invoice;

public class InvoiceDAO extends BaseDAO {
    public boolean insert(Integer invoiceId, Integer customerId, BigDecimal total) throws SQLException {
        Connection con = ds.getConnection();
        return insert(con,invoiceId, customerId, total);
    }
    public boolean insert(Connection con, Integer invoiceId, Integer customerId, BigDecimal total)
            throws SQLException {
        final String preparedString = String.join("\n", "INSERT INTO INVOICE( \"ID\", \"CUSTOMERID\", \"TOTAL\" )",
                "VALUES ( ?,? ,? )");
        final PreparedStatement ps = con.prepareStatement(preparedString);
        ps.setInt(1, invoiceId);
        ps.setInt(2, customerId);
        ps.setBigDecimal(3, total);
        return ps.execute();

    }
    public void delete(Integer invoiceId) throws SQLException {
        Connection con  = ds.getConnection();
        String preparedString = String.join("\n",
        "DELETE FROM INVOICE",
        "WHERE INVOICE.ID = ?");
        PreparedStatement ps =  con.prepareStatement(preparedString);
        ps.setInt(1, invoiceId);
        ps.execute();
        
    }
    public List<Invoice>  selectByInvoiceId(Integer invoiceId) throws SQLException {
        Connection con = ds.getConnection();
        List<Invoice> invoices = selectByInvoiceId(con, invoiceId);
        con.close();
        return invoices;
    }
    public List<Invoice> selectByInvoiceId(Connection con, Integer invoiceId) throws SQLException {
        List<Invoice> invoices = new ArrayList<Invoice>();
            final String preparedString = String.join("\n",
                    "SELECT INVOICE.ID, INVOICE.CUSTOMERID, INVOICE.TOTAL FROM INVOICE",
                    "WHERE INVOICE.ID = ?");
            final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, invoiceId);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("INVOICE.ID"));
                invoice.setCustomerId(rs.getInt("INVOICE.CUSTOMERID"));
                invoice.setTotal(rs.getBigDecimal("INVOICE.TOTAL"));
                invoices.add(invoice);
            }
        return invoices;
    }

    public Integer getMaxInvoiceNumber() throws SQLException {
        Integer maxInvoice = null;
        final Connection con = ds.getConnection();
        final String preparedString = String.join("\n", "SELECT MAX(ID) as maxId FROM INVOICE");

        final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        final ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            maxInvoice = rs.getInt("maxId");
        }
        return maxInvoice;

    }
    public List<Invoice> selectInvoicesByCustomerId(Integer customerId) throws SQLException{
        List<Invoice> invoices = new ArrayList<Invoice>();
        Connection con = ds.getConnection();
        String preparedString = String.join("\n",
        "SELECT INVOICE.ID, INVOICE.CUSTOMERID, INVOICE.TOTAL FROM INVOICE",
         "WHERE INVOICE.CUSTOMERID = ?");
         PreparedStatement ps = con.prepareStatement(preparedString);
         ps.setInt(1, customerId);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            Invoice invoice = new Invoice();
            invoice.setId(rs.getInt("INVOICE.ID"));
            invoice.setCustomerId(rs.getInt("INVOICE.CUSTOMERID"));
            invoice.setTotal(rs.getBigDecimal("INVOICE.TOTAL"));
            invoices.add(invoice);
        }
        con.close();
        return invoices;
    
    } 

    public InvoiceDAO() {
        super();
    }
}