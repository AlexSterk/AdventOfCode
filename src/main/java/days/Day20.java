package days;

import setup.Day;
import util.Grid;
import util.Grid.InfiniteGrid;

public class Day20 extends Day {

    private InfiniteGrid<Character> grid;
    private String lookupTable;


    @Override
    public void processInput() {
        String[] split = input.split("\n\n");
        lookupTable = split[0];
        String[] lines = split[1].split("\n");
        grid = new InfiniteGrid<>(() -> '.');
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                grid.set(x, y, lines[y].charAt(x));
            }
        }
    }

    @Override
    public Object part1() {
        for (int i = 0; i < 2; i++) {
            int minX = grid.minX();
            int minY = grid.minY();
            int maxX = grid.maxX();
            int maxY = grid.maxY();

//            System.out.println(grid);
            var copy = grid.copy();

            for (int y = minY - 1; y <= maxY + 1; y++)
                for (int x = minX - 1; x <= maxX + 1; x++) {
//                    System.out.println(x + ", " + y);
                    Grid<Character> subgrid = copy.subgrid(x - 1, x + 1, y - 1, y + 1);
//                    System.out.println(subgrid);
//                    System.out.println();
                    String bits = subgrid.toString().replaceAll("\n", "").replaceAll("\\.", "0").replaceAll("#", "1");
                    int index = Integer.parseInt(bits, 2);
                    grid.set(x, y, lookupTable.charAt(index));
                }
        }

        System.out.println(grid);

        return grid.getAll().stream().filter(t -> t.data() == '#').count();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public boolean isTest() {
        return true;
    }
}
