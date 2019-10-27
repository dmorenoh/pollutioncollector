package com.domain.utils;

import com.google.maps.model.LatLng;
import lombok.Value;

@Value
public class GreatCircleDistance
{
    LatLng initialPoint;
    LatLng endPoint;
    double distanceKm;
    double initialBearing;
    double finalBearing;

}
