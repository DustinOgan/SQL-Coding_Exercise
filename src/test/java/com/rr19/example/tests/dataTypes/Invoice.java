package com.rr19.example.tests.dataTypes;

import java.math.BigDecimal;

public class Invoice {
    Integer id;
    Integer customerId;
    BigDecimal total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Invoice(Integer id, Integer customerId, BigDecimal total) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
    }

    public Invoice() {
    }
}