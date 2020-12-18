package com.example.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@Slf4j
public class RecommendationController {

    private static final String RESPONSE_STRING_FORMAT = "recommendation v2 from '%s': %d\n";

    /**
     * Counter to help us see the lifecycle
     */
    private int count = 0;

    /**
     * Flag for throwing a 503 when enabled
     */
    private boolean misbehave = false;

    private static final String HOSTNAME = parseContainerIdFromHostname(
            System.getenv().getOrDefault("HOSTNAME", "unknown"));

    static String parseContainerIdFromHostname(String hostname) {
        return hostname.replaceAll("recommendation-v\\d+-", "");
    }

    @RequestMapping("/")
    public ResponseEntity<String> getRecommendations(@RequestHeader HttpHeaders headers) {

        // print all http headers
        headers.forEach((key, value) -> {
            log.info(String.format(
                    "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });

        count++;
        log.info(String.format("recommendation request from %s: %d", HOSTNAME, count));

        // simulate service response slowly for Hystrix demo
        // Or just stop this service
//        timeout();

        log.debug("recommendation service ready to return");
        if (misbehave) {
            return doMisbehavior();
        }
        return ResponseEntity.ok(String.format(RecommendationController.RESPONSE_STRING_FORMAT, HOSTNAME, count));
    }

    private ResponseEntity<String> doMisbehavior() {
        log.debug(String.format("Misbehaving %d", count));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(String.format("recommendation misbehavior from '%s'\n", HOSTNAME));
    }

    private void timeout() {
        try {
            Thread.sleep(new java.util.Random().nextInt(10000 - 500) + 500); // sleep [500, 10000) ms
        } catch (InterruptedException e) {
            log.info("Thread interrupted");
        }
    }
}
