package com.minsk.revolute;

import com.minsk.revolute.controller.Controller;
import com.minsk.revolute.convertor.TransferDataConverterImpl;
import com.minsk.revolute.entity.Account;
import com.minsk.revolute.repository.AccountRepositoryImpl;
import com.minsk.revolute.service.ServiceImpl;
import com.minsk.revolute.utils.TestUtils;
import org.apache.log4j.BasicConfigurator;

import java.util.HashMap;
import java.util.stream.LongStream;

public class Louncher {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        TransferDataConverterImpl converter = new TransferDataConverterImpl();
        HashMap<Long, Account> accountData = new HashMap<>();
        LongStream.range(0, 10).forEach(i -> accountData.put(i, new Account(i, TestUtils.random(1000))));
        AccountRepositoryImpl repository = new AccountRepositoryImpl(accountData);
        ServiceImpl service = new ServiceImpl(repository);
        Controller controller = new Controller(converter, service);
        controller.launcher();
    }
}
