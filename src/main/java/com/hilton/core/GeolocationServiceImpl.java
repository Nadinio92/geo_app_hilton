package com.hilton.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.hilton.api.GeolocationResponseDto;
import com.hilton.client.GeolocationApiService;
import com.hilton.client.dto.GeolocationDto;
import com.hilton.db.GeolocationDao;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.time.Instant;
import java.util.List;

@Slf4j
public class GeolocationServiceImpl implements GeolocationService {

    @Inject @Config("app.expiration")
    private int ipExpirationPeriod;

    @Inject
    private GeolocationApiService geolocationApiService;

    @Inject
    private GeolocationDao dao;

    @Inject
    private ObjectMapper objectMapper;

    @InTransaction
    @Override
    public GeolocationResponseDto getGeolocationByIp(String ip) {
        try {
            var geolocationEntity = dao.findByIp(ip).orElseGet(() -> {
                fetchFromRemoteAndSave(ip);
                return dao.findByIp(ip).orElseThrow();
            });
            log.info("Geolocation retrieved from the database for IP: {}", ip);
            var geolocationDto = objectMapper.readValue(geolocationEntity.getPayload(), GeolocationDto.class);
            return mapToResponseDto(geolocationDto, geolocationEntity.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchFromRemoteAndSave(String ip) {
        try {
            var geolocationDto = geolocationApiService.getLocationByIp(ip);
            dao.saveOrUpdate(ip, objectMapper.writeValueAsString(geolocationDto));
            log.info("Geolocation for IP {} was received from ip-api.com and stored in the database", ip);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @InTransaction
    @Override
    public List<String> refreshDeprecated() {
        List<String> refreshedIps = dao.findDeprecated(ipExpirationPeriod);
        refreshedIps.forEach(this::fetchFromRemoteAndSave);
        return refreshedIps;
    }

    private GeolocationResponseDto mapToResponseDto(GeolocationDto dto, Instant time) {
        var result = new GeolocationResponseDto();
        result.setCity(dto.getCity());
        result.setCountry(dto.getCountry());
        result.setCountryCode(dto.getCountryCode());
        result.setIsp(dto.getIsp());
        result.setLat(dto.getLat());
        result.setLon(dto.getLon());
        result.setOrg(dto.getOrg());
        result.setQuery(dto.getQuery());
        result.setRegion(dto.getRegion());
        result.setRegionName(dto.getRegionName());
        result.setStatus(dto.getStatus());
        result.setTimezone(dto.getTimezone());
        result.setZip(dto.getZip());
        result.setTimestamp(time);
        return result;
    }


}
