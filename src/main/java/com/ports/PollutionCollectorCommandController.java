package com.ports;

import com.application.commandhandler.PollutionCollectorCommandHandler;
import com.domain.pollutioncollector.command.ReroutePollutionCollectorCommand;
import com.domain.pollutioncollector.command.StartPollutionCollectorCommand;
import com.domain.pollutioncollector.command.StopPollutionCollectorCommand;
import com.google.maps.model.EncodedPolyline;
import com.ports.datatransferobject.ReroutePollutionCollectorRequest;
import com.ports.datatransferobject.StartPollutionCollectorRequest;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myapp")
@RequiredArgsConstructor
public class PollutionCollectorCommandController
{
    private final PollutionCollectorCommandHandler commandHandler;


    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestBody @Valid StartPollutionCollectorRequest request)
    {
        commandHandler.handle(
            StartPollutionCollectorCommand.builder()
                .id(UUID.randomUUID().toString())
                .speedInMetersPerSecond(request.getSpeed())
                .stopIntervalInKm(request.getStopInterval())
                .encodedPolylineRoute(new EncodedPolyline(request.getPolylineEncoded()))
                .robotName(request.getRobotName()).build());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @PostMapping("/stop")
    public ResponseEntity<Void> stop()
    {
        commandHandler.handle(new StopPollutionCollectorCommand());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @PostMapping("/reroute")
    public ResponseEntity<Void> reroute(@RequestBody ReroutePollutionCollectorRequest request)
    {
        commandHandler.handle(
            ReroutePollutionCollectorCommand.builder()
                .encodedPolylineRoute(new EncodedPolyline(request.getPolylineEncoded())).build()
        );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
