package com.reactive.pmcollector.commandhandler;

import com.reactive.pmcollector.model.PM25Report;
import com.reactive.pmcollector.model.Robot;
import java.time.Duration;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetPM25ReportQuery
{

    public Flux<PM25Report> query()
    {
        Robot robot = Robot.getInstance();

        Flux<Long> interval = Flux.interval(Duration.ofSeconds(10));
        Flux<PM25Report> events = Flux
            .fromStream(Stream.generate(
                robot::getPM25Report));

        return Flux.zip(events, interval, (key, value) -> key);
    }
}
