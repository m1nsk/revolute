package com.minsk.revolute.repository;

import com.minsk.revolute.entity.Account;
import com.minsk.revolute.exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountRepositoryConcurrentTest {

    private AccountRepository repository;

    @BeforeEach
    public void initAccountData(){
        Map<Long, Account> accountMap = new HashMap<>();
        accountMap.put(10L, new Account(10L, new BigDecimal("2000.00")));
        accountMap.put(11L, new Account(11L, new BigDecimal("5000.00")));
        accountMap.put(12L, new Account(12L, new BigDecimal("2000.00")));
        accountMap.put(13L, new Account(13L, new BigDecimal("3000.00")));
        repository = new AccountRepositoryImpl(accountMap);
    }

    @Test
    void transferConcurrent() throws ExecutionException, InterruptedException {
        int threadsCount = 10;
        ExecutorService service1 = Executors.newFixedThreadPool(threadsCount);
        ExecutorService service2 = Executors.newFixedThreadPool(threadsCount);

        BigDecimal sumBefore = repository.getAll()
                .stream().map(Account::getAmount)
                .reduce(BigDecimal::add).orElse(null);

        Collection<Future> futures = new ArrayList<>(threadsCount * 100);

        LongStream.range(0, threadsCount * 100).forEach(item -> {
            futures.add(service1.submit(() -> repository.transfer(10L, 11L, new BigDecimal("1.00"))));
            futures.add(service1.submit(() -> repository.transfer(10L, 12L, new BigDecimal("2.00"))));
            futures.add(service2.submit(() -> repository.transfer(11L, 10L, new BigDecimal("1.00"))));
            futures.add(service2.submit(() -> repository.transfer(12L, 10L, new BigDecimal("2.00"))));
        });

        for (Future item : futures) {
            item.get();
        }

        BigDecimal sumAfter = repository.getAll()
                .stream().map(Account::getAmount)
                .reduce(BigDecimal::add).orElse(null);
        Assertions.assertEquals(new BigDecimal("2000.00"), repository.getById(10L).getAmount());
        Assertions.assertEquals(new BigDecimal("5000.00"), repository.getById(11L).getAmount());
        Assertions.assertEquals(new BigDecimal("2000.00"), repository.getById(12L).getAmount());
        Assertions.assertEquals(sumBefore, sumAfter);
    }
}