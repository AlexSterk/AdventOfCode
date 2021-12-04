package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private Grid<Integer> grid;

    @BeforeEach
    void setUp() {
        grid = new Grid<>(3, 3);
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                grid.set(x, y, ++i);
            }
        }
    }

    @Test
    void getColumn0() {
        assertEquals(grid.getColumn(0).stream().map(Grid.Tile::data).toList(), List.of(1, 4, 7));
    }

    @Test
    void getColumn1() {
        assertEquals(grid.getColumn(1).stream().map(Grid.Tile::data).toList(), List.of(2, 5, 8));
    }

    @Test
    void getColumn2() {
        assertEquals(grid.getColumn(2).stream().map(Grid.Tile::data).toList(), List.of(3, 6, 9));
    }

    @Test
    void getColumns() {
        assertEquals(grid.getColumns().stream().map(l -> l.stream().map(Grid.Tile::data).toList()).toList(), List.of(
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(3, 6, 9)
        ));
    }
}
