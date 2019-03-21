package com.reactive.pmcollector.commandhandler;

import com.google.common.eventbus.EventBus;
import com.reactive.pmcollector.command.StartDataPickingCommand;
import com.reactive.pmcollector.events.PollutionDataPickingReadyEvent;
import com.reactive.pmcollector.model.Robot;
import org.springframework.stereotype.Component;

import static com.reactive.pmcollector.model.Robot.newReadyRobot;

@Component
public class StartDataPickingCommandHandler
{
    private final EventBus eventBus;


    public StartDataPickingCommandHandler(EventBus eventBus)
    {
        this.eventBus = eventBus;
    }


    public void handle(final StartDataPickingCommand command)
    {
        final Robot readyRobot = newReadyRobot(
            command.getRobotName(),
            command.getRobotSpeed(),
            command.getEncodedPolylineRoute().decodePath().get(0));

        final PollutionDataPickingReadyEvent pollutionCollectionStartedEvent = PollutionDataPickingReadyEvent.of(
            readyRobot,
            command.getEncodedPolylineRoute());

        eventBus.post(pollutionCollectionStartedEvent);
    }
}
