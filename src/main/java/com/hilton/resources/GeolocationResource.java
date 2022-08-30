package com.hilton.resources;


import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.hilton.api.GeolocationResponseDto;
import com.hilton.core.GeolocationService;
import com.hilton.utils.Ipv4;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/v1/geolocation/")
@Produces(MediaType.APPLICATION_JSON)
public class GeolocationResource {

    @Inject
    private GeolocationService geolocationService;

    @GET
    @Timed
    public GeolocationResponseDto getGeolocation(@QueryParam("ip")
                                                 @Ipv4(message = "Incorrect Ipv4 IP address") String ip) {
        return geolocationService.getGeolocationByIp(ip);
    }

}
