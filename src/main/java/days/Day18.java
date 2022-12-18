package days;

import setup.Day;

import java.util.*;

public class Day18 extends Day {

    private List<Point> points;
    @Override
    public void processInput() {
        points = lines().stream().map(s -> {
            var split = s.split(",");
            return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }).toList();
    }

    @Override
    public Object part1() {
        return points.stream().map(p -> p.neighbours().stream().filter(points::contains).toList()).mapToInt(n -> 6 - n.size()).sum();
    }

    @Override
    public Object part2() {
        int minX, minY, minZ;
        int maxX, maxY, maxZ;

        minX = minY = minZ = Integer.MAX_VALUE;
        maxX = maxY = maxZ = Integer.MIN_VALUE;

        for (Point p : points) {
            if (p.x < minX) minX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.z < minZ) minZ = p.z;
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
            if (p.z > maxZ) maxZ = p.z;
        }

        minX -= 1;
        minY -= 1;
        minZ -= 1;

        maxX += 1;
        maxY += 1;
        maxZ += 1;

        Point cur = new Point(minX, minY, minZ);
        int area = 0;

        Queue<Point> toVisit = new ArrayDeque<>();
        toVisit.offer(cur);
        Set<Point> visited = new HashSet<>();

        while (!toVisit.isEmpty()) {
            cur = toVisit.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);
            for (Point n : cur.neighbours()) {
                if (visited.contains(n)) continue;
                if (points.contains(n)) area += 1;
                else if (n.x >= minX && n.x <= maxX && n.y >= minY && n.y <= maxY && n.z >= minZ && n.z <= maxZ) toVisit.offer(n);
            }
        }

        return area;
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

    @Override
    public String partTwoSolution() {
        return "2604";
    }

    private record Point(int x, int y, int z) {
        private Set<Point> neighbours() {
            return Set.of(
                    new Point(x + 1, y, z),
                    new Point(x - 1, y, z),
                    new Point(x, y + 1, z),
                    new Point(x, y - 1, z),
                    new Point(x, y, z + 1),
                    new Point(x, y, z - 1)
            );
        }
    }
}
