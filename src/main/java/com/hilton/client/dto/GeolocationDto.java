package com.hilton.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GeolocationDto {
    private String query;
    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private double lat;
    private double lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
}
