package com.application.commandhandler;

import com.domain.exception.PollutionCollectorException;
import com.domain.pollutioncollector.command.ReroutePollutionCollectorCommand;
import com.domain.pollutioncollector.command.StartPollutionCollectorCommand;
import com.domain.pollutioncollector.command.StopPollutionCollectorCommand;
import com.domain.pollutioncollector.event.PollutionCollectorStartedEvent;
import com.domain.robot.model.RobotCollector;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.maps.model.LatLng;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollutionCollectorCommandHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(PollutionCollectorCommandHandler.class);
    private final EventBus eventBus;


    public void handle(final StartPollutionCollectorCommand command)
    {
        Preconditions.checkNotNull(command.getRobotName());
        Preconditions.checkNotNull(command.getEncodedPolylineRoute());
        Preconditions.checkNotNull(command.getSpeedInMetersPerSecond());
        Preconditions.checkNotNull(command.getStopIntervalInKm());

        Preconditions.checkArgument(!command.getEncodedPolylineRoute().decodePath().isEmpty(), "route cannot be empty");

        List<LatLng> route = command.getEncodedPolylineRoute().decodePath();

        RobotCollector robotCollector = RobotCollector.loadRobot(
            command.getRobotName(),
            route.get(0),
            command.getStopIntervalInKm(),
            command.getSpeedInMetersPerSecond());

        eventBus.post(PollutionCollectorStartedEvent.builder().robotCollector(robotCollector).route(route).build());
    }


    public void handle(final ReroutePollutionCollectorCommand command)
    {
        Preconditions.checkNotNull(command.getEncodedPolylineRoute(), "route cannot be null");
        Preconditions.checkArgument(!command.getEncodedPolylineRoute().decodePath().isEmpty(), "route cannot be empty");

        LOG.info("Re-reouting..");
        List<LatLng> route = command.getEncodedPolylineRoute().decodePath();

        RobotCollector currentRobot = RobotCollector.getCurrentRobot()
            .orElseThrow(() -> new PollutionCollectorException("There is a not any robot created"));

        currentRobot.stop();

        currentRobot.restart(route.get(0));

        LOG.info("Re-reouting starting..");
        eventBus.post(PollutionCollectorStartedEvent.builder().robotCollector(currentRobot).route(route).build());
    }


    public void handle(final StopPollutionCollectorCommand command)
    {
        RobotCollector
            .getCurrentRobot()
            .orElseThrow(() -> new PollutionCollectorException("There is not any robot"))
            .stop();

    }
}
