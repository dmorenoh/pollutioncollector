package com.reactive.pmcollector.eventshandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.reactive.pmcollector.events.PollutionDataPickingReadyEvent;
import com.reactive.pmcollector.service.RobotRouteHandlerService;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PollutionDataPickingReadyEventHandler implements PollutionDataPickingEventHandler<PollutionDataPickingReadyEvent>
{
    private final EventBus eventBus;
    private final RobotRouteHandlerService robotRouteHandlerService;


    public PollutionDataPickingReadyEventHandler(
        final EventBus eventBus,
        final RobotRouteHandlerService robotRouteHandlerService)
    {
        this.eventBus = eventBus;
        this.robotRouteHandlerService = robotRouteHandlerService;
    }


    @PostConstruct
    public void init()
    {
        eventBus.register(this);
    }


    @Override
    @Subscribe
    public void handle(PollutionDataPickingReadyEvent event)
    {
        robotRouteHandlerService.performRobotRoute(
            event.getReadyRobot(),
            event.getPolylineRoute().decodePath());
    }
}
