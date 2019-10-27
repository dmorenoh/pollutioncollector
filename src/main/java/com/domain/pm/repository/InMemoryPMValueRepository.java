package com.domain.pm.repository;

import com.data.PMValuesInMemory;
import com.domain.pm.model.PMValue;
import com.domain.pm.model.PM25Report;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InMemoryPMValueRepository implements PMValueRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryPMValueRepository.class);


    @Override
    public void save(PMValue value)
    {
        PMValuesInMemory instance = PMValuesInMemory.getInstance();
        instance.save(value);
    }


    @Override
    public List<PMValue> getAll()
    {
        PMValuesInMemory instance = PMValuesInMemory.getInstance();
        return instance.getAll();
    }


    @Override
    public PM25Report getCurrentReport()
    {
        PMValuesInMemory instance = PMValuesInMemory.getInstance();

        LOG.info("Values size " + instance.getAll().size());

        double average = instance.getAll().stream()
            .mapToInt(PMValue::getValue)
            .average()
            .orElse(-1.0);

        LOG.info("AVG " + average);
        return new PM25Report((int) Math.round(average));
    }


}
