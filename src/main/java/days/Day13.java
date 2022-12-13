package days;

import setup.Day;

import java.util.*;

public class Day13 extends Day {

    private List<List<CList>> packets;

    @Override
    public void processInput() {
        List<CList> CLists = lines().stream().filter(s -> s.length() > 0).map(CVal::parse).map(c -> (CList) c).toList();

        // group Clists in groups of 2
        packets = new ArrayList<>();
        for (int i = 0; i < CLists.size(); i += 2) {
            List<CList> group = new ArrayList<>();
            group.add(CLists.get(i));
            group.add(CLists.get(i + 1));
            packets.add(group);
        }
    }

    @Override
    public Object part1() {
        int sum = 0;

        for (int i = 0; i < packets.size(); i++) {
            List<CList> packet = packets.get(i);
            CList left = packet.get(0);
            CList right = packet.get(1);

            if (Objects.equals(correctOrder(left, right), true)) {
                sum += i+1;
            }
        }


        return sum;
    }

    private Boolean correctOrder(CVal left, CVal right) {
        if (left instanceof CNum leftNum && right instanceof CNum rightNum) {
            if (leftNum.n < rightNum.n) {
                return true;
            } else if (leftNum.n > rightNum.n) {
                return false;
            }
            return null;
        }
        if (left instanceof CList leftList && right instanceof CList rightList) {
            Queue<CVal> leftItems = new ArrayDeque<>(leftList.items);
            Queue<CVal> rightItems = new ArrayDeque<>(rightList.items);

            Boolean compare = null;

            while (compare == null && !leftItems.isEmpty() && !rightItems.isEmpty()) {
                compare = correctOrder(leftItems.poll(), rightItems.poll());
            }

            if (compare == null) {
                if (leftItems.isEmpty() && rightItems.isEmpty()) {
                    return null;
                } else return leftItems.isEmpty();
            }

            return compare;
        } else if (left instanceof CNum) {
            return correctOrder(new CList(List.of(left)), right);
        } else {
            return correctOrder(left, new CList(List.of(right)));
        }
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "5882";
    }

    private interface CVal {
        private static CVal parse(String s) {
            if (s.startsWith("[")) {
                return CList.parse(s);
            } else {
                return CNum.parse(s);
            }
        }
    }

    private record CNum(int n) implements CVal {
        private static CNum parse(String s) {
            return new CNum(Integer.parseInt(s));
        }

        @Override
        public String toString() {
            return "CNum{" +
                    "n=" + n +
                    '}';
        }
    }

    private record CList(List<CVal> items) implements CVal {
        private static CList parse(String s) {
            String substring = s.substring(1, s.length() - 1);

            List<CVal> items = new ArrayList<>();
            StringBuilder t = new StringBuilder();
            Stack<Character> stack = new Stack<>();

            for (int i = 0; i < substring.length(); i++) {
                char c = substring.charAt(i);
                if (c == '[') {
                    stack.push(c);
                } else if (c == ']') {
                    stack.pop();
                }

                if (c == ',' && stack.isEmpty()) {
                    items.add(CVal.parse(t.toString()));
                    t = new StringBuilder();
                } else {
                    t.append(c);
                }
            }

            if (t.length() > 0) {
                items.add(CVal.parse(t.toString()));
            }

            return new CList(items);
        }

        @Override
        public String toString() {
            return "CList{" +
                    "items=" + items +
                    '}';
        }
    }
}
