package days;

import setup.Day;
import util.Graph;
import util.Grid;
import util.Grid.Tile;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "unchecked", "rawtypes"})
public class Day12 extends Day {

    private Graph<Tile<WeightedString>> graph;
    private Tile start;
    private Tile end;

    @Override
    public void processInput() {
        Grid<String> grid = Grid.parseGrid(input);

        start = grid.getAll().stream().filter(t -> t.data().equals("S")).findFirst().map(t -> {
            grid.set(t, "a");
            return grid.getTile(t.x(), t.y());
        }).get();
        end = grid.getAll().stream().filter(t -> t.data().equals("E")).findFirst().map(t -> {
            grid.set(t, "z");
            return grid.getTile(t.x(), t.y());
        }).get();
        Grid<WeightedString> map = grid.map(WeightedString::new);

        start = map.getTile(start.x(), start.y());
        end = map.getTile(end.x(), end.y());

        graph = Grid.gridToGraph(map);


        graph.nodes().forEach(n -> {
            Set<Tile<WeightedString>> neighbours = new HashSet<>(graph.getNeighbours(n));
            neighbours.forEach(m -> {
                if (m.data().s.charAt(0) > n.data().s.charAt(0) + 1) {
                    graph.removeEdge(n, m, true);
                }
            });
        });
    }

    @Override
    public Object part1() {
        return graph.getDistance(start, end);
    }

    @Override
    public Object part2() {
        return graph.nodes().stream().filter(n -> n.data().s.equals("a")).map(n -> graph.getDistance(n, end)).min(Integer::compareTo).get();
    }

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "472";
    }

    @Override
    public String partTwoSolution() {
        return "465";
    }

    private record WeightedString(String s) implements Grid.Weighted {

        @Override
            public Integer getWeight() {
                return 1;
            }

            @Override
            public String toString() {
                return s;
            }
        }
}
