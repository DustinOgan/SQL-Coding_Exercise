package com.rr19.example.tests.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rr19.example.tests.dataTypes.Product;

public class ProductDAO extends BaseDAO {
    public Integer getProductIdByProductNameAndPrice(String productName, BigDecimal price ) throws SQLException{
        final Product product = new Product();
        final Connection con = ds.getConnection();
        final String preparedString = String.join("\n", 
        "SELECT PRODUCT.ID from PRODUCT",
        "WHERE PRODUCT.NAME = ?",
        "AND PRICE = ?");
        final PreparedStatement ps = con.prepareStatement(preparedString, ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, productName);
        ps.setString(2, price.toString());
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            product.setId(rs.getInt("PRODUCT.ID"));
        }
        con.close();
        return product.getId();
    }
}