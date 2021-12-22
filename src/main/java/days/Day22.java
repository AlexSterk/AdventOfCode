package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day22 extends Day {

    private List<RebootStep> rebootSteps;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        rebootSteps = Arrays.stream(lines).map(RebootStep::parse).toList();
    }

    @Override
    public Object part1() {
        Cuboid range = new Cuboid(-50, 50, -50, 50, -50, 50);
        List<RebootStep> rebootSteps = this.rebootSteps.stream().filter(s -> s.c.inRange(range)).toList();

        return run(rebootSteps);
    }

    private long run(List<RebootStep> rebootSteps) {
        List<RebootStep> list = new ArrayList<>();
        for (RebootStep rebootStep : rebootSteps) {
            for (RebootStep step : List.copyOf(list)) {
                Cuboid overlap = rebootStep.c.intersection(step.c);
                if (overlap != null) {
                    list.add(new RebootStep(!step.on, overlap));
                }
            }
            if (rebootStep.on) list.add(rebootStep);
        }

        return list.stream().mapToLong(r -> r.c.volume() * (r.on ? 1 : -1)).sum();
    }

    @Override
    public Object part2() {
        return run(rebootSteps);
    }

    @Override
    public String partOneSolution() {
        return "589411";
    }

    @Override
    public String partTwoSolution() {
        return "1130514303649907";
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record RebootStep(boolean on, Cuboid c) {
        private static RebootStep parse(String s) {
            boolean on = s.startsWith("on");
            Cuboid c = Cuboid.parse(s.replaceFirst(".* ", ""));
            return new RebootStep(on, c);
        }
    }

    private record Cuboid(long x1, long x2, long y1, long y2, long z1, long z2) {
        private static Cuboid parse(String s) {
            String[] split = s.split(",");
            return new Cuboid(
                    Integer.parseInt(split[0].substring(2).split("\\.\\.")[0]),
                    Integer.parseInt(split[0].substring(2).split("\\.\\.")[1]),
                    Integer.parseInt(split[1].substring(2).split("\\.\\.")[0]),
                    Integer.parseInt(split[1].substring(2).split("\\.\\.")[1]),
                    Integer.parseInt(split[2].substring(2).split("\\.\\.")[0]),
                    Integer.parseInt(split[2].substring(2).split("\\.\\.")[1])
            );
        }

        private Cuboid intersection(Cuboid c) {
            if (x1 > c.x2 || c.x1 > x2 || y1 > c.y2 || c.y1 > y2 || z1 > c.z2 || c.z1 > z2) return null;

            return new Cuboid(
                    Math.max(x1, c.x1),
                    Math.min(x2, c.x2),
                    Math.max(y1, c.y1),
                    Math.min(y2, c.y2),
                    Math.max(z1, c.z1),
                    Math.min(z2, c.z2)
            );
        }

        private boolean inRange(Cuboid c) {
            return this.equals(intersection(c));
        }

        private long volume() {
            return Math.abs(x2 - x1 + 1) * Math.abs(y2 - y1 + 1) * Math.abs(z2 - z1 + 1);
        }
    }
}
