package days;

import setup.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static util.Line.Point;

public class Day15 extends Day {


    private ArrayList<Point> sensors;
    private ArrayList<Point> beacons;

    @Override
    public void processInput() {

        sensors = new ArrayList<>();
        beacons = new ArrayList<>();

        var p = Pattern.compile("x=(-?\\d+), y=(-?\\d+)");
        for (String line : lines()) {
            Matcher matcher = p.matcher(line);
            matcher.find();
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));

            sensors.add(new Point(x, y));
            matcher.find();
            x = Integer.parseInt(matcher.group(1));
            y = Integer.parseInt(matcher.group(2));

            beacons.add(new Point(x, y));
        }
    }

    @Override
    public Object part1() {
        Set<Point> cannotContainBeacon = new HashSet<>();
        int y = isTest() ? 10 : 2000000;

        int minX, maxX;
//        minX  = Integer.MAX_VALUE;
//        maxX  = Integer.MIN_VALUE;
//
//
//        for (Point sensor : sensors) {
//            int closest = beacons.stream().map(sensor::manhattanDistance).min(Integer::compareTo).get();
//
//            minX = Math.min(minX, sensor.x() - closest);
//            maxX = Math.max(maxX, sensor.x() + closest);
//
//        }

        for (Point sensor : sensors) {
            int closest = beacons.stream().map(sensor::manhattanDistance).min(Integer::compareTo).get();
            IntStream.rangeClosed(sensor.x() - closest, sensor.x() + closest)
                    .mapToObj(x -> new Point(x, y))
                    .filter(p -> !beacons.contains(p))
                    .filter(p -> p.manhattanDistance(sensor) <= closest)
                    .forEach(cannotContainBeacon::add);
        }


        return cannotContainBeacon.size();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "4725496";
    }
}
