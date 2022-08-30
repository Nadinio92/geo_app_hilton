package com.hilton;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hilton.config.AppProperties;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class GeoConfiguration extends Configuration {

    @Valid @NotNull
    private DataSourceFactory datasourceFactory = new DataSourceFactory();

    @Valid @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @Valid @NotNull
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    @JsonProperty("app")
    public AppProperties appProperties;

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.datasourceFactory = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return datasourceFactory;
    }

    @JsonProperty("httpClient")
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClient;
    }

    @JsonProperty("httpClient")
    public void setHttpClientConfiguration(HttpClientConfiguration httpClient) {
        this.httpClient = httpClient;
    }

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty("jerseyClient")
    public void setJerseyClientConfiguration(JerseyClientConfiguration jerseyClient) {
        this.jerseyClient = jerseyClient;
    }
}
