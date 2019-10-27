package com.application.queryhandler;

import com.domain.pm.datatransferobject.PM25ReportResponse;
import com.domain.robot.model.RobotStatus;
import java.time.Duration;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class PollutionCollectorQueryHandler
{
    private final Integer stopIntervalInMeters;
    private final RobotCollectorQueryHandler robotCollectorQueryHandler;


    public PollutionCollectorQueryHandler(
        @Value("${report.interval.seconds}") Integer stopIntervalInMeters,
        RobotCollectorQueryHandler robotCollectorQueryHandler)
    {
        this.stopIntervalInMeters = stopIntervalInMeters;
        this.robotCollectorQueryHandler = robotCollectorQueryHandler;
    }


    public Flux<PM25ReportResponse> query()
    {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(stopIntervalInMeters));
        Flux<PM25ReportResponse> events = Flux
            .fromStream(Stream.generate(
                robotCollectorQueryHandler::getReport));
        return Flux.zip(events, interval, (key, value) -> key).takeWhile(it -> it.getRobotStatus() != RobotStatus.FINISHED);
    }
}
