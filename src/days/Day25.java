package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import util.Graph;

public class Day25 extends Day {

    private List<SpacetimePoint> points;

    @Override
    public void processInput() {
        points = Arrays.stream(input.split("\n")).map(SpacetimePoint::SpacetimePoint).toList();
    }

    @Override
    public Object part1() {
        Graph<SpacetimePoint> graph = new Graph<>();
        points.forEach(graph::addNode);

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                SpacetimePoint p1 = points.get(i), p2 = points.get(j);
                int d = p1.distanceTo(p2);
                if (d <= 3) graph.addEdge(p1, p2, d);
            }
        }

        Map<SpacetimePoint, Integer> constellations = graph.floodFill();

        return constellations.values().stream().distinct().count();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record SpacetimePoint(int x1, int x2, int x3, int x4) {
        private static SpacetimePoint SpacetimePoint(String s) {
            String[] split = s.split(",");
            return new SpacetimePoint(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        }

        private int distanceTo(SpacetimePoint other) {
            return Math.abs(x1 - other.x1) + Math.abs(x2 - other.x2) + Math.abs(x3 - other.x3) + Math.abs(x4 - other.x4);
        }
    }
}
