package days;

import setup.Day;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class Day7 extends Day {
    List<Integer> crabs;

    @Override
    public void processInput() {
        crabs = Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList();
    }

    @Override
    public Object part1() {
        int min = Collections.min(crabs);
        int max = Collections.max(crabs);

        OptionalInt min1 = IntStream.rangeClosed(min, max).map(i -> crabs.stream().mapToInt(x -> Math.abs(x - i)).sum()).min();

        return min1.getAsInt();
    }

    @Override
    public Object part2() {
        int min = Collections.min(crabs);
        int max = Collections.max(crabs);

        OptionalInt min1 = IntStream.rangeClosed(min, max)
                .map(i -> crabs.stream()
                        .mapToInt(x -> Math.abs(x - i))
                        .map(x -> IntStream.rangeClosed(1, x).sum())
                        .sum())
                .min();

        return min1.getAsInt();
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "349357";
    }

    @Override
    public String partTwoSolution() {
        return "96708205";
    }
}
