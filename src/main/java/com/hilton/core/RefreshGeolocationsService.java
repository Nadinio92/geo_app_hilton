package com.hilton.core;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RefreshGeolocationsService extends AbstractScheduledService implements Managed {

    @Inject
    private GeolocationService service;

    @Override
    public void start() throws Exception {
        startAsync();
    }

    @Override
    public void stop() throws Exception {
        stopAsync();
    }

    @Override
    protected void runOneIteration() {
        var ips = service.refreshDeprecated();
        log.info("Refreshed geolocations for IPs: {}", ips);
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.MINUTES);
    }
}
