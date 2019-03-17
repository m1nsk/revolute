package com.minsk.revolute.service;

import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.repository.AccountRespository;

import java.math.BigDecimal;

public class RevoluteServiceImpl implements RevoluteService {

    private AccountRespository respository;

    public RevoluteServiceImpl(AccountRespository respository) {
        this.respository = respository;
    }

    public void transferMoney(TransferDto transferDto) {
        Long idOut = transferDto.getIdOut();
        Long idIn = transferDto.getIdIn();
        BigDecimal amount = transferDto.getAmount();
        respository.transfer(idOut, idIn, amount);
    }
}
