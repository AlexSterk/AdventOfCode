package days;

import setup.Day;
import util.Grid;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Day14 extends Day {

    int maxX, maxY;
    private List<List<Pair<Integer, Integer>>> rocks;
    private Grid<Character> cave;

    @Override
    public void processInput() {
        rocks = new ArrayList<>();
        for (String line : lines()) {
            String[] points = line.split(" -> ");
            List<Pair<Integer, Integer>> pairs = new ArrayList<>();
            for (String point : points) {
                var split = point.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                var pair = new Pair<>(x, y);

                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);

                pairs.add(pair);
            }

            rocks.add(pairs);
        }

        cave = new Grid<>(maxX + 1, maxY + 1);
        cave.fill('.');
    }

    @Override
    public Object part1() {
        for (List<Pair<Integer, Integer>> rock : rocks) {
            for (int i = 0; i < rock.size() - 1; i++) {
                var start = rock.get(i);
                var end = rock.get(i + 1);

                var startX = Math.min(start.a(), end.a());
                var endX = Math.max(start.a(), end.a());
                var startY = Math.min(start.b(), end.b());
                var endY = Math.max(start.b(), end.b());

                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        cave.set(x, y, '#');
                    }
                }
            }
        }


        System.out.printf("Before simulation%n%s%n%n", cave);

        cave.set(500, 0, '+');
        var source = cave.getTile(500, 0);
        var sand = source;
        int rested  = 0;


        while (true) {
            if (sand.down() == null) {
                break;
            }

            if (sand.down().data() == '.') {
                sand = sand.down();
                continue;
            }

            if (sand.down().left().data() == '.') {
                sand = sand.down().left();
                continue;
            }

            if (sand.down().right().data() == '.') {
                sand = sand.down().right();
                continue;
            }

            cave.set(sand, 'o');
            sand = source;
            rested++;
        }

        System.out.printf("After simulation%n%s%n%n", cave);


        return rested;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "715";
    }
}
