package com.ports.datatransferobject;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartPollutionCollectorRequest
{
    @NotNull
    private String robotName;
    @NotNull
    private Double speed;
    @NotNull
    private Double stopInterval;
    @NotNull
    private String polylineEncoded;

}
