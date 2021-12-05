package days;

import setup.Day;
import util.Grid;
import util.Line;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Day5 extends Day {
    private List<Line> lines;
    private Grid<Integer> board;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        this.lines = Arrays.stream(lines).map(Line::toLine).toList();

        List<Line.Point> points = this.lines.stream()
                .flatMap(l -> Stream.of(l.a(), l.b()))
                .toList();

        var maxX = Collections.max(points, Comparator.comparing(Line.Point::x)).x() + 1;
        var maxY = Collections.max(points, Comparator.comparing(Line.Point::y)).y() + 1;

        board = new Grid<>(maxX, maxY);
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                board.set(x, y, 0);
            }
        }
    }

    @Override
    public Object part1() {
        lines.stream()
                .filter(l -> l.horizontal() || l.vertical())
                .map(Line::expand)
//                .peek(System.out::println)
                .forEach(ps -> ps.forEach(p -> board.set(p.x(), p.y(), board.getTile(p.x(), p.y()).data() + 1)));
//        System.out.println(board);
        return board.getAll().stream().filter(t -> t.data() >= 2).count();
    }

    @Override
    public Object part2() {
        lines.stream()
//                .sorted(Comparator.comparingInt(o -> Math.min(o.a().y(), o.b().y())))
                .map(Line::expand)
//                .peek(System.out::println)
                .forEach(ps -> ps.forEach(p -> board.set(p.x(), p.y(), board.getTile(p.x(), p.y()).data() + 1)));
//        System.out.println(board);
        return board.getAll().stream().filter(t -> t.data() >= 2).count();
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public boolean isTest() {
        return false;
    }

}
