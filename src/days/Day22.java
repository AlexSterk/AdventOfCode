package days;

import setup.Day;
import util.Graph;
import util.Line.Point;

import java.util.Arrays;
import java.util.Set;

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

        height = target.y() + 100;
        width = target.x() + 100;
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
                else if (x == target.x() && y == target.y()) geoIndex = 0;
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
        Graph<Node> graph = new Graph<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Node a = null, b = null;
                switch (riskLevel[y][x]) {
                    case 0 -> {
                        a = new Node(x, y, Tool.GEAR);
                        b = new Node(x, y, Tool.TORCH);
                    }
                    case 1 -> {
                        a = new Node(x, y, Tool.GEAR);
                        b = new Node(x, y, Tool.NEITHER);
                    }
                    case 2 -> {
                        a = new Node(x, y, Tool.NEITHER);
                        b = new Node(x, y, Tool.TORCH);
                    }
                }
                graph.addNode(a);
                graph.addNode(b);
                graph.addEdge(a, b, 7);
            }
        }

        for (Node n1 : graph.nodes()) {
            for (Node n2 : n1.neighbours()) {
                if (graph.nodes().contains(n2)) graph.addEdge(n1, n2, 1);
            }
        }

        return graph.getDistance(new Node(0, 0, Tool.TORCH), new Node(target.x(), target.y(), Tool.TORCH));
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum Tool {
        TORCH,
        GEAR,
        NEITHER
    }

    private record Node(int x, int y, Tool t) {
        private Node up() {
            return new Node(x, y - 1, t);
        }

        private Node down() {
            return new Node(x, y + 1, t);
        }

        private Node left() {
            return new Node(x - 1, y, t);
        }

        private Node right() {
            return new Node(x + 1, y, t);
        }

        private Set<Node> neighbours() {
            return Set.of(
                    up(),
                    down(),
                    left(),
                    right()
            );
        }
    }
}
