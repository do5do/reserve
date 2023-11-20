package com.zerobase.reserve.global.infra.address.impl;

import com.zerobase.reserve.global.exception.ApiBadRequestException;
import com.zerobase.reserve.global.exception.ErrorCode;
import com.zerobase.reserve.global.infra.address.CoordinateClient;
import com.zerobase.reserve.global.infra.address.dto.CoordinateDto;
import com.zerobase.reserve.global.infra.address.dto.KakaoResponseDto;
import com.zerobase.reserve.global.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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
    private final RestTemplate restTemplate;

    @Override
    public CoordinateDto getCoordinate(String address) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("query", address)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uri.toUri(), HttpMethod.GET, entity, String.class);

            return objectMapperUtils.jsonToObject(response.getBody(),
                    KakaoResponseDto.class);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException is occurred. ", e);
            throw new ApiBadRequestException(ErrorCode.INVALID_REQUEST);
        }
    }
}
