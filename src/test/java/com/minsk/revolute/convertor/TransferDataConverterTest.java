package com.minsk.revolute.convertor;

import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TransferDataConverterTest {

    private TransferDataConverter transferDataConverter = new TransferDataConverterImpl();

    @Test
    void convertValidRequest() {
        TransferDto transferDto = transferDataConverter.convertRequest("10", "11", "1000.00");
        assertEquals(new Long(10L), transferDto.getIdOut());
        assertEquals(new Long(11L), transferDto.getIdIn());
        assertEquals(new BigDecimal("1000.00"), transferDto.getAmount());
    }

    @Test
    void convertInvalidId1Request() {
        ValidationException a = assertThrows(ValidationException.class, () -> {
            TransferDto transferDto = transferDataConverter.convertRequest("a", "11", "1000.00");
            assertEquals(new Long(10L), transferDto.getIdOut());
        });
        assertTrue(a.getMessage().contains("idOut"));
    }

    @Test
    void convertInvalidId2Request() {
        ValidationException a = assertThrows(ValidationException.class, () -> {
            TransferDto transferDto = transferDataConverter.convertRequest("10", "b", "1000.00");
        });
        assertTrue(a.getMessage().contains("idIn"));
    }

    @Test
    void convertId1EqualsId2Request() {
        ValidationException a = assertThrows(ValidationException.class, () -> {
            TransferDto transferDto = transferDataConverter.convertRequest("10", "10", "1000.00");
        });
        assertTrue(a.getMessage().contains("same"));
    }

    @Test
    void convertInvalidAmountRequest() {
        ValidationException a = assertThrows(ValidationException.class, () -> {
            TransferDto transferDto = transferDataConverter.convertRequest("10", "11", "asd2");
        });
        assertTrue(a.getMessage().contains("amount"));
    }

    @Test
    void convertLessThanZeroAmountRequest() {
        ValidationException a = assertThrows(ValidationException.class, () -> {
            TransferDto transferDto = transferDataConverter.convertRequest("10", "11", "-100.00");
        });
        assertTrue(a.getMessage().contains("greater"));
    }
}