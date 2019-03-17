package com.minsk.revolute.repository;

import com.minsk.revolute.entity.Account;

import java.math.BigDecimal;
import java.util.Set;

public interface AccountRepository {
    void transfer(Long outId, Long inId, BigDecimal amount);
    Set<Account> getAll();
    Account getById(Long id);
}
