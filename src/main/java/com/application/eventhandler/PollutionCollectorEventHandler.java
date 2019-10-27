package com.application.eventhandler;

import com.application.commandhandler.RobotCollectorCommandHandler;
import com.domain.robot.command.MoveRobotCommand;
import com.domain.pollutioncollector.event.PollutionCollectorStartedEvent;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollutionCollectorEventHandler
{
    private final EventBus eventBus;

    private final RobotCollectorCommandHandler robotCollectorCommandHandler;


    @PostConstruct
    public void init()
    {
        eventBus.register(this);
    }


    @Subscribe
    public void handle(final PollutionCollectorStartedEvent event)
    {
        Preconditions.checkNotNull(event.getRobotCollector(), "Robot cannot be null");
        Preconditions.checkArgument(event.getRobotCollector().isStarted(), "Robot is not started");
        Preconditions.checkNotNull(event.getRoute(), "Robot cannot be null ");
        Preconditions.checkArgument(!event.getRoute().isEmpty(), "route cannot be empty");

        event.getRoute().forEach(it -> robotCollectorCommandHandler.handle(new MoveRobotCommand(event.getRobotCollector(), it)));
        event.getRobotCollector().stop();
    }


}
