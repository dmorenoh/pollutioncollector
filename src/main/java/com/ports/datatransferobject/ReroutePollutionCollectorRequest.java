package com.ports.datatransferobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReroutePollutionCollectorRequest
{
    private String polylineEncoded;
}
