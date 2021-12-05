package days;

import setup.Day;

public class Day2 extends Day {
    String[] instructions;

    @Override
    public void processInput() {
        instructions = input.split("\n");
    }

    @Override
    public Object part1() {
        int horizontal = 0, depth = 0;

        for (String instruction : instructions) {
            String[] command = instruction.split(" ");
            int n = Integer.parseInt(command[1]);
            switch (command[0]) {
                case "forward" -> horizontal += n;
                case "down" -> depth += n;
                case "up" -> depth -= n;
            }
        }

        return horizontal * depth;
    }

    @Override
    public Object part2() {
        int horizontal = 0, depth = 0, aim = 0;

        for (String instruction : instructions) {
            String[] command = instruction.split(" ");
            int n = Integer.parseInt(command[1]);
            switch (command[0]) {
                case "forward" -> {
                    horizontal += n;
                    depth += aim * n;
                }
                case "down" -> aim += n;
                case "up" -> aim -= n;
            }
        }

        return horizontal * depth;
    }

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "1868935";
    }

    @Override
    public String partTwoSolution() {
        return "1965970888";
    }
}
