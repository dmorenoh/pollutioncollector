package com.reactive.pmcollector.eventshandler;

import com.reactive.pmcollector.events.PollutionDataPickingEvent;

public interface PollutionDataPickingEventHandler<T extends PollutionDataPickingEvent>
{
    void handle(T event);
}
