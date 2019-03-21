package com.reactive.pmcollector.model;

import com.google.maps.model.LatLng;
import org.junit.Test;

import static com.reactive.pmcollector.utils.DistanceCalculator.distance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RobotTest
{
    @Test
    public void shouldMoveRobotWhenDestinationProvided() throws InterruptedException
    {
        LatLng start = new LatLng(41.3752752, 2.146514);
        LatLng end = new LatLng(41.375360, 2.148719);

        double distanceInMeters = Math.round(distance(start, end));

        Robot robot = Robot.newReadyRobot("name", 100, start);

        robot.moveToPoint(end);
        assertThat(robot.getCurrentPosition(), equalTo(end));
        assertThat(robot.getNonStopTraveledDistance(), equalTo(distanceInMeters));
    }
}