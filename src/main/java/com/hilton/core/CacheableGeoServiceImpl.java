package com.hilton.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hilton.api.GeolocationResponseDto;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class CacheableGeoServiceImpl extends GeolocationServiceImpl {

    // thread-safe cache
    private final LoadingCache<String, GeolocationResponseDto> cache;

    @Inject
    public CacheableGeoServiceImpl(@Config("app.ttl") int ttl) {
        CacheLoader<String, GeolocationResponseDto> loader;
        loader = new CacheLoader<>() {
            @Override
            public GeolocationResponseDto load(String ip) {
                log.info("Cache has no ip: {}, loading from database.", ip);
                return fetchDto(ip);
            }
        };
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(ttl, TimeUnit.SECONDS)
                .build(loader);
    }

    @Override
    public GeolocationResponseDto getGeolocationByIp(String ip) {
        try {
            return cache.get(ip);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @InTransaction
    @Override
    public List<String> refreshDeprecated() {
        List<String> refreshedIps = super.refreshDeprecated();
        cache.invalidateAll(refreshedIps);
        return refreshedIps;
    }

    private GeolocationResponseDto fetchDto(String ip) {
        return super.getGeolocationByIp(ip);
    }

}
