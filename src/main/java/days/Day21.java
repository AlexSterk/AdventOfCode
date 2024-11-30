package days;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import setup.Day;
import util.Direction;
import util.Grid;
import util.Line;

import java.util.*;

import static util.Annotations.Solution;

//@TestInput
public class Day21 extends Day {

    private Grid<String> grid;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
    }

    @Solution("3671")
    @Override
    public Object part1() {
        var start = grid.getAll().stream().filter(c -> c.data().equals("S")).findFirst().get();

        var stepsNeeded = isTest() ? 6 : 64;

        return plotCount(start, stepsNeeded);
    }

    private int plotCount(Grid.Tile<String> start, int stepsNeeded) {
        Set<Line.Point> currents = new HashSet<>(List.of(new Line.Point(start.x(), start.y())));
        for (int i = 0; i < stepsNeeded; i++) {
            Set<Line.Point> nCurrents = new HashSet<>();

            for (Line.Point current : currents) {
                for (Direction direction : Direction.CARDINAL) {
                    var p = current.add(direction.asPoint());
                    var n = grid.getWrappedTile(p.x(), p.y());
                    if (n.data().equals("#")) {
                        continue;
                    }

                    nCurrents.add(p);
                }
            }

            currents = nCurrents;
        }

        return currents.size();
    }

    @Solution("")
    @Override
    public Object part2() {
        // Some grid statistics
        System.out.printf("Grid size: %d x %d\n", grid.width, grid.height);
        var start = grid.getAll().stream().filter(c -> c.data().equals("S")).findFirst().get();
        System.out.printf("Start: %s\n", start);

        var x0 = 65;
        var x1 = 65+131;
        var x2 = 65+131*2;

        var y0 = plotCount(start, x0);
        var y1 = plotCount(start, x1);
        var y2 = plotCount(start, x2);

        System.out.printf("y0: %d\n", y0);
        System.out.printf("y1: %d\n", y1);
        System.out.printf("y2: %d\n", y2);

        var fitter = PolynomialCurveFitter.create(2);
        WeightedObservedPoints obs = new WeightedObservedPoints();
        obs.add(0, y0); // 65 * 131 * 0
        obs.add(1, y1); // 65 * 131 * 1
        obs.add(2, y2); // 65 * 131 * 2
        var _coeffs = fitter.fit(obs.toList());

        var coeffs = Arrays.stream(_coeffs).mapToLong(Math::round).toArray();

        System.out.printf("Coefficients: %s\n", Arrays.toString(coeffs));

        // 26501365 = 202300 * 131 + 65
        double xn = 202300;

        return (long) (coeffs[0] + coeffs[1] * xn + coeffs[2] * xn * xn);
    }

    @Override
    public int getDay() {
        return 21;
    }
}
