package days;

import setup.Day;
import util.Grid;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DataFlowIssue")
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
    }

    @Override
    public Object part1() {
        cave = new Grid<>(maxX + 1, maxY + 1);
        initCave();

        cave.set(500, 0, '+');

        var source = cave.getTile(500, 0);
        var sand = source;
        int rested = 0;

        while (sand.down() != null) {
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

        return rested;
    }

    @Override
    public Object part2() {
        cave = new Grid<>(maxX + 1 + 200, maxY + 1 + 2);
        initCave();
        cave.getRow(maxY + 2).forEach(t -> cave.set(t, '#'));

        cave.set(500, 0, '+');

        var source = cave.getTile(500, 0);
        var sand = source;
        int rested = 0;

        while (sand.data() != 'o') {
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
            sand = cave.getTile(source.x(), source.y());
            rested++;
        }


        return rested;
    }

    private void initCave() {
        cave.fill('.');

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

    @Override
    public String partTwoSolution() {
        return "25248";
    }
}
