package days;

import setup.Day;
import util.Pair;

import java.util.List;
import java.util.stream.Stream;

public class Day2 extends Day {
    List<Game> games;

    @Override
    public void processInput() {
        this.games = this.lines().stream().map(s -> {
            var split = s.split(":");
            var id = Integer.parseInt(split[0].replaceAll("\\D", ""));
            s = split[1].trim();

            var subsets = Stream.of(s.split(";")).map(set -> {
                var split2 = set.split(",");
                var pairs = Stream.of(split2).map(pair -> {
                    pair = pair.trim();
                    var split3 = pair.split(" ");
                    var num = Integer.parseInt(split3[0]);
                    var color = split3[1];
                    return new Pair<>(num, color);
                }).toList();
                return pairs;
            }).toList();

            return new Game(id, subsets);

        }).toList();
    }

    @Override
    public Object part1() {
        var possible = this.games.stream().filter(g -> g.isPossible(12, 13, 14)).toList();

        return possible.stream().mapToInt(Game::id).sum();
    }

    @Override
    public Object part2() {
        return this.games.stream().mapToLong(Game::power).sum();
    }

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "2810";
    }

    @Override
    public String partTwoSolution() {
        return "69110";
    }

    record Game(int id, List<List<Pair<Integer, String>>> subsets) {
        public boolean isPossible(int red, int green, int blue) {
            var maxRed = this.max("red");
            var maxGreen = this.max("green");
            var maxBlue = this.max("blue");

            System.out.println(maxRed + " " + maxGreen + " " + maxBlue);

            return maxRed <= red && maxGreen <= green && maxBlue <= blue;
        }

        private int max(String color) {
            return this.subsets.stream().flatMap(List::stream).filter(p -> p.b().equals(color)).mapToInt(Pair::a).max().orElse(0);
        }

        public long power() {
            var r = (long) this.max("red");
            var g = (long) this.max("green");
            var b = (long) this.max("blue");

            return r * g * b;
        }
    }
}
