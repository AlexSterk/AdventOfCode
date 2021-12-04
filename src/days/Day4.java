package days;

import setup.Day;
import util.Grid;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Day4 extends Day {
    private List<Integer> numbers;
    private List<Grid<BingoNumber>> boards;

    @Override
    public void processInput() {
        String[] parts = input.split("\n\n");
        numbers = Arrays.stream(parts[0].split(",")).map(Integer::parseInt).toList();
        boards = Arrays.stream(parts)
                .skip(1)
                .map(s -> {
                    Grid<BingoNumber> b = new Grid<>(5, 5);
                    String[] lines = s.split("\n");
                    for (int y = 0; y < lines.length; y++) {
                        String[] line = lines[y].trim().split(" +");
                        for (int x = 0; x < line.length; x++) {
                            b.set(x, y, new BingoNumber(Integer.parseInt(line[x])));
                        }
                    }
                    return b;
                })
                .toList();
    }

    @Override
    public Object part1() {
        for (Integer number : numbers) {
            System.out.println(number + "\n");
            boards.forEach(x -> System.out.println(x + "\n"));
            System.out.println("=================\n");
            for (Grid<BingoNumber> board : boards) {
                Optional<Grid.Tile<BingoNumber>> first = board.getAll().stream().filter(t -> t.data().number.equals(number)).findFirst();
                first.ifPresent(t -> board.set(t, new BingoNumber(number, true)));
            }
            Optional<List<Grid.Tile<BingoNumber>>> winningBoard = boards.stream()
                    .flatMap(b -> Stream.concat(b.getRows().stream(), b.getColumns().stream()))
                    .filter(r -> r.stream().allMatch(t -> t.data().marked))
                    .findFirst();
            if (winningBoard.isPresent()) {
                var board = winningBoard.get().get(0).grid();
                System.out.println(board);
                int unmarkedSum = board.getAll().stream().filter(t -> !t.data().marked()).mapToInt(t -> t.data().number).sum();
                return unmarkedSum * number;
            }
        }
        throw new RuntimeException("No winning board found");
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        for (Integer number : numbers) {
            System.out.println(number + "\n");
            boards.forEach(x -> System.out.println(x + "\n"));
            System.out.println("=================\n");
            for (Grid<BingoNumber> board : boards) {
                Optional<Grid.Tile<BingoNumber>> first = board.getAll().stream().filter(t -> t.data().number.equals(number)).findFirst();
                first.ifPresent(t -> board.set(t, new BingoNumber(number, true)));
            }
            Optional<List<Grid.Tile<BingoNumber>>> winningBoard = boards.stream()
                    .flatMap(b -> Stream.concat(b.getRows().stream(), b.getColumns().stream()))
                    .filter(r -> r.stream().allMatch(t -> t.data().marked))
                    .findFirst();
            if (winningBoard.isPresent()) {
                var board = winningBoard.get().get(0).grid();
                System.out.println(board);
                int unmarkedSum = board.getAll().stream().filter(t -> !t.data().marked()).mapToInt(t -> t.data().number).sum();
                return unmarkedSum * number;
            }
        }
        throw new RuntimeException("No winning board found");
    }

//    private boolean winningBoard(Grid<BingoNumber> board) {
//
//    }

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record BingoNumber(Integer number, boolean marked) {
        public BingoNumber(Integer number) {
            this(number, false);
        }

        @Override
        public String toString() {
            String n = (number < 10 ? " " : "") + number + " ";

            if (marked) return "\033[31m" + n + "\033[0m";
            else return n;
        }
    }
}
