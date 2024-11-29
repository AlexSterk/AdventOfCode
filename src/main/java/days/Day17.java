package days;

import setup.Day;
import util.Dijkstra;
import util.Direction;
import util.Grid;

import java.util.List;
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
                new State(start, Direction.E, 0),
                s -> s.position().equals(end),
                State::neighbors,
                (c, s) -> Integer.parseInt(s.position.data())
        );

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
        return true;
    }

    private record State(Grid.Tile<String> position, Direction dir, int stepsTakenInDir) {
        public State(Grid.Tile<String> position, Direction dir, int stepsTakenInDir) {
            this.position = position;
            this.dir = dir;
            this.stepsTakenInDir = stepsTakenInDir;
        }

        public State move() {
            return new State(position.add(dir.asPoint()), dir, stepsTakenInDir + 1);
        }

        public State turn(Direction dir) {
            return new State(position, dir, 0);
        }

        public State turnAndMove(Direction newDir) {
            return turn(newDir).move();
        }

        public boolean isValid() {
            return position != null && stepsTakenInDir < 3;
        }

        public List<State> neighbors() {
            return Stream.of(
                    move(),
                    turnAndMove(dir.left()),
                    turnAndMove(dir.right())
            ).filter(State::isValid).toList();
        }
    }

//    private record Num(int n) implements Grid.Weighted {
//
//        @Override
//        public Integer getWeight() {
//            return n;
//        }
//    }
//
//    private record Configuration(List<Grid.Tile<Num>> path, Direction direction) {
//        public boolean isValid() {
//            if (path.size() < 3) return true;
//            var last = path.subList(path.size() - 3, path.size());
//
//            // invalid if they all have the same X or same Y coordinate
//            var x = last.stream().map(Grid.Tile::x).distinct().count();
//            var y = last.stream().map(Grid.Tile::y).distinct().count();
//
//            return x != 1 && y != 1;
//        }
//
//        public int getWeight() {
//            return path.stream().skip(1).mapToInt(t -> t.data().n).sum();
//        }
//    }
}
