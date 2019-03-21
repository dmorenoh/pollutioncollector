package com.reactive.pmcollector.events;

import com.google.maps.model.EncodedPolyline;
import com.reactive.pmcollector.model.Robot;

public class PollutionDataPickingReadyEvent extends PollutionDataPickingEvent
{
    private final EncodedPolyline polylineRoute;
    private final Robot readyRobot;


    public static PollutionDataPickingReadyEvent of(
        final Robot robot,
        final EncodedPolyline polylineRoute)
    {
        return new PollutionDataPickingReadyEvent(polylineRoute, robot);
    }


    private PollutionDataPickingReadyEvent(EncodedPolyline polylineRoute, Robot readyRobot)
    {
        this.polylineRoute = polylineRoute;
        this.readyRobot = readyRobot;
    }


    public Robot getReadyRobot()
    {
        return readyRobot;
    }


    public EncodedPolyline getPolylineRoute()
    {
        return polylineRoute;
    }
}
