package days;

import setup.Day;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {

    private int serialNumber;

    @Override
    public void processInput() {
        serialNumber = Integer.parseInt(input.trim());
    }

    private int getPowerLevel(int x, int y) {
        int rackID = x+10;
        int powerLevel = rackID*y;
        powerLevel += serialNumber;
        powerLevel *= rackID;

        String s = "000" + powerLevel;
        char digit = new StringBuilder(s).reverse().charAt(2);
        powerLevel = Character.getNumericValue(digit);

        return powerLevel - 5;
    }

    @Override
    public void part1() {
        Map<Pair, Integer> power = new HashMap<>();

        for (int x = 1; x < 301; x++) {
            for (int y = 1; y < 301; y++) {
                power.put(new Pair(x, y), getPowerLevel(x, y));
            }
        }

        Map<Pair, Integer> squarePower = new HashMap<>();
        for (int x = 1; x < 301 - 3; x++) {
            for (int y = 1; y < 301 - 3; y++) {
                List<Pair> coords = List.of(
                        new Pair(x, y),
                        new Pair(x + 1, y),
                        new Pair(x + 2, y),
                        new Pair(x, y + 1),
                        new Pair(x + 1, y + 1),
                        new Pair(x + 2, y + 1),
                        new Pair(x, y + 2),
                        new Pair(x + 1, y + 2),
                        new Pair(x + 2, y + 2)
                );
                int sum = coords.stream().mapToInt(power::get).sum();
                squarePower.put(new Pair(x, y), sum);
            }
        }

        System.out.println(squarePower.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static record Pair(int x, int y) {}
}
