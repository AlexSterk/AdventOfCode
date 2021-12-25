package days;

import setup.Day;
import util.Grid;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day6 extends Day {

    private Grid<Character> message;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        message = new Grid<>(Arrays.stream(lines).map(s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new)).toArray(Character[][]::new));
    }

    @Override
    public Object part1() {
        return message.getColumns().stream().map(l -> l.stream()
                        .map(Grid.Tile::data).toList())
                .map(l -> l.stream().collect(Collectors.toMap(c -> c, c -> 1, Integer::sum)))
                .map(m -> Collections.max(m.entrySet(), Map.Entry.comparingByValue()).getKey())
                .map(Objects::toString).collect(Collectors.joining());
    }

    @Override
    public Object part2() {
        return message.getColumns().stream().map(l -> l.stream()
                        .map(Grid.Tile::data).toList())
                .map(l -> l.stream().collect(Collectors.toMap(c -> c, c -> 1, Integer::sum)))
                .map(m -> Collections.min(m.entrySet(), Map.Entry.comparingByValue()).getKey())
                .map(Objects::toString).collect(Collectors.joining());
    }

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "ikerpcty";
    }

    @Override
    public String partTwoSolution() {
        return "uwpfaqrq";
    }
}
