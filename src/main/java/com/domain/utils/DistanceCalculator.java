package com.domain.utils;

import com.google.maps.model.LatLng;
import java.text.DecimalFormat;

public class DistanceCalculator
{
    private static final double EARTH_RADIUS = 6371;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.000");


    private DistanceCalculator()
    {
    }


    public static GreatCircleDistance getGreatCircleDistance(LatLng origin, LatLng destination)
    {
        return new GreatCircleDistance(origin, destination, distance(origin, destination), bearing(origin, destination));
    }


    public static LatLng getDestination(LatLng startPoint, double bearing, double distance)
    {
        double bearingAsRadians = Math.toRadians(bearing);
        double dist = distance / EARTH_RADIUS;
        double lat1 = Math.toRadians(startPoint.lat);
        double lng1 = Math.toRadians(startPoint.lng);

        double destinationLatitude = Math.asin(Math.sin(lat1) * Math.cos(dist) +
            Math.cos(lat1) * Math.sin(dist) * Math.cos(bearingAsRadians));

        double destinationLongitude = lng1 + Math.atan2(
            Math.sin(bearingAsRadians) * Math.sin(dist) * Math.cos(lat1),
            Math.cos(dist) - Math.sin(lat1) * Math.sin(destinationLatitude));

        return new LatLng(Math.toDegrees(destinationLatitude), Math.toDegrees(destinationLongitude));
    }


    private static double bearing(LatLng origin, LatLng destination)
    {
        double longDiffAsRadians = Math.toRadians(destination.lng - origin.lng);

        double y = Math.sin(longDiffAsRadians) * Math.cos(Math.toRadians(destination.lat));

        double x = Math.cos(Math.toRadians(origin.lat)) * Math.sin(Math.toRadians(destination.lat)) -
            Math.sin(Math.toRadians(origin.lat)) * Math.cos(Math.toRadians(destination.lat)) * Math.cos(longDiffAsRadians);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }


    public static double distance(final LatLng origin, final LatLng destination)
    {
        double latDistance = Math.toRadians(destination.lat - origin.lat);
        double lonDistance = Math.toRadians(destination.lng - origin.lng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(destination.lat)) * Math.cos(Math.toRadians(destination.lat))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Double.valueOf(DECIMAL_FORMAT.format(EARTH_RADIUS * c));
    }
}
