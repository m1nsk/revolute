package com.minsk.revolute.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Account {
    private final Long id;
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account clone() {
        return new Account(this.id, new BigDecimal(this.amount.toString()));
    }
}
