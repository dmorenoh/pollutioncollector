package com.reactive.pmcollector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollutionDataPickerRequest
{
    private String robotName;
    private Integer speed;
    private String polylineEncoded;

}
