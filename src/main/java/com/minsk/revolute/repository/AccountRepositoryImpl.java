package com.minsk.revolute.repository;

import com.minsk.revolute.entity.Account;
import com.minsk.revolute.exceptions.NotEnoughMoneyException;
import com.minsk.revolute.utils.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccountRepositoryImpl implements AccountRepository {

    private Map<Long, Account> accountMap;

    public AccountRepositoryImpl(Map<Long, Account> accountMap) {
        this.accountMap = accountMap;
    }

    @Override
    public void transfer(Long outId, Long inId, BigDecimal amount) {
        Account inAccount = ValidationUtils.exceptionIfNull(accountMap.get(inId), "no such id in account");
        Account outAccount = ValidationUtils.exceptionIfNull(accountMap.get(outId), "no such out account");
        MonitorHandler monitorHandler = new MonitorHandler(inAccount, outAccount);
        synchronized (monitorHandler.first) {
            synchronized (monitorHandler.second) {
                BigDecimal outAmount = outAccount.getAmount();
                if(outAmount.compareTo(amount) < 0) {
                    throw new NotEnoughMoneyException(amount.toString());
                }
                inAccount.setAmount(inAccount.getAmount().add(amount));
                outAccount.setAmount(outAccount.getAmount().subtract(amount));
            }
        }
    }

    @Override
    public Set<Account> getAll() {
        return new HashSet<>(accountMap.values());
    }

    @Override
    public Account getById(Long id) {
        return accountMap.get(id);
    }

    private static class MonitorHandler {

        private final Account first;
        private final Account second;

        private MonitorHandler(Account out, Account in) {
                this.first = out.getId().compareTo(in.getId()) > 0 ? out : in;
                this.second = out.getId().compareTo(in.getId()) > 0 ? in : out;
        }
    }
}
