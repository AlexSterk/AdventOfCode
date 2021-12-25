package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day7 extends Day {

    private List<String> ips;

    @Override
    public void processInput() {
        ips = Arrays.stream(input.split("\n")).toList();
    }

    @Override
    public Object part1() {
        Pattern abba = Pattern.compile("(\\w)(\\w)\\2\\1");
        Pattern hypernet = Pattern.compile("\\[.*?]");

        return ips.stream().filter(ip -> {
            var abbas = abba.matcher(ip).results().map(MatchResult::group).filter(s -> s.charAt(0) != s.charAt(1)).toList();
            var hypernets = hypernet.matcher(ip).results().map(MatchResult::group).toList();

            if (abbas.isEmpty()) return false;
            for (String m : abbas) {
                for (String h : hypernets) {
                    if (h.contains(m)) return false;
                }
            }

            return true;
        }).count();
    }

    @Override
    public Object part2() {
        Pattern aba = Pattern.compile("(?=((.)(?!\\2).\\2)).");
        Pattern hypernet = Pattern.compile("\\[.*?]");

        return ips.stream().filter(ip -> {
            var abas = aba.matcher(ip).results().toList();
            var hypernets = hypernet.matcher(ip).results().toList();

            for (MatchResult m : abas) {
                String a = m.group(1);
                if (hypernets.stream().anyMatch(h -> m.start() > h.start() && m.end() < h.end())) continue;

                for (MatchResult h : hypernets) {
                    String bab = "" + a.charAt(1) + a.charAt(0) + a.charAt(1);
                    if (h.group().contains(bab)) return true;
                }
            }

            return false;
        }).count();
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "115";
    }

    @Override
    public String partTwoSolution() {
        return "231";
    }
}
