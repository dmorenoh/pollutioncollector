package com.reactive.pmcollector.model;

import com.google.maps.model.LatLng;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PM25Report
{
    private final long timeStamp;
    private final LatLng location;
    private final PM25LevelType level;
    private final String source;


    public PM25Report(LocalDateTime timeStamp, LatLng location, int average, String source)
    {
        this.timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        this.location = location;
        this.level = PM25LevelType.getLevel(average);
        this.source = source;
    }


    public LatLng getLocation()
    {
        return location;
    }


    public long getTimeStamp()
    {
        return timeStamp;
    }


    public PM25LevelType getLevel()
    {
        return level;
    }


    public String getSource()
    {
        return source;
    }

}
