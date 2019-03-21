package com.reactive.pmcollector.repository;

import com.reactive.pmcollector.model.PM25Value;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PM25ValuesRepository extends ReactiveCrudRepository<PM25Value, String>
{

}
