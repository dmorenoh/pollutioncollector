package com.reactive.pmcollector.service;

import com.google.maps.model.LatLng;
import com.reactive.pmcollector.model.Robot;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.reactive.pmcollector.utils.DistanceCalculator.distance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class RobotRouteHandlerServiceImplTest
{

    private static final int STOP_INTERVAL_IN_METERS = 200;

    private RobotRouteHandlerServiceImpl robotRouteHandlerService;

    private Robot spyRobot;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setup()
    {
        robotRouteHandlerService = new RobotRouteHandlerServiceImpl(STOP_INTERVAL_IN_METERS);
    }


    @Test
    public void shouldFailWhenRobotIsNotInitialized()
    {
        final Robot robot = Robot.newReadyRobot("name", 100, null);

        expectedException.expect(NullPointerException.class);
        robotRouteHandlerService.performRobotRoute(robot, Collections.emptyList());
    }


    @Test
    public void shouldMoveRobotWhenRouteInformed()
    {
        LatLng start = new LatLng(41.3752752, 2.146514);
        LatLng end = new LatLng(41.375360, 2.148719);

        final Robot robot = Robot.newReadyRobot("name", 100, start);

        List<LatLng> routeMarks = Arrays.asList(
            start,
            end);
        spyRobot = spy(robot);
        robotRouteHandlerService.performRobotRoute(spyRobot, routeMarks);
        assertTrue(Math.round(distance(start, end)) < STOP_INTERVAL_IN_METERS);
        assertThat(spyRobot.getCurrentPosition(), equalTo(end));
        assertTrue(spyRobot.getPm25ValueList().isEmpty());
    }


    @Test
    public void shouldMoveRobotWhenRouteInformedAndPickupData()
    {
        LatLng start = new LatLng(41.375356, 2.147716);
        LatLng end = new LatLng(41.374695, 2.170183);

        final Robot robot = Robot.newReadyRobot("name", 100, start);

        List<LatLng> routeMarks = Arrays.asList(
            start,
            end);
        spyRobot = spy(robot);
        robotRouteHandlerService.performRobotRoute(spyRobot, routeMarks);
        assertTrue(Math.round(distance(start, end)) > STOP_INTERVAL_IN_METERS);
        assertThat(spyRobot.getCurrentPosition(), equalTo(end));
        assertFalse(spyRobot.getPm25ValueList().isEmpty());
        assertTrue(spyRobot.getPm25ValueList().size() > 0);
    }
}