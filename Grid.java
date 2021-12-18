package util;

import com.sun.source.tree.Tree;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Grid<T> {

    public final int width, height;
    private final Map<Integer, Map<Integer, Tile<T>>> grid;
    private Supplier<T> empty;

    public Grid(int w, int h) {
        this.width = w;
        this.height = h;
        this.grid = new TreeMap<>();
        for (int y = 0; y < h; y++) {
            this.grid.put(y, new TreeMap<>());
        }
    }

    public Grid(T[][] array) {
        this(array[0].length, array.length);
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                set(x, y, array[y][x]);
            }
        }
    }

    public Grid(int w, int h, Supplier<T> empty) {
        this(w, h);
        this.empty = empty;
    }

    public static <T extends Weighted> Graph<Tile<T>> gridToGraph(Grid<T> grid) {
        Graph<Tile<T>> graph = new Graph<>();

        for (Tile<T> tTile : grid.getAll()) {
            graph.addNode(tTile);
        }

        for (Tile<T> tTile : grid.getAll()) {
            for (Tile<T> tile : grid.getAdjacent(tTile, false)) {
                graph.addEdge(tTile, tile, tile.data.getWeight(), true);
            }
        }

        return graph;
    }

    public void init(Supplier<T> data, boolean overwrite) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (_getTile(x, y) == null || overwrite) set(x, y, data.get());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid<?> grid1 = (Grid<?>) o;
        return grid.equals(grid1.grid);
    }

    @Override
    public int hashCode() {
        return grid.hashCode();
    }

    public void set(int x, int y, T data) {
        grid.get(y).put(x, new Tile<>(x, y, data, this));
    }

    public void set(Tile<T> tile, T data) {
        set(tile.x, tile.y, data);
    }

    public Tile<T> getTile(int x, int y) {
        Tile<T> tTile = _getTile(x, y);
        if (tTile == null && empty != null) {
            Tile<T> t = new Tile<>(x, y, empty.get(), this);
            grid.get(y).put(x, t);
            return t;
        }
        return tTile;
    }

    private Tile<T> _getTile(int x, int y) {
        return grid.get(y).get(x);
    }

    public List<Tile<T>> getAll() {
        return grid.values().stream().flatMap(integerTileMap -> integerTileMap.values().stream()).toList();
    }

    public Grid<T> copy() {
        Grid<T> g = new Grid<>(width, height, empty);
        getAll().forEach(t -> g.set(t, t.data));
        return g;
    }

    public Grid<T> subgrid(int xStart, int xEnd, int yStart, int yEnd) {
        Grid<T> sub = new Grid<>(xEnd - xStart + 1, yEnd - yStart + 1, empty);
        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                Tile<T> t = getTile(x, y);
                if (t != null) sub.set(t.x - xStart, t.y - yStart, t.data);
            }
        }
        return sub;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Grid<T> subgridNoNull() {
        int minX = getAll().stream().filter(Objects::nonNull).min(Comparator.comparing(Tile::x)).get().x();
        int maxX = getAll().stream().filter(Objects::nonNull).max(Comparator.comparing(Tile::x)).get().x();
        int minY = getAll().stream().filter(Objects::nonNull).min(Comparator.comparing(Tile::y)).get().y();
        int maxY = getAll().stream().filter(Objects::nonNull).max(Comparator.comparing(Tile::y)).get().y();

        return subgrid(minX, maxX, minY, maxY);
    }

    public Set<Tile<T>> getAdjacent(Tile<T> tile, boolean includeDiagonals) {
        Set<Tile<T>> set = new HashSet<>();
        set.add(tile.up());
        set.add(tile.down());
        set.add(tile.left());
        set.add(tile.right());
        if (includeDiagonals) {
            Tile<T> left = tile.left();
            if (left != null) {
                set.add(left.up());
                set.add(left.down());
            }
            Tile<T> right = tile.right();
            if (right != null) {
                set.add(right.up());
                set.add(right.down());
            }
        }
        set.remove(null);
        return set;
    }

    public List<Tile<T>> getRow(int y) {
        return new ArrayList<>(grid.get(y).values());
    }

    public List<Tile<T>> getColumn(int x) {
        List<Tile<T>> arr = new ArrayList<>();
        grid.keySet().forEach(y -> arr.add(grid.get(y).get(x)));
        return arr;
    }

    public List<List<Tile<T>>> getRows() {
        return IntStream.range(0, height).mapToObj(this::getRow).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<List<Tile<T>>> getColumns() {
        return IntStream.range(0, width).mapToObj(this::getColumn).collect(Collectors.toCollection(ArrayList::new));
    }

    public Set<Grid<T>> allVariations() {
        Set<Grid<T>> set = new HashSet<>();
        for (Grid<T> g : List.of(this, this.flip(false))) {
            set.add(g);
            set.add(g.rotate());
            set.add(g.rotate().rotate());
            set.add(g.rotate().rotate().rotate());
        }
        return set;
    }

    @Override
    public String toString() {
        return grid.values().stream().map(l -> l.values().stream().map(t -> t == null ? "\033[31m\u25A1\033[0m" : t.data.toString()).collect(Collectors.joining())).collect(Collectors.joining("\n"));
    }

    public String toString(Map<Tile<T>, String> custom) {
        return grid.values().stream().map(l -> l.values().stream().map(t -> {
            if (custom.containsKey(t)) return custom.get(t);
            return t == null ? "\033[31m\u25A1\033[0m" : t.data.toString();
        }).collect(Collectors.joining())).collect(Collectors.joining("\n"));
    }

    public Grid<T> rotate() {
        Grid<T> grid = new Grid<>(height, width);

        List<List<Tile<T>>> columns = this.getColumns();
        columns.forEach(Collections::reverse);
        for (int y = 0; y < columns.size(); y++) {
            for (int x = 0; x < columns.get(y).size(); x++) {
                grid.set(x, y, columns.get(y).get(x).data);
            }
        }

        return grid;
    }

    public Grid<T> flip(boolean vertical) {
        Grid<T> copy = new Grid<>(width, height);

        if (vertical) {
            List<List<Tile<T>>> rows = getRows();
            Collections.reverse(rows);
            for (int y = 0; y < rows.size(); y++) {
                for (Tile<T> t : rows.get(y)) {
                    copy.set(t.x, y, t.data);
                }
            }
        } else {
            List<List<Tile<T>>> columns = getColumns();
            Collections.reverse(columns);
            for (int x = 0; x < columns.size(); x++) {
                for (Tile<T> t : columns.get(x)) {
                    copy.set(x, t.y, t.data);
                }
            }
        }
        return copy;
    }

    public interface Weighted {
        Integer getWeight();
    }

    public record Tile<T>(int x, int y, T data, Grid<T> grid) {

        public Tile<T> up() {
            return y - 1 >= 0 ? grid.getTile(x, y - 1) : null;
        }

        public Tile<T> down() {
            return y + 1 < grid.height ? grid.getTile(x, y + 1) : null;
        }

        public Tile<T> left() {
            return x - 1 >= 0 ? grid.getTile(x - 1, y) : null;
        }

        public Tile<T> right() {
            return x + 1 < grid.width ? grid.getTile(x + 1, y) : null;
        }

        @Override
        public String toString() {
            return "Tile{" + "x=" + x + ", y=" + y + ", data=" + data + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile<?> tile = (Tile<?>) o;
            return x == tile.x && y == tile.y && Objects.equals(data, tile.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, data);
        }
    }
}
