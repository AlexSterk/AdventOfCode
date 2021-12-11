package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;

import java.util.Set;
import java.util.stream.Collectors;

public class Day11 extends Day {
    private Grid<Octopus> octopusses;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        octopusses = new Grid<>(lines[0].length(), lines.length);
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                octopusses.set(x, y, new Octopus(Character.getNumericValue(lines[y].charAt(x))));
            }
        }
    }

    @Override
    public Object part1() {
        int count = 0;
        for (int n = 0; n < 100; n++) {
            count = simulateRound(count);
        }

        return count;
    }

    private int simulateRound(int count) {
        octopusses.getAll().forEach(t -> t.data().energy++);

        Set<Tile<Octopus>> needsToFlash;
        while (!(needsToFlash = octopusses.getAll().stream().filter(t -> t.data().energy > 9 && !t.data().flashed).collect(Collectors.toSet())).isEmpty()) {
            for (Tile<Octopus> tile : needsToFlash) {
                count++;
                tile.data().flashed = true;
                octopusses.getAdjacent(tile, true).forEach(t -> t.data().energy++);
            }
        }

        octopusses.getAll().stream().filter(t -> t.data().energy > 9).forEach(t -> t.data().reset());
        return count;
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        int n = 0;
        while (true) {
            n++;
            int count = simulateRound(0);
            if (count == 100) break;
        }

        return n;
    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class Octopus {
        private int energy;
        private boolean flashed = false;
        public Octopus(int energy) {
            this.energy = energy;
        }

        public void reset() {
            energy = 0;
            flashed = false;
        }
    }

    @Override
    public String partOneSolution() {
        return "1749";
    }

    @Override
    public String partTwoSolution() {
        return "285";
    }
}
