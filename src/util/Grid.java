package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid<T> {

    private final List<List<Tile<T>>> grid;
    public final int width, height;

    public Grid(int w, int h) {
        this.width = w;
        this.height = h;
        this.grid = new ArrayList<>(h);
        for (int y = 0; y < h; y++) {
            this.grid.add(new ArrayList<>(Collections.nCopies(w, null)));
        }
    }

    public void set(int x, int y, T data) {
        grid.get(y).set(x, new Tile<>(x, y, data, this));
    }

    public void set(Tile<T> tile, T data) {
        set(tile.x, tile.y, data);
    }

    public Tile<T> getTile(int x, int y) {
        return grid.get(y).get(x);
    }

    public List<Tile<T>> getAll() {
        return grid.stream().flatMap(Collection::stream).toList();
    }

    public List<Tile<T>> getRow(int y) {
        return new ArrayList<>(grid.get(y));
    }

    public List<Tile<T>> getColumn(int x) {
        List<Tile<T>> arr = new ArrayList<>();
        grid.forEach(l -> arr.add(l.get(x)));
        return arr;
    }

    public List<List<Tile<T>>> getRows() {
        return new ArrayList<>(grid);
    }

    public List<List<Tile<T>>> getColumns() {
        return IntStream.range(0, width).mapToObj(this::getColumn).toList();
    }

    @Override
    public String toString() {
        return grid.stream().map(l -> l.stream().map(t -> t.data.toString()).collect(Collectors.joining())).collect(Collectors.joining("\n"));
    }

    public record Tile<T>(int x, int y, T data, Grid<T> grid) {

        public Tile<T> up() {
            return grid.getTile(x, y - 1);
        }

        public Tile<T> down() {
            return grid.getTile(x, y + 1);
        }

        public Tile<T> left() {
            return grid.getTile(x - 1, y);
        }

        public Tile<T> right() {
            return grid.getTile(x + 1, y);
        }

        @Override
        public String toString() {
            return "Tile{" +
                    "x=" + x +
                    ", y=" + y +
                    ", data=" + data +
                    '}';
        }
    }
}
