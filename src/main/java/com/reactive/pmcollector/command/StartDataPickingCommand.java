package com.reactive.pmcollector.command;

import com.google.maps.model.EncodedPolyline;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StartDataPickingCommand
{
    private final String robotName;
    private final int robotSpeed;
    private final EncodedPolyline encodedPolylineRoute;

}
