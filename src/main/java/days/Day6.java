package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 extends Day {
    private List<Integer> fishes;

    @Override
    public void processInput() {
        fishes = new ArrayList<>(Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList());
    }

    @Override
    public Object part1() {
        for (int n = 0; n < 80; n++) {
//            System.out.println(fishes);
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
        for (int n = 0; n < 80; n++) {
//            System.out.println(fishes);
            int size = fishes.size();
            for (int i = 0; i < size; i++) {
                Integer integer = fishes.get(i);
                if (integer == 0) {
                    fishes.set(i, 6);
                    fishes.add(8);
                } else fishes.set(i, integer - 1);
            }
        }

        return null;
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
}
