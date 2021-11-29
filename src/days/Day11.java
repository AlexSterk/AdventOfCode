package days;

import setup.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {

    private int serialNumber;
    private Map<Pair, Integer> powerLevels;

    @Override
    public void processInput() {
        serialNumber = Integer.parseInt(input.trim());
    }

    private int getPowerLevel(int x, int y) {
        int rackID = x + 10;
        int powerLevel = rackID * y;
        powerLevel += serialNumber;
        powerLevel *= rackID;

        String s = "000" + powerLevel;
        char digit = new StringBuilder(s).reverse().charAt(2);
        powerLevel = Character.getNumericValue(digit);

        return powerLevel - 5;
    }

    @Override
    public void part1() {
        powerLevels = new HashMap<>();

        for (int x = 1; x < 301; x++) {
            for (int y = 1; y < 301; y++) {
                powerLevels.put(new Pair(x, y), getPowerLevel(x, y));
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
                int sum = coords.stream().mapToInt(powerLevels::get).sum();
                squarePower.put(new Pair(x, y), sum);
            }
        }

        System.out.println(squarePower.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());
    }

    @Override
    public void part2() {
        Map<Triple, Long> squarePowerLevels = new HashMap<>();
        powerLevels.forEach((k, v) -> squarePowerLevels.put(new Triple(k.x, k.y, 1), Long.valueOf(v)));

        for (int y = 1; y < 6; y++) {
            for (int x = 1; x < 6; x++) {
                System.out.print(" " + squarePowerLevels.get(new Triple(x, y, 1)) + " ");
            }
            System.out.println();
        }

        int gridSize = 300;
        for (int s = 2; s < gridSize + 1; s++) {
            for (int x = 1; x <= gridSize - s + 1; x++) {
                for (int y = 1; y <= gridSize - s + 1; y++) {
                    long a, b, c, d, e;

                    a = squarePowerLevels.get(new Triple(x + s - 1, y, 1));
                    b = squarePowerLevels.get(new Triple(x, y + s - 1, 1));
                    c = squarePowerLevels.get(new Triple(x, y, s - 1));
                    d = squarePowerLevels.get(new Triple(x + 1, y + 1, s - 1));
                    e = s > 2 ?  squarePowerLevels.get(new Triple(x + 1, y + 1, s - 2)) : 0;

                    squarePowerLevels.put(new Triple(x, y, s), a + b + c + d - e);
                }
            }
        }

        Map.Entry<Triple, Long> tripleLongEntry = squarePowerLevels.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        System.out.println(tripleLongEntry.getKey());
        System.out.println(tripleLongEntry.getValue());

    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static record Pair(int x, int y) {
    }

    private static record Triple(int x, int y, int size) {
    }
}
