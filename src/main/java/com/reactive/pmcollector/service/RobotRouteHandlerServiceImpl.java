package com.reactive.pmcollector.service;

import com.google.maps.model.LatLng;
import com.reactive.pmcollector.model.Robot;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RobotRouteHandlerServiceImpl implements RobotRouteHandlerService
{
    private final Integer stopIntervalInMeters;


    public RobotRouteHandlerServiceImpl(
        @Value("${stop.intervals.meters}") Integer stopIntervalInMeters)
    {
        this.stopIntervalInMeters = stopIntervalInMeters;
    }


    @Override
    public void performRobotRoute(final Robot robot, List<LatLng> routeMarks)
    {
        Objects.requireNonNull(robot.getCurrentPosition(), "Robot should be initialized");
        Objects.requireNonNull(routeMarks, "Routes should be informed");

        routeMarks.forEach(routeMark -> moveRobot(robot, routeMark));
    }


    private void moveRobot(Robot robot, LatLng destination)
    {
        try
        {
            robot.moveToPoint(destination);
            if (robot.getNonStopTraveledDistance() >= stopIntervalInMeters)
            {
                collectPMValues(robot);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    private void collectPMValues(final Robot robot)
    {
        int stops = (int) (robot.getNonStopTraveledDistance() / stopIntervalInMeters);
        for (int itr = 0; itr < stops; itr++)
        {
            robot.pickPM25Value();
        }
        robot.setNonStopTraveledDistance(robot.getNonStopTraveledDistance() % stopIntervalInMeters);
    }
}
