package com.zerobase.reserve.global.infra.address;

import com.zerobase.reserve.global.infra.address.dto.CoordinateDto;

public interface CoordinateClient {
    CoordinateDto getCoordinate(String address);
}
