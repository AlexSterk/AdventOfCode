package days;

import setup.Day;
import util.Grid;
import util.Line.Point;
import util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Day13 extends Day {
    private Grid<Character> paper;
    private List<Pair<Character, Integer>> folds;

    @Override
    public void processInput() {
        String[] split = input.split("\n\n");
        String[] lines = split[0].split("\n");

        List<Point> dots = new ArrayList<>();

        for (String line : lines) {
            String[] c = line.split(",");
            dots.add(new Point(Integer.parseInt(c[0]), Integer.parseInt(c[1])));
        }

        var w = Collections.max(dots, Comparator.comparing(Point::x)).x() + 1;
        var h = Collections.max(dots, Comparator.comparing(Point::y)).y() + 1;

        paper = new Grid<>(w, h);
        paper.init(() -> '.', false);

        dots.forEach(p -> paper.set(p.x(), p.y(), '#'));

        folds = new ArrayList<>();
        String[] lines2 = split[1].trim().split("\n");
        for (String f : lines2) {
            f = f.substring(11);
            folds.add(new Pair<>(f.charAt(0), Integer.parseInt(f.substring(2))));
        }
    }

    @Override
    public Object part1() {
        executeFold(folds.get(0));

        return paper.getAll().stream().filter(t -> t.data() == '#').count();
    }

    private void executeFold(Pair<Character, Integer> fold) {
        Grid<Character> receiving;
        Grid<Character> flipping;
        Character a = fold.a();
        if (a == 'x') {
            receiving = paper.subgrid(0, fold.b() - 1, 0, paper.height - 1);
            flipping = paper.subgrid(fold.b() + 1, paper.width - 1, 0, paper.height - 1);
            flipping = flipping.flip(false);

            int offset = receiving.width - flipping.width;
            Grid<Character> subgrid = receiving.subgrid(offset, receiving.width - 1, 0, receiving.height - 1);
            for (Grid.Tile<Character> t : subgrid.getAll()) {
                if (t.x() < flipping.width) {
                    Grid.Tile<Character> flippingTile = flipping.getTile(t.x(), t.y());
                    receiving.set(t.x() + offset, t.y(), flippingTile.data() == '#' ? '#' : t.data());
                }
            }
            paper = receiving;
        } else if (a == 'y') {
            receiving = paper.subgrid(0, paper.width - 1, 0, fold.b() - 1);
            flipping = paper.subgrid(0, paper.width - 1, fold.b() + 1, paper.height - 1).flip(true);

            int offset = receiving.height - flipping.height;

            Grid<Character> subgrid = receiving.subgrid(0, receiving.width - 1, offset, receiving.height - 1);
            for (Grid.Tile<Character> t : subgrid.getAll()) {
                if (t.y() < flipping.height)
                    receiving.set(t.x(), t.y() + offset, flipping.getTile(t.x(), t.y()).data() == '#' ? '#' : t.data());
            }
            paper = receiving;
        }
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        for (Pair<Character, Integer> fold : folds) {
            executeFold(fold);
        }

        return "\n" + paper + "\n";
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "655";
    }
}
