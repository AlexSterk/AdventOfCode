package days;

import setup.Day;
import util.Line;
import util.Line.Point;

import java.util.Arrays;

public class Day22 extends Day {
    private int depth;
    private Integer[][] geologicIndex;
    private Integer[][] erosionLevels;
    private Integer[][] riskLevel;

    private Point target;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        depth = Integer.parseInt(lines[0].substring(7));
        String[] coords = lines[1].substring(8).split(",");
        target = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));

        geologicIndex = new Integer[target.y() + 1][target.x() + 1];
        erosionLevels = new Integer[target.y() + 1][target.x() + 1];
        riskLevel = new Integer[target.y() + 1][target.x() + 1];
    }

    @Override
    public Object part1() {
        for (int y = 0; y <= target.y(); y++) {
            for (int x = 0; x <= target.x(); x++) {
                int geoIndex;
                if (x == 0 && y == 0) geoIndex = 0;
                if (x == target.x() && y == target.y()) geoIndex = 0;
                else if (y == 0) geoIndex = x * 16807;
                else if (x == 0) geoIndex = y * 48271;
                else geoIndex = erosionLevels[y][x-1] * erosionLevels[y - 1][x];
                geologicIndex[y][x] = geoIndex;
                erosionLevels[y][x] = (geologicIndex[y][x] + depth) % 20183;
                riskLevel[y][x] = erosionLevels[y][x] % 3;
            }
        }

        return Arrays.stream(riskLevel).flatMap(Arrays::stream).mapToInt(i -> i).sum();
    }

    @Override
    public Object part2() {


        return null;
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
