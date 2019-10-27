package com.infra;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig
{
    @Bean
    public EventBus getEventBus()
    {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }
}
