package com.data;

import com.domain.pm.model.PMValue;
import java.util.ArrayList;
import java.util.List;

public class PMValuesInMemory
{
    private List<PMValue> pmValues;
    private static PMValuesInMemory INSTANCE = null;


    public static PMValuesInMemory getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new PMValuesInMemory();
        }
        return INSTANCE;
    }


    public void save(final PMValue value)
    {
        this.pmValues.add(value);
    }


    public List<PMValue> getAll()
    {
        return this.pmValues;
    }


    private PMValuesInMemory()
    {
        pmValues = new ArrayList<>();
    }
}
