package com.reactive.pmcollector.model;

public enum PM25LevelType
{
    GOOD(0), MODERATE(51), USG(101), UNHEALTHY(151);

    private int minValue;


    PM25LevelType(int minValue)
    {
        this.minValue = minValue;
    }


    public static PM25LevelType getLevel(int value)
    {
        PM25LevelType returned = GOOD;
        for (PM25LevelType item : values())
        {
            if (item.minValue < value)
            {
                returned = item;
            }
        }
        return returned;
    }
}
