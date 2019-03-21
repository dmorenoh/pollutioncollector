package com.reactive.pmcollector.controller;

import com.google.maps.model.EncodedPolyline;
import com.reactive.pmcollector.command.StartDataPickingCommand;
import com.reactive.pmcollector.commandhandler.GetPM25ReportQuery;
import com.reactive.pmcollector.commandhandler.StartDataPickingCommandHandler;
import com.reactive.pmcollector.dto.PollutionDataPickerRequest;
import com.reactive.pmcollector.model.PM25Report;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/myapp")
public class PM25CollectorController
{
    private final StartDataPickingCommandHandler startDataPickingCommandHandler;
    private final GetPM25ReportQuery getPM25ReportQuery;


    public PM25CollectorController(
        final StartDataPickingCommandHandler startDataPickingCommandHandler,
        final GetPM25ReportQuery getPM25ReportQuery)
    {
        this.getPM25ReportQuery = getPM25ReportQuery;
        this.startDataPickingCommandHandler = startDataPickingCommandHandler;
    }


    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestBody PollutionDataPickerRequest pollutionDataPickerRequest)
    {
        StartDataPickingCommand startDataPickingCommand = new StartDataPickingCommand(
            pollutionDataPickerRequest.getRobotName(),
            pollutionDataPickerRequest.getSpeed(),
            new EncodedPolyline(pollutionDataPickerRequest.getPolylineEncoded()));
        startDataPickingCommandHandler.handle(startDataPickingCommand);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/reports", produces = "text/event-stream")
    public Flux<PM25Report> createReport()
    {
        return getPM25ReportQuery.query();

    }
}
