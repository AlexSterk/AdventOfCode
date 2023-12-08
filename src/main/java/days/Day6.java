package days;

import setup.Day;
import util.BinarySearch;

import java.util.ArrayList;
import java.util.List;

public class Day6 extends Day {
    List<Race> races;

    @Override
    public void processInput() {
        var timeS = lines().get(0).split(":")[1];
        var distanceS = lines().get(1).split(":")[1];

        var times = timeS.trim().split("\\s+");
        var distances = distanceS.trim().split("\\s+");

        races = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            races.add(new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i])));
        }
    }

    @Override
    public Object part1() {
        return races.stream().mapToInt(this::waysToWin).mapToLong(i -> i).reduce((a, b) -> a * b).getAsLong();
    }

    private boolean willWin(Race r, long time) {
        return (r.duration - time) * time > r.recordDistance;
    }

    private int waysToWin(Race r) {
        int ways = 0;
        for (int i = 0; i < r.duration; i++) {
            if (willWin(r, i)) {
                ways++;
            }
        }
        return ways;
    }

    @Override
    public Object part2() {
        var time = Long.parseLong(lines().get(0).split(":")[1].replaceAll("\\s+", ""));
        var distance = Long.parseLong(lines().get(1).split(":")[1].replaceAll("\\s+", ""));

        var race = new Race(time, distance);

        long min = BinarySearch.Long.search(0L, race.duration, l -> this.willWin(race, l));
        long max = BinarySearch.Long.search(0L, race.duration, l -> !this.willWin(race, l));
        return max - min;
    }

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "252000";
    }

    @Override
    public String partTwoSolution() {
        return "36992486";
    }

    private record Race(long duration, long recordDistance) {
    }
}
