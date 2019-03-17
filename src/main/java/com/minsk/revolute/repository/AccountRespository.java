package com.minsk.revolute.repository;

import java.math.BigDecimal;

public interface AccountRespository {
    void transfer(Long outId, Long inId, BigDecimal amount);
}
