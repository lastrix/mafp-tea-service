package org.lastrix.mafp.tea.client.impl;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.lastrix.jwt.Jwt;
import org.lastrix.jwt.JwtSecret;
import org.lastrix.jwt.UserType;
import org.lastrix.mafp.tea.model.dto.TeaCupDto;
import org.lastrix.rest.JwtAutoConfiguration;
import org.lastrix.rest.Rest;
import org.lastrix.rest.RestAutoConfiguration;
import org.lastrix.rest.test.ControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Disabled
@SpringBootTest(classes = {
        RestAutoConfiguration.class,
        JwtAutoConfiguration.class
})
public class TeaClientTest extends ControllerTest {
    private static final int REQ_COUNT = 10_000;
    private static final int NUM_USERS = 1000;
    private static final int THREAD_COUNT = 16;
    @Autowired
    private JwtSecret jwtSecret;
    private final RestTemplate template = new RestTemplateBuilder().build();
    private final SecureRandom random = new SecureRandom();
    private final List<UUID> users = new ArrayList<>();

    @Test
    public void testRemote() throws Exception {
        var expiration = Instant.now().plus(5, ChronoUnit.HOURS);
        Map<UUID, String> tokens = IntStream.range(1, NUM_USERS + 1).parallel().boxed()
                .map(i -> {
                    UUID userId = new UUID(0L, i);
                    return ImmutablePair.of(userId, "Bearer " + JWT.create()
                            .withExpiresAt(Date.from(expiration))
                            .withIssuer(JwtAutoConfiguration.MAFP_ISSUER)
                            .withClaim(Jwt.CLAIM_USER_TYPE, UserType.PERSON.name())
                            .withClaim(Jwt.CLAIM_USER_ID, userId.toString())
//                .withArrayClaim(Jwt.CLAIM_ROLES, new String[]{"User"})
                            .sign(jwtSecret.getAlgorithm()));
                }).collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
        users.addAll(tokens.keySet());

        var f = tokens.entrySet().iterator().next();
        var result = doDrink(f.getKey(), 100, f.getValue());
        log.info("Got: {}", result);

        log.info("Warming up...");
        for (int i = 0; i < REQ_COUNT; i++) {
            var n = nextUser(tokens);
            doDrink(n.getKey(), 100, n.getValue());
        }

        var pool = new ForkJoinPool(THREAD_COUNT);
        AtomicInteger counter = new AtomicInteger();
        var monitor = new Object();
        var list = new CopyOnWriteArrayList<Duration>();
        log.info("Testing...");
        for (int i = 0; i < THREAD_COUNT; i++) {
            counter.incrementAndGet();
            pool.submit(() -> {
                try {
                    var start = Instant.now();
                    for (int k = 0; k < REQ_COUNT; k++) {
                        var n = nextUser(tokens);
                        doDrink(n.getKey(), 100, n.getValue());
                    }
                    var elapsed = Duration.between(start, Instant.now());
                    list.add(elapsed);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                } finally {
                    counter.decrementAndGet();
                    synchronized (monitor) {
                        monitor.notifyAll();
                    }
                }
            });
        }
        while (counter.get() > 0) {
            synchronized (monitor) {
                monitor.wait();
            }
        }
        list.forEach(e -> log.info("Taken: {}", e));
        var est = list.stream().mapToDouble(e -> e.toMillis() + (double) e.toNanosPart() * 10e-9).average().orElse(0d) / REQ_COUNT;
        log.info("Finished! {} ms, {} rps", est, 1000 / est * THREAD_COUNT);
    }

    private Map.Entry<UUID, String> nextUser(Map<UUID, String> tokens) {
        int pos = random.nextInt(users.size());
        var userId = users.get(pos);
        return ImmutablePair.of(userId, tokens.get(userId));
    }

    private Rest<TeaCupDto> doDrink(UUID userId, int amount, String token) {
        var uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/")
                .path("api/v1/tea/dummy")
                .queryParam("amount", amount)
                .queryParam("teaType", "Green")
                .build().toUri();

        var requestBuilder = RequestEntity.post(uri)
                .headers(buildRequestHeaders(userId, token))
                .build();
        var typeRef = new ParameterizedTypeReference<Rest<TeaCupDto>>() {
        };
        var response = template.exchange(requestBuilder, typeRef);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Unable to contact tea service: " + response.getStatusCode());
        }
        var body = response.getBody();
        if (body == null) throw new IllegalStateException();
        return body;
    }

    private HttpHeaders buildRequestHeaders(UUID userId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }
}
