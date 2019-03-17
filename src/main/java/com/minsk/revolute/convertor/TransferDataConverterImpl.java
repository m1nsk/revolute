package com.minsk.revolute.convertor;

import com.minsk.revolute.dto.TransferDto;
import com.minsk.revolute.exceptions.RevoluteValidationException;
import com.minsk.revolute.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class TransferDataConverterImpl implements TransferDataConverter {
    public TransferDto convertRequest(String id1, String id2, String amount) {
        Long idOut = validateIdOut(id1);
        Long idIn = validateIdIn(id2);
        BigDecimal amountValue = validateAmount(amount);
        validateCondition(idOut, idIn, amount);
        return new TransferDto(idOut, idIn, amountValue);
    }

    private void validateCondition(Long idOut, Long idIn, String amount) {
        if(idIn.equals(idOut)) {
            throw new RevoluteValidationException("idOut and idIn can't be the same numbers");
        }
    }

    private static BigDecimal validateAmount(String amount) {
        amount = ValidationUtils.exceptionIfNull(amount, "null amount value");
        try {
            BigDecimal value = new BigDecimal(amount);
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RevoluteValidationException("amount should be greater then zero");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new RevoluteValidationException("invalid amount value");
        }
    }

    private static Long validateIdIn(String idIn) {
        idIn = ValidationUtils.exceptionIfNull(idIn, "null idIn value");
        try {
            return Long.parseLong(idIn);
        } catch (NumberFormatException e) {
            throw new RevoluteValidationException("invalid idIn value");
        }
    }

    private static Long validateIdOut(String idOut) {
        idOut = ValidationUtils.exceptionIfNull(idOut, "null idOut value");
        try {
            return Long.parseLong(idOut);
        } catch (NumberFormatException e) {
            throw new RevoluteValidationException("invalid idOut value");
        }
    }
}
