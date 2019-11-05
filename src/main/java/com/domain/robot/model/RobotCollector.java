package com.domain.robot.model;

import com.domain.exception.PollutionCollectorException;
import com.domain.pm.model.PMValue;
import com.domain.utils.GreatCircleDistance;
import com.google.maps.model.LatLng;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.domain.utils.DistanceCalculator.getDestination;

@Data
public class RobotCollector
{
    private static final Logger LOG = LoggerFactory.getLogger(RobotCollector.class);

    private final String name;

    private RobotStatus status;

    private LatLng currentPosition;

    private final double stopDistanceInterval;

    private final double speedAsMtrsPerSecond;

    private double distanceWithoutStopping;

    private static RobotCollector INSTANCE = null;


    public static void clear()
    {
        INSTANCE = null;
    }


    public static Optional<RobotCollector> getCurrentRobot()
    {
        return Optional.ofNullable(INSTANCE);
    }


    public static RobotCollector loadRobot(
        final String name,
        final LatLng startPosition,
        double stopDistanceInterval,
        double speedAsMtrsPerSecond)
    {
        if (INSTANCE != null)
        {
            throw new PollutionCollectorException("There is a robot already created");
        }
        INSTANCE = new RobotCollector(
            name,
            RobotStatus.STARTED,
            startPosition,
            stopDistanceInterval,
            speedAsMtrsPerSecond,
            0);
        return INSTANCE;
    }


    private RobotCollector(
        final String name,
        final RobotStatus status,
        final LatLng currentPosition,
        final double stopDistanceInterval,
        final double speedAsMtrsPerSecond,
        final double distanceWithoutStopping)
    {
        this.name = name;
        this.status = status;
        this.currentPosition = currentPosition;
        this.stopDistanceInterval = stopDistanceInterval;
        this.speedAsMtrsPerSecond = speedAsMtrsPerSecond;
        this.distanceWithoutStopping = distanceWithoutStopping;
    }


    public PMValue readPollutionValue()
    {
        final Random random = new Random();
        int pm = random.nextInt(200);
        return new PMValue(pm, this.currentPosition, LocalDateTime.now());
    }


    public synchronized void move(final GreatCircleDistance greatCircleDistance)
    {
        if (this.isFinished())
        {
            throw new PollutionCollectorException("robot stopped");
        }
        double distanceInMeters = Math.round(greatCircleDistance.getDistanceKm() * 1000);
        double delayedSeconds = distanceInMeters / speedAsMtrsPerSecond;
        try
        {
            Thread.sleep((long) delayedSeconds * 1000);
        }
        catch (InterruptedException e)
        {
            throw new PollutionCollectorException("error while moving");
        }
        this.currentPosition = greatCircleDistance.getEndPoint();
    }


    public void moveWithoutStopping(GreatCircleDistance distance)
    {
        this.move(distance);
        this.distanceWithoutStopping = distanceWithoutStopping + distance.getDistanceKm();
    }


    public void moveToCollectionStop(double initialBearing)
    {
        double distance = this.stopDistanceInterval - this.distanceWithoutStopping;
        final LatLng nextStop = getDestination(this.currentPosition, initialBearing, distance);

        GreatCircleDistance greatCircleDistance = GreatCircleDistance.builder()
            .distanceKm(distance)
            .initialPoint(this.currentPosition)
            .endPoint(nextStop).build();

        this.move(greatCircleDistance);

        this.distanceWithoutStopping = 0;
    }


    public boolean hasStopCollectionInTheWay(double distanceToWalk)
    {
        return (distanceToWalk + this.distanceWithoutStopping) >= this.stopDistanceInterval;
    }


    public boolean currentPositionIs(final LatLng position)
    {
        return this.getCurrentPosition().equals(position);
    }


    public synchronized void restart(final LatLng startPoint)
    {
        this.currentPosition = startPoint;
        this.status = RobotStatus.STARTED;
    }


    public synchronized void stop()
    {
        LOG.info("Stopping");
        this.status = RobotStatus.FINISHED;
        notifyAll();
    }


    public boolean isStarted()
    {
        return this.status == RobotStatus.STARTED;
    }


    public boolean isFinished()
    {
        return this.status == RobotStatus.FINISHED;
    }


    public LatLng getCollectionStopPosition(double bearing)
    {
        double distance = this.stopDistanceInterval - this.distanceWithoutStopping;
        return getDestination(this.currentPosition, bearing, distance);
    }
}
