package days;

import setup.Day;

import java.util.*;

public class Day2 extends Day {
    List<List<Integer>> spreadsheet;

    @Override
    public void processInput() {
        spreadsheet = new ArrayList<>();
        for (String s : input.split("\n")) {
            ArrayList<Integer> e = new ArrayList<>();
            spreadsheet.add(e);
            for (String s1 : s.split("\\s")) {
                e.add(Integer.parseInt(s1));
            }
        }
    }

    @Override
    public Object part1() {
        return spreadsheet.stream().mapToInt(l -> Math.abs(Collections.max(l) - Collections.min(l))).sum();
    }

    @Override
    public Object part2() {
        int sum = 0;
        r: for (List<Integer> row : spreadsheet) {
            for (Integer i : row) {
                for (Integer j : row) {
                    if (i.equals(j)) continue;
                    if (i % j == 0) {
                        sum +=i/j;
                        continue r;
                    }
                }
            }
        }
        return sum;
    }

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public String partOneSolution() {
        return "43074";
    }

    @Override
    public String partTwoSolution() {
        return "280";
    }
}
