package org.lastrix.mafp.tea.perf;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.lastrix.jwt.Jwt;
import org.lastrix.jwt.JwtSecret;
import org.lastrix.jwt.UserType;
import org.lastrix.perf.tester.PerfSuite;
import org.lastrix.rest.JwtAutoConfiguration;
import org.springframework.http.HttpHeaders;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class TeaPerfSuite implements PerfSuite {
    private static final int USER_COUNT = 100_000;
    private static final Instant EXPIRATION = Instant.now().plus(24, ChronoUnit.HOURS);
    private final JwtSecret secret = new JwtSecret();
    private final OkHttpClient client = new OkHttpClient();
    private final SecureRandom random = new SecureRandom();
    private final Map<UUID, String> userTokens = new HashMap<>();

    @Override
    public void init() {
        log.info("Initializing...");
        var m = IntStream.range(1, USER_COUNT + 1)
                .parallel()
                .mapToObj(x -> {
                    var uid = new UUID(0L, x);
                    return ImmutablePair.of(uid, tokenForUser(uid));
                })
                .collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
        userTokens.putAll(m);
        log.info("Initialization complete!");
    }

    private String tokenForUser(UUID uid) {
        return "Bearer " + JWT.create()
                .withExpiresAt(Date.from(EXPIRATION))
                .withIssuer(JwtAutoConfiguration.MAFP_ISSUER)
                .withClaim(Jwt.CLAIM_USER_TYPE, UserType.PERSON.name())
                .withClaim(Jwt.CLAIM_USER_ID, uid.toString())
//                .withArrayClaim(Jwt.CLAIM_ROLES, new String[]{"User"})
                .sign(secret.getAlgorithm());
    }

    @Override
    public void next() throws Exception {
        var userId = new UUID(0L, random.nextInt(USER_COUNT) + 1);
        var amount = random.nextInt(900) + 100;
        try {
            var result = doDrink(userId, amount);
            log.trace("Got result: {}", result);
        } catch (Exception e) {
            log.info("Failed to create request", e);
        }
    }

    @Override
    public void close() {

    }

    private String doDrink(UUID userId, int amount) {
        var url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(8081)
                .encodedPath("/api/v1/tea/dummy")
                .build();

        var request = new Request.Builder()
                .url(url)
                .post(new FormBody.Builder()
                        .add("amount", String.valueOf(amount))
                        .add("teaType", "Green")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, userTokens.get(userId))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().toString();
        } catch (Exception e) {
            log.info("Failed to request resource", e);
            return "";
        }
    }
}
