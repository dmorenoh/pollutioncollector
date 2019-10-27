package com.domain.pollutioncollector.command;

import com.google.maps.model.EncodedPolyline;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StartPollutionCollectorCommand
{
    String id;
    String robotName;
    Double speedInMetersPerSecond;
    Double stopIntervalInKm;
    EncodedPolyline encodedPolylineRoute;
}
