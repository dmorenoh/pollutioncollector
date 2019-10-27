package com.domain.pm.repository;

import com.domain.pm.model.PMValue;
import com.domain.pm.model.PM25Report;
import java.util.List;

public interface PMValueRepository
{
    void save(PMValue value);
    List<PMValue> getAll();
    PM25Report getCurrentReport();
}
