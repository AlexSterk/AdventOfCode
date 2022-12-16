package days;

import setup.Day;
import util.Line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static util.Line.Point;

public class Day15 extends Day {


    private ArrayList<Sensor> sensors;
//    private ArrayList<Point> beacon;

    @Override
    public void processInput() {

        sensors = new ArrayList<>();

        var p = Pattern.compile("x=(-?\\d+), y=(-?\\d+)");
        for (String line : lines()) {
            Matcher matcher = p.matcher(line);
            matcher.find();
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));

            matcher.find();
            int x2 = Integer.parseInt(matcher.group(1));
            int y2 = Integer.parseInt(matcher.group(2));

            sensors.add(new Sensor(x, y, x2, y2));
        }
    }

    @Override
    public Object part1() {
        Set<Point> cannotContainBeacon = new HashSet<>();
        int y = isTest() ? 10 : 2000000;


        List<Point> beacons = sensors.stream().map(Sensor::beacon).toList();
        for (Sensor sensor : sensors) {
            IntStream.rangeClosed(sensor.x() - sensor.distance(), sensor.x() + sensor.distance())
                    .mapToObj(x -> new Point(x, y))
                    .filter(o -> !beacons.contains(o))
                    .filter(p -> p.manhattanDistance(sensor.sensor()) <= sensor.distance())
                    .forEach(cannotContainBeacon::add);
        }


        return cannotContainBeacon.size();
    }

    @Override
    public Object part2() {
        Set<Point> possiblePoints = new HashSet<>();
        Set<Point> visited = new HashSet<>();

        for (Sensor sensor : sensors) {

//
            int minX = sensor.x() - sensor.distance() - 1;
            int maxX = sensor.x() + sensor.distance() + 1;
            int minY = sensor.y() - sensor.distance() - 1;
            int maxY = sensor.y() + sensor.distance() + 1;
//
            Line topLeft = new Line(minX, sensor.y(), sensor.x(), minY);
            Line topRight = new Line(sensor.x(), minY, maxX, sensor.y());
            Line botRight = new Line(sensor.x(), maxY, minX, sensor.y());
            Line botLeft = new Line(maxX, sensor.y(), sensor.x(), maxY);

            for (Line line : List.of(topLeft, topRight, botRight, botLeft)) {
                System.out.println(line.expand());
                for (Point p : line.expand()) {
                    if (p.x() >= 0 && p.x() <= 4000000 && p.y() >= 0 && p.y() <= 4000000) {
                        if (visited.contains(p)) {
                            possiblePoints.remove(p);
                        } else {
                            possiblePoints.add(p);
                            visited.add(p);
                        }
                    }
                }
            }


        }

        System.out.println(possiblePoints.size());

//        System.out.println(possiblePoints.entrySet().stream().limit(10).toList());

//        System.out.println(possiblePoints.entrySet().stream().filter(e -> e.getValue() == 1).count());
//        List<Point> points = possiblePoints.entrySet().stream().filter(e -> e.getValue() == 1).map(Map.Entry::getKey).toList();
//        System.out.println(points);
//        long x = points.get(0).x();
//        long y = points.get(0).y();
//
//        return x * 4000000 + y;

        return null;
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public String partOneSolution() {
        return "4725496";
    }

    private record Sensor(Point sensor, Point beacon) {
        public Sensor(int x, int y, int x2, int y2) {
            this(new Point(x, y), new Point(x2, y2));
        }

        public int x() {
            return sensor.x();
        }

        public int y() {
            return sensor.y();
        }

        public int distance() {
            return sensor.manhattanDistance(beacon);
        }
    }


}
