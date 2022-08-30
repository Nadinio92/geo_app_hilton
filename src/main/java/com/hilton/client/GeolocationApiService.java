package com.hilton.client;

import com.hilton.client.dto.GeolocationDto;

public interface GeolocationApiService {
    GeolocationDto getLocationByIp(String ip);
}
