package com.minsk.revolute.integration;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.minsk.revolute.controller.Controller;
import com.minsk.revolute.convertor.TransferDataConverterImpl;
import com.minsk.revolute.entity.Account;
import com.minsk.revolute.repository.AccountRepositoryImpl;
import com.minsk.revolute.service.ServiceImpl;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    @BeforeAll
    static void integration() {
        BasicConfigurator.configure();
        TransferDataConverterImpl converter = new TransferDataConverterImpl();
        HashMap<Long, Account> accountData = new HashMap<>();
        LongStream.range(0, 10).forEach(i -> accountData.put(i, new Account(i, new BigDecimal("2000.00"))));
        AccountRepositoryImpl repository = new AccountRepositoryImpl(accountData);
        ServiceImpl service = new ServiceImpl(repository);
        Controller controller = new Controller(converter, service);
        controller.launcher();
    }

    @Test
    void doValidRequest() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "1");
        map.put("id2", "2");
        map.put("amount", "1000.00");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        System.out.println(response.getBody());
        Assertions.assertTrue(response.getStatus() == 200);
    }

    @Test
    void doInvalidId1Request() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "a");
        map.put("id2", "2");
        map.put("amount", "1000.00");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        Assertions.assertTrue(response.getStatus() == 404);
        assertTrue(response.getBody().contains("idOut"));
    }

    @Test
    void doInvalidId2Request() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "1");
        map.put("id2", "b");
        map.put("amount", "1000.00");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        Assertions.assertTrue(response.getStatus() == 404);
        assertTrue(response.getBody().contains("idIn"));
    }

    @Test
    void doId1EqualsId2Request() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "1");
        map.put("id2", "1");
        map.put("amount", "1000.00");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        Assertions.assertTrue(response.getStatus() == 404);
        assertTrue(response.getBody().contains("same"));
    }

    @Test
    void doInvalidAmountRequest() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "1");
        map.put("id2", "2");
        map.put("amount", "a");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        System.out.println(response.getBody());
        Assertions.assertTrue(response.getStatus() == 404);
        assertTrue(response.getBody().contains("amount"));
    }

    @Test
    void doLessThanZeroAmountRequest() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "1");
        map.put("id2", "2");
        map.put("amount", "-1000.00");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        Assertions.assertTrue(response.getStatus() == 404);
        assertTrue(response.getBody().contains("greater"));
    }

    @Test
    void doNotEnoughMoneyRequest() throws UnirestException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", "1");
        map.put("id2", "2");
        map.put("amount", "10000.00");

        HttpResponse<String> response = Unirest.put("http://0.0.0.0:8080/transfer").fields(map).asString();
        Assertions.assertTrue(response.getStatus() == 404);
    }

}