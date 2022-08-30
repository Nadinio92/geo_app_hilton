package com.hilton.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Getter @Setter
public class GeolocationEntity {
    String ip;
    String payload;
    Instant time;
}
