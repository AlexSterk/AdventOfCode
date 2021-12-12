package days;

import setup.Day;
import util.Graph;

import java.util.Map;

public class Day12 extends Day {
    private Graph<String> graph;

    @Override
    public void processInput() {
        graph = new Graph<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            String program = line.substring(0, line.indexOf(' '));
            graph.addNode(program);
            for (String connectedTo : line.substring(line.indexOf('>') + 1).trim().split(", ")) {
                graph.addNode(connectedTo);
                graph.addEdge(program, connectedTo, 1);
            }
        }
    }

    @Override
    public Object part1() {
        Map<String, Integer> connected = graph.floodFill();
        Integer group = connected.get("0");
        return connected.values().stream().filter(i -> i.equals(group)).count();
    }

    @Override
    public Object part2() {
        return null;
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
        return "283";
    }
}
