package com.zerobase.reserve.global.utils;

import com.zerobase.reserve.global.exception.ApiExternalException;
import com.zerobase.reserve.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

/**
 * 외부 api와 통신하기 위한 범용 유틸 클래스입니다.
 * RestTemplate의 경우 지역적으로 생성하여 사용하게 되면 리소스가 낭비될 수 있습니다.
 * 때문에 singleton으로 생성하여 사용하도록 합니다.
 * 또한 외부와 통신하는 과정이 많을 것을 대비해 자주 사용하는 메소드를 미리 구현하여
 * 일관된 처리를 할 수 있도록 합니다.
 */
@Slf4j
@Component
public class RestTemplateUtils {
    private final RestTemplate restTemplate;

    public RestTemplateUtils() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(
                        PoolingHttpClientConnectionManagerBuilder.create()
                                .setMaxConnTotal(200)
                                .setMaxConnPerRoute(50)
                                .build())
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(3000);

        this.restTemplate = new RestTemplate(factory);
    }

    public String exchange(UriComponents uriComponents, HttpHeaders headers) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uriComponents.encode().toUri(), HttpMethod.GET,
                    new HttpEntity<>(headers), String.class);

            return response.getBody();
        } catch (RestClientException e) {
            log.error("RestClientException is occurred. ", e);
            throw new ApiExternalException(ErrorCode.INTERNAL_ERROR,
                    "restTemplate api server error");
        }
    }
}
