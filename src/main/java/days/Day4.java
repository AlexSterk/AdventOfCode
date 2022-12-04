package days;

import setup.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day4 extends Day {

    private final List<Ranges> ranges = new ArrayList<>();
    @Override
    public void processInput() {
        var p = Pattern.compile("\\d+");
        for (String line : lines()) {
            List<Integer> collect = p.matcher(line).results().map(MatchResult::group).map(Integer::parseInt).toList();
            ranges.add(new Ranges(collect.get(0), collect.get(1), collect.get(2), collect.get(3)));
        }
    }

    @Override
    public Object part1() {
        int fullOverlap = 0;
        for (Ranges range : ranges) {
            if (range.a1 <= range.b1 && range.a2 >= range.b2) {
                fullOverlap++;
            } else if (range.b1 <= range.a1 && range.b2 >= range.a2) {
                fullOverlap++;
            }
        }



        return fullOverlap;
    }

    @Override
    public Object part2() {
        int overlap = 0;

        for (Ranges range : ranges) {
            if (range.a1 <= range.b1 && range.a2 >= range.b2) {
                overlap ++;
            } else if (range.b1 <= range.a1 && range.b2 >= range.a2) {
                overlap ++;
            } else if (range.a1 <= range.b1 && range.a2 >= range.b1) {
                overlap ++;
            } else if (range.b1 <= range.a1 && range.b2 >= range.a1) {
                overlap ++;
            }
        }

        return overlap;
    }

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "599";
    }

    @Override
    public String partTwoSolution() {
        return "928";
    }

    private record Ranges(int a1, int a2, int b1, int b2) {}
}
