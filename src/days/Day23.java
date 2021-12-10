package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day23 extends Day {

    private List<Nanobot> nanobots;

    @Override
    public void processInput() {
        nanobots = Arrays.stream(input.split("\n")).map(Nanobot::Nanobot).toList();
    }

    @Override
    public Object part1() {
        Nanobot strongest = Collections.max(nanobots, Comparator.comparing(nanobot -> nanobot.radius));
        return nanobots.stream().filter(n -> n.inRange(strongest)).count();
    }

    @Override
    public Object part2() {
        TreeMap<Integer, Integer> ranges = new TreeMap<>();
        Nanobot origin = new Nanobot(0, 0, 0, 0);
        for (Nanobot n : nanobots) {
            int originDistance = n.distanceTo(origin);
            ranges.put(Math.max(0, originDistance - n.radius), 1);
            ranges.put(originDistance + n.radius, -1);
        }
        int count = 0, maxCount = 0, result = 0;
        for (Map.Entry<Integer, Integer> each : ranges.entrySet()) {
            count += each.getValue();
            if (count > maxCount) {
                result = each.getKey();
                maxCount = count;
            }
        }

        return result;
    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Nanobot(int x, int y, int z, int radius) {
        private static final Pattern BOT_PATTERN = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

        private static Nanobot Nanobot(String s) {
            Matcher matcher = BOT_PATTERN.matcher(s);
            matcher.matches();
            return new Nanobot(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4))
            );
        }

        public int distanceTo(Nanobot other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
        }

        public boolean inRange(Nanobot other) {
            return distanceTo(other) <= other.radius;
        }
    }
}
