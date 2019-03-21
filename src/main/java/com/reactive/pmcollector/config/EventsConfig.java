package com.reactive.pmcollector.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfig
{
    @Bean
    public EventBus getEventBus()
    {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }
}
