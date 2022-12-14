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
    }

    @Override
    public Object part1() {
        cave = new Grid<>(maxX + 1, maxY + 1);
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

        cave.set(500, 0, '+');
        int leftX = cave.getColumns().stream().dropWhile(list -> list.stream().allMatch(c -> c.data() == '.')).findFirst().get().get(0).x();
        cave = cave.subgrid(leftX - 1, maxX, 0, maxY);

        System.out.printf("Before simulation%n%s%n%n", cave);

        var source = cave.getAll().stream().filter(c -> c.data() == '+').findFirst().get();
        var sand = source;
        int rested = 0;


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
        cave = new Grid<>(maxX + 1 + 200, maxY + 1 + 2);
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
        cave.getRow(maxY + 2).forEach(t -> cave.set(t, '#'));

        cave.set(500, 0, '+');
//        int leftX = cave.getColumns().stream().dropWhile(list -> list.stream().allMatch(c -> c.data() == '.')).findFirst().get().get(0).x();
//        cave = cave.subgrid(leftX - 1, maxX, 0, maxY);

        System.out.printf("Before simulation%n%s%n%n", cave);

        var source = cave.getAll().stream().filter(c -> c.data() == '+').findFirst().get();
        var sand = source;
        int rested = 0;


        while (true) {
            if (sand.data() == 'o') {
                break;
            }


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
            sand = cave.getTile(source.x() , source.y());
            rested++;
        }

        System.out.printf("After simulation%n%s%n%n", cave);


        return rested;
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
