package com.minsk.revolute.service;

import com.minsk.revolute.dto.TransferDto;

public interface RevoluteService {
    void transferMoney(TransferDto transferDto);
}
