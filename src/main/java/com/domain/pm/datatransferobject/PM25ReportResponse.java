package com.domain.pm.datatransferobject;

import com.domain.pm.model.PM25LevelType;
import com.domain.robot.model.RobotStatus;
import com.google.maps.model.LatLng;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PM25ReportResponse
{
    long timeStamp;
    LatLng location;
    PM25LevelType level;
    String source;
    RobotStatus robotStatus;
}
