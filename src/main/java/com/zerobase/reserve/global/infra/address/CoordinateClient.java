package com.zerobase.reserve.global.infra.address;

import com.zerobase.reserve.global.infra.address.dto.CoordinateDto;

/**
 * 주소 기반 좌표를 가져오는 범용 인터페이스입니다.
 * 좌표 데이터를 얻어오는 대상이 교체될 수 있기 때문에 인터페이스를 두었습니다.
 */
public interface CoordinateClient {
    CoordinateDto getCoordinate(String address);
}
