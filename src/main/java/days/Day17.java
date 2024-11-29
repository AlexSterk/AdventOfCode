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
                new State(start, Direction.E, 0, null),
                s -> s.position().equals(end),
                State::neighbors,
                (c, s) -> Integer.parseInt(s.position.data())
        );

        for (State state : d.end().path()) {
            System.out.println(state);
        }

        return d.get();
    }

    @Override
    public Object part2() {
        return null;
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

    private record State(Grid.Tile<String> position, Direction dir, int stepsTakenInDir, State prevState) {
        public State move(Direction nDir) {
            return new State(position.add(nDir.asPoint()), nDir,   dir == nDir ? stepsTakenInDir + 1 : 1, this);
        }

        public boolean isValid() {
            return position != null && stepsTakenInDir <= 3;
        }

        public List<State> neighbors() {
            return Stream.of(
                    move(dir),
                    move(dir.left()),
                    move(dir.right())
            ).filter(State::isValid).toList();
        }

        public List<State> path() {
            var result = new ArrayList<State>();
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
            State state = (State) o;
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
    }
}
