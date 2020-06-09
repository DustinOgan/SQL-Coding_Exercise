package com.rr19.example.tests.dataTypes;

import java.math.BigDecimal;

public class Item {
    Integer invoiceId;
    Integer item;
    Integer productId;
    Integer quantity;
    BigDecimal cost;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Item(Integer invoiceId, Integer item, Integer productId, Integer quantity, BigDecimal cost) {
        this.invoiceId = invoiceId;
        this.item = item;
        this.productId = productId;
        this.quantity = quantity;
        this.cost = cost;
    }

    public Item() {
    }
}