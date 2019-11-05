package com.application.queryhandler;

import com.domain.exception.PollutionCollectorException;
import com.domain.pm.datatransferobject.PM25ReportResponse;
import com.domain.pm.model.PM25Report;
import com.domain.pm.repository.PMValueRepository;
import com.domain.robot.model.RobotCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RobotCollectorQueryHandler
{
    private final PMValueRepository repository;


    public PM25ReportResponse getReport()
    {
        PM25Report currentReport = repository.getCurrentReport();

        RobotCollector currentRobot = RobotCollector.getCurrentRobot()
            .orElseThrow(() -> new PollutionCollectorException("There is not any robot"));

        return PM25ReportResponse.builder()
            .level(currentReport.getLevel())
            .location(currentRobot.getCurrentPosition())
            .source("Robot")
            .robotStatus(currentRobot.getStatus())
            .timeStamp(currentReport.getTimeStamp()).build();

    }
}
