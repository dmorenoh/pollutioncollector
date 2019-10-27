package com.application.commandhandler;

import com.domain.pm.repository.PMValueRepository;
import com.domain.robot.command.MoveRobotCommand;
import com.domain.robot.model.RobotCollector;
import com.domain.utils.GreatCircleDistance;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.domain.utils.DistanceCalculator.getGreatCircleDistance;

@Component
@RequiredArgsConstructor
public class RobotCollectorCommandHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(RobotCollectorCommandHandler.class);

    private final PMValueRepository repository;


    public void handle(final MoveRobotCommand command)
    {
        RobotCollector robotCollector = command.getRobotCollector();

        LOG.info("Current robot status:{}", robotCollector.getStatus());
        while (!robotCollector.currentPositionIs(command.getDestination()))
        {
            GreatCircleDistance distanceToNextPosition = getGreatCircleDistance(
                robotCollector.getCurrentPosition(),
                command.getDestination());

            LOG.info("Distance to walk:{}", distanceToNextPosition.getDistanceKm());
            if (distanceToNextPosition.getDistanceKm() + robotCollector.getDistanceWithoutStopping() >= robotCollector.getStopDistanceInterval())
            {
                robotCollector.moveToCollectionStop(distanceToNextPosition.getInitialBearing());
                repository.save(robotCollector.readPollutionValue());
            }
            else
            {
                robotCollector.moveWithoutStopping(command.getDestination());
            }

        }
    }

}
