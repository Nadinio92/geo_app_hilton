package com.hilton.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.hilton.GeoApplication;
import com.hilton.api.GeolocationResponseDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import ru.vyarus.dropwizard.guice.test.ClientSupport;
import ru.vyarus.dropwizard.guice.test.jupiter.TestDropwizardApp;

import javax.ws.rs.core.Response;

@TestDropwizardApp(value = GeoApplication.class, config = "config.yml")
public class IntegrationTest {

    @Inject
    ObjectMapper mapper;

    @Test
    public void callApi(ClientSupport client) throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Response response = client.target("http://localhost:8080/v1/geolocation?ip=123.123.32.23")
                .request()
                .get();
        var receivedDto = response.readEntity(GeolocationResponseDto.class);
        var expectedDto = mapper.readValue(getClass().getResource("/fixtures/123.123.32.23.json"), GeolocationResponseDto.class);
        receivedDto.setTimestamp(null);
        expectedDto.setTimestamp(null);
        Assert.assertEquals(receivedDto, expectedDto);
    }
}