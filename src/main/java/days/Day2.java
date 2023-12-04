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
        return null;
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

    record Game(int id, List<List<Pair<Integer, String>>> subsets) {
        public boolean isPossible(int red, int green, int blue) {
            var maxRed = this.subsets.stream().flatMap(List::stream).filter(p -> p.b().equals("red")).mapToInt(Pair::a).max().orElse(0);
            var maxGreen = this.subsets.stream().flatMap(List::stream).filter(p -> p.b().equals("green")).mapToInt(Pair::a).max().orElse(0);
            var maxBlue = this.subsets.stream().flatMap(List::stream).filter(p -> p.b().equals("blue")).mapToInt(Pair::a).max().orElse(0);

            return maxRed <= red && maxGreen <= green && maxBlue <= blue;
        }
    }
}
