package com.reactive.pmcollector.model;

import com.google.maps.model.LatLng;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;

import static com.reactive.pmcollector.utils.DistanceCalculator.distance;

@Data
public class Robot
{
    private final String name;
    private final int speedAsMtrsPerSecond;
    private LatLng currentPosition;
    private double nonStopTraveledDistance;
    private RobotStatus robotStatus;
    private static Robot INSTANCE = null;
    private List<PM25Value> pm25ValueList;


    public static Robot newReadyRobot(
        final String name,
        final int speedAsMtrsPerSecond,
        final LatLng startingPosition)
    {
        INSTANCE = new Robot(name, speedAsMtrsPerSecond, startingPosition, 0, RobotStatus.READY);
        return INSTANCE;
    }


    public static Robot getInstance()
    {
        return INSTANCE;
    }


    private Robot(
        final String name,
        final int speedAsMtrsPerSecond,
        final LatLng currentPosition,
        final double nonStopTraveledDistance,
        final RobotStatus robotStatus)
    {
        this.name = name;
        this.speedAsMtrsPerSecond = speedAsMtrsPerSecond;
        this.currentPosition = currentPosition;
        this.nonStopTraveledDistance = nonStopTraveledDistance;
        this.robotStatus = robotStatus;
        this.pm25ValueList = new ArrayList<>();
    }


    public String getName()
    {
        return name;
    }


    public void setNonStopTraveledDistance(double nonStopTraveledDistance)
    {
        this.nonStopTraveledDistance = nonStopTraveledDistance;
    }


    public void setCurrentPosition(LatLng currentPosition)
    {
        this.currentPosition = currentPosition;
    }


    public double getNonStopTraveledDistance()
    {
        return nonStopTraveledDistance;
    }


    public LatLng getCurrentPosition()
    {
        return currentPosition;
    }


    public PM25Value pickPM25Value()
    {
        final Random random = new Random();
        int pm = random.nextInt(200);
        PM25Value pm25Value = new PM25Value(pm, this.currentPosition);
        pm25ValueList.add(pm25Value);
        System.out.println("Collecting " + pm);
        return pm25Value;
    }


    public void moveToPoint(LatLng destination) throws InterruptedException
    {
        if (currentPosition.equals(destination))
        {
            return;
        }

        double distanceInMeters = Math.round(distance(currentPosition, destination));
        double delayedSeconds = distanceInMeters / (double) speedAsMtrsPerSecond;
        double millis = delayedSeconds * 1000;
        System.out.println("Robot moving " + distanceInMeters);
        Thread.sleep((long) millis);

        currentPosition = destination;

        nonStopTraveledDistance = nonStopTraveledDistance + distanceInMeters;
    }


    public PM25Report getPM25Report()
    {
        if (this.pm25ValueList.isEmpty())
        {
            return new PM25Report(LocalDateTime.now(), this.currentPosition, 0, this.name);
        }
        System.out.println("Values size " + pm25ValueList.size());
        double average = pm25ValueList.stream().mapToInt(PM25Value::getValue).average().getAsDouble();
        System.out.println("Avg " + average);
        return new PM25Report(LocalDateTime.now(), this.currentPosition, (int) Math.round(average), this.name);
    }

}

