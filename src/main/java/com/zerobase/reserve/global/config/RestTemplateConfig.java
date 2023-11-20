package com.zerobase.reserve.global.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(
                        PoolingHttpClientConnectionManagerBuilder.create()
                                .setMaxConnTotal(200)
                                .setMaxConnPerRoute(50)
                                .build()
                )
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory factory(
            CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(3000);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(
            HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
