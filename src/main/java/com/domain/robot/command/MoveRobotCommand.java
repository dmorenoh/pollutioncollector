package com.domain.robot.command;

import com.domain.robot.model.RobotCollector;
import com.google.maps.model.LatLng;
import lombok.Value;

@Value
public class MoveRobotCommand
{
    RobotCollector robotCollector;
    LatLng destination;
}
