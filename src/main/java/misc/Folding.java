package misc;

import setup.Day;
import util.Line.Point;
import util.Pair;

import java.util.*;

public class Folding extends Day {
    private Set<Point> dots;
    private List<Pair<Character, Integer>> folds;

    public Folding(String s) {
        super(s);
    }

    @Override
    public void processInput() {
        String[] split = input.split("\n\n");
        dots = new HashSet<>();
        for (String s : split[0].split("\n")) {
            String[] p = s.split(",");
            dots.add(new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
        }
        folds = new ArrayList<>();
        String[] lines2 = split[1].trim().split("\n");
        for (String f : lines2) {
            f = f.substring(11);
            folds.add(new Pair<>(f.charAt(0), Integer.parseInt(f.substring(2))));
        }
    }

    @Override
    public Object part1() {
        return null;
    }

    private void executeFold(Set<Point> in, Pair<Character, Integer> fold) {
        if (fold.a() == 'x') {
            List<Point> points = in.stream().filter(p -> p.x() > fold.b()).toList();
            points.stream()
                    .peek(in::remove)
                    .map(p -> new Point(fold.b() - (p.x() - fold.b()), p.y()))
                    .forEach(in::add);
        }
        else if (fold.a() == 'y') {
            List<Point> points = in.stream().filter(p -> p.y() > fold.b()).toList();
            points.stream()
                    .peek(in::remove)
                    .map(p -> new Point(p.x(), fold.b() - (p.y() - fold.b())))
                    .forEach(in::add);
        }
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    public void doStuff() {
        folds.forEach(f -> executeFold(dots, f));

        var maxy = Collections.max(dots, Comparator.comparing(Point::y)).y();
        var maxx = Collections.max(dots, Comparator.comparing(Point::x)).x();

        for (int y = 0; y <= maxy; y++) {
            for (int x = 0; x <= maxx; x++) {
                System.out.print(dots.contains(new Point(x, y)) ? '#' : '.');
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Folding folding = new Folding("data/misc/day13.txt");
        folding.processInput();
        folding.doStuff();
    }
}
