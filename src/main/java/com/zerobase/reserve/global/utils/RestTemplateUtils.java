package com.zerobase.reserve.global.utils;

import com.zerobase.reserve.global.exception.ApiBadRequestException;
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
            throw new ApiBadRequestException(ErrorCode.INTERNAL_ERROR,
                    "restTemplate api server error");
        }
    }
}
