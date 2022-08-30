package com.hilton.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hilton.client.dto.GeolocationDto;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

@Singleton
public class GeolocationApiServiceImpl implements GeolocationApiService {

    @Inject
    private Client httpClient;

    @Inject @Config("app.apiUrl")
    private String apiUrl;

    @Override
    public GeolocationDto getLocationByIp(String ip) {
        try {
            var webTarget = this.httpClient.target(String.format("%s%s", apiUrl, ip));
            var invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
            var response = invocationBuilder.get();
            return response.readEntity(GeolocationDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
