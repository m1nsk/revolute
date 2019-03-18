package com.minsk.revolute.controller;

import com.minsk.revolute.convertor.TransferDataConverter;
import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.exceptions.NotEnoughMoneyException;
import com.minsk.revolute.exceptions.ValidationException;
import com.minsk.revolute.service.Service;
import lombok.extern.slf4j.Slf4j;

import static spark.Spark.*;

@Slf4j
public class Controller {

    private TransferDataConverter converter;

    private Service service;

    public Controller(TransferDataConverter converter, Service service) {
        this.converter = converter;
        this.service = service;
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
            response.status(400);
            response.body(e.getMessage());
        });

        exception(ValidationException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
    }
}
