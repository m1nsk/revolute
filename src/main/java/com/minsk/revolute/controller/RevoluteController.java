package com.minsk.revolute.controller;

import com.minsk.revolute.convertor.TransferDataConverter;
import com.minsk.revolute.convertor.TransferDataConverterImpl;
import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.entity.Account;
import com.minsk.revolute.exceptions.NotEnoughMoneyException;
import com.minsk.revolute.exceptions.RevoluteValidationException;
import com.minsk.revolute.repository.AccountRepositoryImpl;
import com.minsk.revolute.service.RevoluteService;
import com.minsk.revolute.service.RevoluteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.util.HashMap;

import static spark.Spark.*;

@Slf4j
public class RevoluteController {

    private TransferDataConverter converter;

    private RevoluteService service;

    public RevoluteController(TransferDataConverter converter, RevoluteService service) {
        this.converter = converter;
        this.service = service;
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        TransferDataConverterImpl converter = new TransferDataConverterImpl();
        HashMap<Long, Account> accountData = new HashMap<>();
        AccountRepositoryImpl respository = new AccountRepositoryImpl(accountData);
        RevoluteServiceImpl service = new RevoluteServiceImpl(respository);
        RevoluteController revoluteController = new RevoluteController(converter, service);
        revoluteController.launcher();
    }

    public void launcher() {
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

        exception(RevoluteValidationException.class, (e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        });
    }
}
