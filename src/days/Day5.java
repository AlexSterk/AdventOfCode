package days;

import setup.Day;
import util.Line;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day5 extends Day {
    private List<Line> lines;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        this.lines = Arrays.stream(lines).map(Line::toLine).toList();
    }

    @Override
    public Object part1() {
        Set<Line.Point> overlap = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            Line lineA = lines.get(i);
            if (!lineA.horizontal() && !lineA.vertical()) continue;
            for (int j = i + 1; j < lines.size(); j++) {
                Line lineB = lines.get(j);
                if (!lineB.horizontal() && !lineB.vertical()) continue;
                overlap.addAll(lineA.overlap(lineB));
            }
        }

        return overlap.size();
    }

    @Override
    public Object part2() {
        return null;
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
