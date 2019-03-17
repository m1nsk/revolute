package com.minsk.revolute.service;

import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.repository.AccountRepository;

import java.math.BigDecimal;

public class ServiceImpl implements Service {

    private AccountRepository repository;

    public ServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    public void transferMoney(TransferDto transferDto) {
        Long idOut = transferDto.getIdOut();
        Long idIn = transferDto.getIdIn();
        BigDecimal amount = transferDto.getAmount();
        repository.transfer(idOut, idIn, amount);
    }
}
