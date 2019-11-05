package com.domain.utils;

import com.google.maps.model.LatLng;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GreatCircleDistance
{
    LatLng initialPoint;
    LatLng endPoint;
    double distanceKm;
    double initialBearing;

}
