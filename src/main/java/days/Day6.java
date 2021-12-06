package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day6 extends Day {
    private List<Integer> fishes;

    @Override
    public void processInput() {
        fishes = new ArrayList<>(Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList());
    }

    @Override
    public Object part1() {
        for (int n = 0; n < 80; n++) {
            int size = fishes.size();
            for (int i = 0; i < size; i++) {
                Integer integer = fishes.get(i);
                if (integer == 0) {
                    fishes.set(i, 6);
                    fishes.add(8);
                } else fishes.set(i, integer - 1);
            }
        }

        return fishes.size();
    }

    @Override
    public Object part2() {
        long spawning;
        long[] populations = IntStream.rangeClosed(0, 8)
                .mapToLong(i -> fishes.stream().filter(x -> x == i).count())
                .toArray();
        for (int i = 0; i < 256; i++) {
            spawning = populations[0];
            populations[0] = populations[1];
            populations[1] = populations[2];
            populations[2] = populations[3];
            populations[3] = populations[4];
            populations[4] = populations[5];
            populations[5] = populations[6];
            populations[6] = populations[7] + spawning;
            populations[7] = populations[8];
            populations[8] = spawning;
        }

        return Arrays.stream(populations).sum();
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
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
        return "353274";
    }

    @Override
    public String partTwoSolution() {
        return "1609314870967";
    }
}
