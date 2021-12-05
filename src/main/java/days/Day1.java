package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day1 extends Day {

    private List<Integer> measurements;

    @Override
    public void processInput() {
        measurements = Arrays.stream(input.trim().split("\n")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public Object part1() {
        Integer increases = 0;

        for (int i = 1; i < measurements.size(); i++) {
            if (measurements.get(i) > measurements.get(i-1)) increases++;
        }


        return increases;
    }

    private static int sum(List<Integer> window) {
        return window.stream().mapToInt(i -> i).sum();
    }

    @Override
    public Object part2() {
        int increases = 0;

        List<List<Integer>> windows = IntStream.rangeClosed(0, measurements.size() - 3).mapToObj(i -> measurements.subList(i, i + 3)).toList();
        for (int i = 1; i < windows.size(); i++) {
            if (sum(windows.get(i)) > sum(windows.get(i-1))) increases++;
        }


        return increases;
    }

    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "1298";
    }

    @Override
    public String partTwoSolution() {
        return "1248";
    }
}
