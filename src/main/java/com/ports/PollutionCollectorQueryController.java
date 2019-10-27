package com.ports;

import com.application.queryhandler.PollutionCollectorQueryHandler;
import com.domain.pm.datatransferobject.PM25ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/myapp")
@RequiredArgsConstructor
public class PollutionCollectorQueryController
{

    private final PollutionCollectorQueryHandler queryHandler;

    @GetMapping(value = "/reports", produces = "text/event-stream")
    public Flux<PM25ReportResponse> createReport()
    {
        return queryHandler.query();
    }
}
