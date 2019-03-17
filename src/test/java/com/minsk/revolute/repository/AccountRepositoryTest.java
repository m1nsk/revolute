package com.minsk.revolute.repository;

import com.minsk.revolute.entity.Account;
import com.minsk.revolute.exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountRepositoryTest {

    private AccountRepository repository;

    @BeforeEach
    public void initAccountData(){
        Map<Long, Account> accountMap = new HashMap<>();
        accountMap.put(10L, new Account(10L, new BigDecimal("1000.00")));
        accountMap.put(11L, new Account(11L, new BigDecimal("100.00")));
        repository = new AccountRepositoryImpl(accountMap);
    }

    @Test
    void transfer() {
        BigDecimal sumBefore = repository.getAll()
                .stream().map(Account::getAmount)
                .reduce(BigDecimal::add).orElse(null);
        repository.transfer(10L, 11L, new BigDecimal("500"));
        BigDecimal sumAfter = repository.getAll()
                .stream().map(Account::getAmount)
                .reduce(BigDecimal::add).orElse(null);
        Assertions.assertEquals(new BigDecimal("500.00"), repository.getById(10L).getAmount());
        Assertions.assertEquals(new BigDecimal("600.00"), repository.getById(11L).getAmount());
        Assertions.assertEquals(sumBefore, sumAfter);
    }

    @Test
    void transferThereAndBack() {
        BigDecimal sumBefore = repository.getAll()
                .stream().map(Account::getAmount)
                .reduce(BigDecimal::add).orElse(null);
        repository.transfer(10L, 11L, new BigDecimal("500"));
        repository.transfer(11L, 10L, new BigDecimal("500"));
        BigDecimal sumAfter = repository.getAll()
                .stream().map(Account::getAmount)
                .reduce(BigDecimal::add).orElse(null);
        Assertions.assertEquals(new BigDecimal("1000.00"), repository.getById(10L).getAmount());
        Assertions.assertEquals(new BigDecimal("100.00"), repository.getById(11L).getAmount());
        Assertions.assertEquals(sumBefore, sumAfter);
    }

    @Test
    void doNotEnoughMoney() {
        NotEnoughMoneyException a = assertThrows(NotEnoughMoneyException.class, () ->
            repository.transfer(10L, 11L, new BigDecimal("2000"))
        );
    }
}