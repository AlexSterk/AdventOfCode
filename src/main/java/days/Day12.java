package days;

import setup.Day;
import util.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12 extends Day {
    private Graph<String> graph;

    @Override
    public void processInput() {
        graph = new Graph<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] split = line.split("-");
            graph.addNode(split[0]);
            graph.addNode(split[1]);
            graph.addEdge(split[0], split[1], 1);
        }
    }

    @Override
    public Object part1() {
        Set<List<String>> walks = new HashSet<>();
        performWalkPart1("start", "end", new ArrayList<>(), walks);
        return walks.size();
    }

    @Override
    public Object part2() {
        Set<List<String>> walks = new HashSet<>();
        performWalkPart2("start", "end", null, new ArrayList<>(), walks);
        return walks.size();
    }

    private void performWalkPart1(String from, String to, List<String> walk, Set<List<String>> walks) {
        if (from.chars().allMatch(Character::isLowerCase) && walk.contains(from)) {
            return;
        }
        walk.add(from);
        if (from.equals(to)) {
            walks.add(walk);
            return;
        }
        for (String neighbour : graph.getNeighbours(from)) {
            performWalkPart1(neighbour, to, new ArrayList<>(walk), walks);
        }
    }

    private void performWalkPart2(String from, String to, String visitTwice, List<String> walk, Set<List<String>> walks) {
        boolean smallCave = from.chars().allMatch(Character::isLowerCase);
        if (smallCave && walk.contains(from)) {
            if (!from.equals(visitTwice)) {
                return;
            }
            if (walk.stream().filter(s -> s.equals(from)).count() > 1) {
                return;
            }
        }
        walk.add(from);
        if (from.equals(to)) {
            walks.add(walk);
            return;
        }
        for (String neighbour : graph.getNeighbours(from)) {
            performWalkPart2(neighbour, to, visitTwice, new ArrayList<>(walk), walks);
            if (smallCave && visitTwice == null && !from.equals("start"))
                performWalkPart2(neighbour, to, from, new ArrayList<>(walk), walks);
        }
    }

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "3497";
    }

    @Override
    public String partTwoSolution() {
        return "93686";
    }
}
