package days;

import setup.Day;
import util.Line.Point;

public class Day17 extends Day {
    private Box targetArea;

    @Override
    public void processInput() {
        String[] split = input.trim().replaceFirst("target area: ", "").split(", ");
        var xs = split[0].substring(2);
        var ys = split[1].substring(2);
        String[] xsplit = xs.split("\\.\\.");
        String[] ysplit = ys.split("\\.\\.");

        targetArea = new Box(
                new Point(Integer.parseInt(xsplit[0]), Integer.parseInt(ysplit[1])),
                new Point(Integer.parseInt(xsplit[1]), Integer.parseInt(ysplit[0]))
        );
    }

    @Override
    public Object part1() {
        int maxY = Integer.MIN_VALUE;
        for (int vx = 0; vx < targetArea.bottomRight().x(); vx++) {
            for (int vy = 0; vy < 256; vy++) {
                int simulate = simulate(new Point(vx, vy));
                if (simulate > maxY) {
                    maxY = simulate;
                }
            }
        }

        return maxY;
    }

    @Override
    public Object part2() {
        int count = 0;
        for (int vx = 0; vx <= targetArea.bottomRight().x(); vx++) {
            for (int vy = -256; vy < 256; vy++) {
                Point velocity = new Point(vx, vy);
                int simulate = simulate(velocity);
                if (simulate > -1) {
                    count++;
                }
            }
        }

        return count;
    }

    private int simulate(Point velocity) {
        Point start = new Point(0, 0);
        Point current = start;
        int maxY = Integer.MIN_VALUE;
        for (int t = 0; t < 1000; t++) {
            current = current.add(velocity);
            if (current.y() > maxY) {
                maxY = current.y();
            }
            velocity = new Point(velocity.x() - (int) Math.signum(velocity.x()), velocity.y() - 1);
            if (targetArea.isWithin(current)) {
                return Math.max(maxY, 0);
            }
        }
        return -1;
    }

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "4950";
    }

    @Override
    public String partTwoSolution() {
        return "1477";
    }

    private record Box(Point topLeft, Point bottomRight) {
        private boolean isWithin(Point p) {
            return isWithinX(p.x())
                    && isWithinY(p.y());
        }

        private boolean isWithinX(int x) {
            return x >= topLeft.x() && x <= bottomRight.x();
        }

        private boolean isWithinY(int y) {
            return y <= topLeft.y() && y >= bottomRight.y();
        }
    }
}
