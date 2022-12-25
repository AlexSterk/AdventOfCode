package days;

import setup.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        int y = isTest() ? 10 : 2000000;

        // This solution assumes that there is no free spot on line y. So not a general case/solution
        int maxX = sensors.stream().map(s -> s.x() - Math.abs(y - s.y()) + s.distance()).max(Integer::compareTo).get();
        int minX = sensors.stream().map(s -> s.x() + Math.abs(y - s.y()) - s.distance()).min(Integer::compareTo).get();

        return maxX - minX;
    }

    @Override
    public Object part2() {
        long limit = 4000000;

        Set<Integer> A = new HashSet<>();
        Set<Integer> B = new HashSet<>();

        for (Sensor sensor : sensors) {
            A.add(sensor.y() - sensor.x() + sensor.distance() + 1);
            A.add(sensor.y() - sensor.x() - sensor.distance() - 1);
            B.add(sensor.x() + sensor.y() + sensor.distance() + 1);
            B.add(sensor.x() + sensor.y() - sensor.distance() - 1);
        }

        for (Integer a : A) {
            for (Integer b : B) {
                Point p = new Point((b - a) / 2, (a + b) / 2);
                if (0 < p.x() && p.x() < limit && 0 < p.y() && p.y() < limit) {
                    if (sensors.stream().allMatch(s -> s.sensor.manhattanDistance(p) > s.distance())) {
                        return p.x() * limit + p.y();
                    }
                }
            }
        }


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

    @Override
    public String partTwoSolution() {
        return "12051287042458";
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
