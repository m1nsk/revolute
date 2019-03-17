package com.minsk.revolute.controller;

import com.minsk.revolute.convertor.TransferDataConverter;
import com.minsk.revolute.convertor.TransferDataConverterImpl;
import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.entity.Account;
import com.minsk.revolute.exceptions.NotEnoughMoneyException;
import com.minsk.revolute.exceptions.ValidationException;
import com.minsk.revolute.repository.AccountRepositoryImpl;
import com.minsk.revolute.service.Service;
import com.minsk.revolute.service.ServiceImpl;
import com.minsk.revolute.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.util.HashMap;
import java.util.stream.LongStream;

import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.exception;

@Slf4j
public class Controller {

    private TransferDataConverter converter;

    private Service service;

    public Controller(TransferDataConverter converter, Service service) {
        this.converter = converter;
        this.service = service;
    }

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

    public void launcher() {
        port(8080);

        put("/transfer", (req, res) -> {
            String id1 = req.queryParams("id1");
            String id2 = req.queryParams("id2");
            String amount = req.queryParams("amount");
            TransferDto data = converter.convertRequest(id1, id2, amount);
            service.transferMoney(data);
            res.status(200);
            res.body("successful");
            return res.body();
        });

        exception(NotEnoughMoneyException.class, (e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        });

        exception(ValidationException.class, (e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        });
    }
}
