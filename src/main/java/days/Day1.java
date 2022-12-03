package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;

public class Day1 extends Day {

    private List<List<Integer>> elfs;

    @Override
    public void processInput() {
        elfs = Arrays.stream(input.split("(\r?\n){2}")).map(s -> Arrays.stream(s.split("\r?\n")).map(Integer::parseInt).toList()).toList();
    }

    @Override
    public Object part1() {
        // sum lists and get max
        return elfs.stream().mapToInt(l -> l.stream().mapToInt(Integer::intValue).sum()).max().orElseThrow();
    }

    @Override
    public String partOneSolution() {
        return "70509";
    }

    @Override
    public Object part2() {
        // sum lists and sort in reverse and get first 3 summed
        return elfs.stream().mapToInt(l -> l.stream().mapToInt(Integer::intValue).sum()).boxed().sorted((a, b) -> b - a).limit(3).mapToInt(Integer::intValue).sum();
    }

    @Override
    public String partTwoSolution() {
        return "208567";
    }

    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
