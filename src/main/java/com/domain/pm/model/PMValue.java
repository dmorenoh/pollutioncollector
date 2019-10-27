package com.domain.pm.model;

import com.google.maps.model.LatLng;
import java.time.LocalDateTime;
import lombok.Value;

@Value
public class PMValue
{
    int value;
    LatLng position;
    LocalDateTime registeredDate;
}
