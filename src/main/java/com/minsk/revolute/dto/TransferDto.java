package com.minsk.revolute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class TransferDto {
    Long idOut;
    Long idIn;
    BigDecimal amount;
}
