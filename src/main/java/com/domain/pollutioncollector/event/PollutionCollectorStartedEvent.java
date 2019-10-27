package com.domain.pollutioncollector.event;

import com.domain.robot.model.RobotCollector;
import com.google.maps.model.LatLng;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PollutionCollectorStartedEvent
{
    List<LatLng> route;
    RobotCollector robotCollector;
}
