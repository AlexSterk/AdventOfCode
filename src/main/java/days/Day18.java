package days;

import setup.Day;
import util.Graph;

import java.util.List;

public class Day18 extends Day {

    private List<Point> points;
    private Graph<Point> graph;

    @Override
    public void processInput() {
        points = lines().stream().map(s -> {
            var split = s.split(",");
            return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }).toList();

        graph = new Graph<>();

        points.forEach(graph::addNode);
        for (Point p1 : points) {
            for (Point p2 : points) {
                if (p1 == p2) continue;
                if (p1.manHattanDistance(p2) == 1) {
                    graph.addEdge(p1, p2, 1);
                }
            }
        }
    }

    @Override
    public Object part1() {
        return points.stream().map(graph::getNeighbours).mapToInt(n -> 6 - n.size()).sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "4604";
    }

    private record Point(int x, int y, int z) {
        private int manHattanDistance(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
        }
    }
}
