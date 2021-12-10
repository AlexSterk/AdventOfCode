package days;

import setup.Day;
import util.Line.Point;

import java.util.*;
import java.util.stream.Collectors;

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
        Graph g = new Graph();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (riskLevel[y][x]) {
                    case 0 -> {
                        g.put(x, y, Tool.GEAR);
                        g.put(x, y, Tool.TORCH);
                    }
                    case 1 -> {
                        g.put(x, y, Tool.GEAR);
                        g.put(x, y, Tool.NEITHER);
                    }
                    case 2 -> {
                        g.put(x, y, Tool.NEITHER);
                        g.put(x, y, Tool.TORCH);
                    }
                }
            }
        }

        Node start = new Node(0, 0, Tool.TORCH);
        Node end = new Node(target.x(), target.y(), Tool.TORCH);

        Map<Node, Integer> dist = new HashMap<>();
        dist.put(start, 0);
        PriorityQueue<Node> Q = new PriorityQueue<>(Comparator.comparing(dist::get));

        for (Node node : g.getAllNodes()) {
            if (!node.equals(start)) {
                dist.put(node, Integer.MAX_VALUE);
            }
        }

        Q.offer(start);

        while (!Q.isEmpty()) {
            Node u = Q.poll();
            for (Node v : g.getNeighbours(u)) {
                int alt = dist.get(u) + g.getWeight(u, v);
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    Q.offer(v);
                }
            }
        }

        return dist.get(end);
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
        NEITHER;
    }

    private record Graph(Map<Integer, Map<Integer, Map<Tool, Node>>> nodes) {
        private Graph() {
            this(new HashMap<>());
        }

        private Set<Node> getAllNodes() {
            return nodes.values()
                    .stream()
                    .flatMap(m -> m.values().stream())
                    .flatMap(m -> m.values().stream())
                    .collect(Collectors.toSet());
        }

        private void put(int x, int y, Tool t) {
            if (!nodes.containsKey(x)) {
                nodes.put(x, new HashMap<>());
            }
            if (!nodes.get(x).containsKey(y)) {
                nodes.get(x).put(y, new HashMap<>());
            }
            nodes.get(x).get(y).put(t, new Node(x, y, t));
        }

        private Node get(int x, int y, Tool t) {
            if (!nodes.containsKey(x)) return null;
            if (!nodes.get(x).containsKey(y)) return null;
            return nodes.get(x).get(y).getOrDefault(t, null);
        }

        private Set<Node> getNeighbours(Node n) {
            Set<Node> neighbours = new HashSet<>();

            neighbours.add(this.get(n.x - 1, n.y, n.tool));
            neighbours.add(this.get(n.x + 1, n.y, n.tool));
            neighbours.add(this.get(n.x, n.y + 1, n.tool));
            neighbours.add(this.get(n.x, n.y - 1, n.tool));

            neighbours.add(this.get(n.x, n.y, Tool.GEAR));
            neighbours.add(this.get(n.x, n.y, Tool.TORCH));
            neighbours.add(this.get(n.x, n.y, Tool.NEITHER));

            neighbours.remove(n);
            neighbours.removeIf(Objects::isNull);

            return neighbours;
        }

        private int getWeight(Node from, Node to) {
            if (from.x == to.x && from.y == to.y) return 7;
            else return 1;
        }
    }

    private record Node(int x, int y, Tool tool) {

    }
}
