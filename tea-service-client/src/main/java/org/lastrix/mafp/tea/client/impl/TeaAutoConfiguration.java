package org.lastrix.mafp.tea.client.impl;

import feign.RequestInterceptor;
import org.lastrix.http.client.api.AbstractHttpClientAutoConfiguration;
import org.lastrix.rest.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class TeaAutoConfiguration extends AbstractHttpClientAutoConfiguration {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    @ConditionalOnProperty(name = "mafp.tea.enable", havingValue = "true", matchIfMissing = true)
    public TeaClient teaClient(@Value("${mafp.tea.url}") String url) {
        return buildClient(url, TeaClient.class, "TeaService");
    }

    @Override
    protected RequestInterceptor jwtAuthInterceptor() {
        return t -> {
            t.removeHeader(HttpHeaders.AUTHORIZATION);
            t.header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.getToken());
        };
    }

    @Bean
    @ConditionalOnBean(TeaClient.class)
    public TeaClientService teaService(TeaClient client) {
        return new RestTeaClientService(client);
    }

    @Bean
    @ConditionalOnMissingBean(TeaClient.class)
    public TeaClientService teaServiceMock() {
        return new TeaClientServiceMock();
    }
}
