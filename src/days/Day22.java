package days;

import setup.Day;
import util.Line.Point;

import java.util.*;

public class Day22 extends Day {
    private int depth;
    private Integer[][] geologicIndex;
    private Integer[][] erosionLevels;
    private Integer[][] riskLevel;

    private Point target;
    private int height;
    private int width;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        depth = Integer.parseInt(lines[0].substring(7));
        String[] coords = lines[1].substring(8).split(",");
        target = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

        height = target.y() + 1000;
        width = target.x() + 1000;
        geologicIndex = new Integer[height][width];
        erosionLevels = new Integer[height][width];
        riskLevel = new Integer[height][width];
    }

    @Override
    public Object part1() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int geoIndex;
                if (x == 0 && y == 0) geoIndex = 0;
                if (x == target.x() && y == target.y()) geoIndex = 0;
                else if (y == 0) geoIndex = x * 16807;
                else if (x == 0) geoIndex = y * 48271;
                else geoIndex = erosionLevels[y][x - 1] * erosionLevels[y - 1][x];
                geologicIndex[y][x] = geoIndex;
                erosionLevels[y][x] = (geologicIndex[y][x] + depth) % 20183;
                riskLevel[y][x] = erosionLevels[y][x] % 3;
            }
        }

        return Arrays.stream(riskLevel)
                .limit(target.y() + 1)
                .flatMap(array -> Arrays.stream(array).limit(target.x() + 1))
                .mapToInt(i -> i).sum();
    }

    @Override
    public Object part2() {
        Node[][] nodes = new Node[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                nodes[y][x] = new Node(x, y, null);
            }
        }



        return null;
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return true;
    }

    private enum Tool {
        TORCH,
        GEAR,
        NEITHER;
    }

    private record Graph(Set<Node> nodes, Map<Edge, Integer> weights) {

    }

    private record Node(int x, int y, Tool tool) {

    }

    private static class Edge extends HashSet<Node> {
        public Edge(Node from, Node to) {
            this.add(from);
            this.add(to);
        }

        public Edge(Node cycle) {
            this.add(cycle);
        }
    }
}
