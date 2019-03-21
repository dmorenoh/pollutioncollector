package com.reactive.pmcollector.model;

import com.google.maps.model.LatLng;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class PM25Value
{
    @Id
    private String id;
    private int value;
    private LatLng position;
    private LocalDateTime registeredDate;


    public PM25Value(int value, LatLng position)
    {
        this.value = value;
        this.position = position;
        this.registeredDate = LocalDateTime.now();
    }


    public LatLng getPosition()
    {
        return position;
    }


    public LocalDateTime getRegisteredDate()
    {
        return registeredDate;
    }


    public int getValue()
    {
        return value;
    }


}
