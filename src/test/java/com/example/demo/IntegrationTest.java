package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = "numberServiceUrl=http://localhost:8001/")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    WireMockServer wireMockServer = new WireMockServer(options().port(8001));

    @BeforeEach
    public void beforeEach() {
        wireMockServer.start();

        String json = "{ \"number\": \"2\" }";
        wireMockServer.stubFor(get("/").willReturn(aResponse().withBody(json)));
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.stop();
    }

    @Test
    public void fizzBuzzIntegratesWithNumberService() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://localhost:" + port + "/api/fizzbuzz", String.class);
        assertEquals("2", result);
    }
}
