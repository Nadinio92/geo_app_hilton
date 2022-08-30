package com.hilton.core;

import com.hilton.api.GeolocationResponseDto;

import java.util.List;

public interface GeolocationService {
    GeolocationResponseDto getGeolocationByIp(String ip);

    List<String> refreshDeprecated();
}


