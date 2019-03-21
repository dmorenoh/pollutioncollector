package com.reactive.pmcollector.service;

import com.google.maps.model.LatLng;
import com.reactive.pmcollector.model.Robot;
import java.util.List;

public interface RobotRouteHandlerService
{
    void performRobotRoute(Robot robot, List<LatLng> routeMarks);
}
