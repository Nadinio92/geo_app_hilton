package com.hilton.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class AppProperties extends Configuration {
    @JsonProperty("expiration") int dbExpiration;
    @JsonProperty("ttl") int cacheTtl;
    @JsonProperty("apiUrl") String apiUrl;
}
