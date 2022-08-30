package com.hilton.config;

import com.google.inject.Provides;
import com.hilton.GeoConfiguration;
import com.hilton.client.GeolocationApiService;
import com.hilton.client.GeolocationApiServiceImpl;
import com.hilton.core.CacheableGeoServiceImpl;
import com.hilton.core.GeolocationService;
import com.hilton.utils.AppRunnerUtils;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

import javax.ws.rs.client.Client;

public class GeoAppModule extends DropwizardAwareModule<GeoConfiguration> {

    private final AppRunnerUtils util = new AppRunnerUtils();

    @Provides
    public Client getJerseyClient() {
        return util.buildJerseyClient(configuration(), environment());
    }

    @Override
    protected void configure() {
        super.configure();
        bind(GeolocationApiService.class).to(GeolocationApiServiceImpl.class);
        bind(GeolocationService.class).to(CacheableGeoServiceImpl.class);
    }
}
