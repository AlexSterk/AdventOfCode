package days;

import org.apache.commons.math3.linear.*;
import setup.Day;
import util.Annotations.TestInput;
import util.Pair;

import java.util.List;

import static util.Annotations.Solution;

//@TestInput
public class Day24 extends Day {

    private List<Hailstone> hailstones;

    @Override
    public void processInput() {
        hailstones = lines().stream().map(Hailstone::parse).toList();
    }

    @Solution("")
    @Override
    public Object part1() {
        var testArea = isTest() ? new Pair<>(7L, 27L) : new Pair<>(200000000000000L, 400000000000000L);
        long intersections = 0;

        for (Hailstone h1 : hailstones) {
            for (Hailstone h2 : hailstones) {
                if (h1 == h2) continue;

//                System.out.println(h1);
//                System.out.println(h2);

                // first constant then coefficient
                Pair<Long, Long> x1 = new Pair<>(h1.x(), h1.vx());
                Pair<Long, Long> x2 = new Pair<>(h2.x(), h2.vx());

                Pair<Long, Long> y1 = new Pair<>(h1.y(), h1.vy());
                Pair<Long, Long> y2 = new Pair<>(h2.y(), h2.vy());

                try {
                    DecompositionSolver solver = new LUDecomposition(new Array2DRowRealMatrix(new double[][]{
                            {x1.b(), -x2.b()},
                            {y1.b(), -y2.b()}
                    })).getSolver();
                    RealMatrix solve = solver.solve(new Array2DRowRealMatrix(new double[][]{
                            {x2.a() - x1.a()},
                            {y2.a() - y1.a()}
                    }));

//                    System.out.println(solve);
                    var t1 = solve.getEntry(0, 0);
                    var t2 = solve.getEntry(1, 0);

                    if (t1 < 0 || t2 < 0) continue;

                    var x = x1.a() + solve.getEntry(0, 0) * x1.b();
                    var y = y1.a() + solve.getEntry(0, 0) * y1.b();

                    if (x >= 0 && x <= testArea.a() && y >= 0 && y <= testArea.b()) {
                        intersections++;
                    }

                    // check if ON the boundary
                    if (x == 0 || x == testArea.a() || y == 0 || y == testArea.b()) {
                        System.out.printf("Intersection: (%f, %f) on boundary\n", x, y);
                    }

//                    System.out.printf("Intersection: (%f, %f)\n", x, y);
//                    System.out.println();
                } catch (SingularMatrixException e) {
                    continue;
                }
            }
        }

        return intersections;
    }

    @Solution("")
    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 24;
    }

//    private static Pair<Double, Double> getIntersection(Line l1, Line l2) {
//        double x1 = l1.a().x(), y1 = l1.a().y(), x2 = l1.b().x(), y2 = l1.b().y();
//        double x3 = l2.a().x(), y3 = l2.a().y(), x4 = l2.b().x(), y4 = l2.b().y();
//
//        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
//
//        if (d == 0) return null;
//
//        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
//        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
//
//        return new Pair<>(xi, yi);
//    }

    private record Hailstone(long x, long y, long z, long vx, long vy, long vz) {
        public Hailstone(String[] in) {
            this(
                    Long.parseLong(in[0]),
                    Long.parseLong(in[1]),
                    Long.parseLong(in[2]),
                    Long.parseLong(in[3]),
                    Long.parseLong(in[4]),
                    Long.parseLong(in[5])
            );
        }

        public static Hailstone parse(String in) {
            return new Hailstone(in.split("\\s*[,@]\\s*"));
        }
    }
}
