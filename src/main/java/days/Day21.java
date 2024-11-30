package days;

import setup.Day;
import util.Dijkstra;
import util.Grid;

import java.util.*;

import static util.Annotations.Solution;
import static util.Annotations.TestInput;

@TestInput
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

        Set<Grid.Tile<String>> currents = new HashSet<>(List.of(start));
        for (int i = 0; i < stepsNeeded; i++) {
            Set<Grid.Tile<String>> nCurrents = new HashSet<>();

            for (Grid.Tile<String> current : currents) {
                var neighbours = grid.getAdjacent(current, false);
                for (Grid.Tile<String> neighbour : neighbours) {
                    if (neighbour.data().equals("#")) {
                        continue;
                    }

                    nCurrents.add(neighbour);
                }
            }

            currents = nCurrents;
        }

        return currents.size();
    }

    @Solution("")
    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 21;
    }
}
