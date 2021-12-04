package days;

import setup.Day;

import java.util.*;

public class Day7 extends Day {
    private Map<String, Set<String>> requires;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        requires = new HashMap<>();
        for (String line : lines) {
            String oneThatIsRequired = line.substring(5,6);
            String oneThatRequires = line.substring(36,37);
            requires.putIfAbsent(oneThatRequires, new HashSet<>());
            requires.putIfAbsent(oneThatIsRequired, new HashSet<>());
            requires.get(oneThatRequires).add(oneThatIsRequired);
        }
    }

    @Override
    public Object part1() {
//        System.out.println(requires);
        List<String> completed = new ArrayList<>();

        while (!requires.isEmpty()) {
            String todo = requires.entrySet().stream().filter(e -> e.getValue().isEmpty()).map(Map.Entry::getKey).sorted().findFirst().get();
            requires.remove(todo);
            completed.add(todo);
            requires.forEach((k, s) -> s.remove(todo));
        }

        return String.join("", completed);
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        List<String> completed = new ArrayList<>();
        int WORKERS = 5;
        int EXTRA_TIME = 60;
        int[] seconds = new int[WORKERS];
        String[] workingOn = new String[WORKERS];
        int totalSeconds = 0;
        int tasks = requires.size();
        while (completed.size() != tasks) {
            System.out.print(" ".repeat(5 - Integer.toString(totalSeconds).length()) + totalSeconds + " ");
            for (int i = 0; i < WORKERS; i++) {
                if (seconds[i] > 0) seconds[i]--;
                if (seconds[i] == 0) {
                    if (workingOn[i] != null) {
                        completed.add(workingOn[i]);
                        for (Map.Entry<String, Set<String>> entry : requires.entrySet()) {
                            Set<String> s = entry.getValue();
                            s.remove(workingOn[i]);
                        }
                        workingOn[i] = null;
                    }
                    var todo = requires.entrySet().stream().filter(e -> e.getValue().isEmpty()).map(Map.Entry::getKey).sorted().findFirst();
                    if (todo.isPresent()) {
                        String toDo = todo.get();
                        workingOn[i] = toDo;
                        seconds[i] = EXTRA_TIME + toDo.charAt(0) - 64;
                        requires.remove(toDo);
                    }
                }
                System.out.print(workingOn[i] == null ? "." : workingOn[i]);
                System.out.print(" ");
            }
            System.out.println(String.join("", completed));
            totalSeconds += 1;
        }
        return totalSeconds - 2;
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
