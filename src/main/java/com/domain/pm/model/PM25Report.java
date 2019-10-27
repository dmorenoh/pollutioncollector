package com.domain.pm.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Value;

@Value
public class PM25Report
{
    long timeStamp;
    PM25LevelType level;

    public PM25Report(int average)
    {
        this.timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        this.level = PM25LevelType.getLevel(average);
    }


}
