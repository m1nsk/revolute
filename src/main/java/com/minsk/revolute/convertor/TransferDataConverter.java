package com.minsk.revolute.convertor;

import com.minsk.revolute.dto.TransferDto;

public interface TransferDataConverter {
    TransferDto convertRequest(String id1, String id2, String amount);
}
