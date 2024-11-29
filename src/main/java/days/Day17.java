package days;

import setup.Day;
import util.Dijkstra;
import util.Direction;
import util.Grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day17 extends Day {

    //    private Graph<Grid.Tile<Num>> graph;
    private Grid<String> grid;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
//        graph = Grid.gridToGraph(grid);
    }

    @Override
    public Object part1() {
        var start = grid.getTile(0, 0);
        var end = grid.getTile(grid.width - 1, grid.height - 1);

        var d = Dijkstra.shortestPath(
                new CrucibleState(start, Direction.E, 0, null),
                s -> s.position().equals(end),
                CrucibleState::neighbors,
                (c, s) -> Integer.parseInt(s.position.data())
        );

        return d.get();
    }

    @Override
    public Object part2() {
        var start = grid.getTile(0, 0);
        var end = grid.getTile(grid.width - 1, grid.height - 1);

        var d = Dijkstra.shortestPath(
                new UltraCrucibleState(start, Direction.E, 0, null),
                s -> s.position().equals(end),
                CrucibleState::neighbors,
                (c, s) -> Integer.parseInt(s.position.data())
        );

        return d.get();
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
        return "1238";
    }

    @Override
    public String partTwoSolution() {
        return "1362";
    }

    private static class CrucibleState {
        protected final Grid.Tile<String> position;
        protected final Direction dir;
        protected final int stepsTakenInDir;
        protected final CrucibleState prevState;

        private CrucibleState(Grid.Tile<String> position, Direction dir, int stepsTakenInDir, CrucibleState prevState) {
            this.position = position;
            this.dir = dir;
            this.stepsTakenInDir = stepsTakenInDir;
            this.prevState = prevState;
        }

        public CrucibleState move(Direction nDir) {
            return new CrucibleState(position.add(nDir.asPoint()), nDir, dir == nDir ? stepsTakenInDir + 1 : 1, this);
        }

        public List<CrucibleState> neighbors() {
            var list = new ArrayList<CrucibleState>();

            if (stepsTakenInDir < 3) {
                list.add(move(dir));
            }
            list.add(move(dir.left()));
            list.add(move(dir.right()));

            list.removeIf(s -> s.position == null);

            return list;
        }

        public List<CrucibleState> path() {
            var result = new ArrayList<CrucibleState>();
            var current = this;
            while (current != null) {
                result.add(current);
                current = current.prevState;
            }
            Collections.reverse(result);
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CrucibleState state = (CrucibleState) o;
            return stepsTakenInDir == state.stepsTakenInDir && dir == state.dir && Objects.equals(position, state.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, dir, stepsTakenInDir);
        }

        @Override
        public String toString() {
            return "State{" +
                    "position=" + position +
                    ", dir=" + dir +
                    ", stepsTakenInDir=" + stepsTakenInDir +
                    '}';
        }

        public Grid.Tile<String> position() {
            return position;
        }
    }

    private static class UltraCrucibleState extends CrucibleState {
        private UltraCrucibleState(Grid.Tile<String> position, Direction dir, int stepsTakenInDir, UltraCrucibleState prevState) {
            super(position, dir, stepsTakenInDir, prevState);
        }

        @Override
        public CrucibleState move(Direction nDir) {
            return new UltraCrucibleState(position.add(nDir.asPoint()), nDir, dir == nDir ? stepsTakenInDir + 1 : 1, this);
        }

        @Override
        public List<CrucibleState> neighbors() {
            var list = new ArrayList<CrucibleState>();

            if (stepsTakenInDir < 10) {
                list.add(move(dir));
            }

            if (stepsTakenInDir >= 4 || stepsTakenInDir == 0) {
                list.add(move(dir.left()));
                list.add(move(dir.right()));
            }

            list.removeIf(s -> s.position == null);
            return list;
        }
    }
}
