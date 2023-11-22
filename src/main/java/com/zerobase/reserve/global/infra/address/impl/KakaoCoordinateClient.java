package com.zerobase.reserve.global.infra.address.impl;

import com.zerobase.reserve.global.infra.address.CoordinateClient;
import com.zerobase.reserve.global.infra.address.dto.CoordinateDto;
import com.zerobase.reserve.global.infra.address.dto.KakaoResponseDto;
import com.zerobase.reserve.global.utils.ObjectMapperUtils;
import com.zerobase.reserve.global.utils.RestTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoCoordinateClient implements CoordinateClient {
    @Value("${kakao.key}")
    private String key;
    private static final String URL = "https://dapi.kakao.com/v2/local/search/address";
    private final ObjectMapperUtils objectMapperUtils;
    private final RestTemplateUtils restTemplateUtils;

    @Override
    public CoordinateDto getCoordinate(String address) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("query", address)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, key);

        String result = restTemplateUtils.exchange(uriComponents, headers);
        return objectMapperUtils.jsonToObject(result, KakaoResponseDto.class);
    }
}
