package days;

import setup.Day;

import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day25 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        TuringMachine machine = new TuringMachine(input);

        machine.run();

        return machine.checkSum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class TuringMachine {
        private final int checkAt;
        private final Map<String, State> states = new HashMap<>();
        private State currentState;
        private int steps = 0;
        private final Map<Integer, Integer> tape = new HashMap<>() {
            @Override
            public Integer get(Object key) {
                return super.getOrDefault(key, 0);
            }
        };
        private int cursor = 0;

        private void run() {
            while (steps < checkAt) {
                for (Action action : currentState.actions.get(tape.get(cursor))) {
                    action.execute();
                }
                steps++;
            }
        }

        private int checkSum() {
            return tape.values().stream().mapToInt(i -> i).sum();
        }

        private TuringMachine(String description) {
            Map<Pattern, Function<String, Action>> actionMap = Map.of(
                    Pattern.compile("Write.* (.+)\\."), (String g) -> new Write(Integer.parseInt(g)),
                    Pattern.compile("Move.*to the (.+)\\."), (String g) -> new Move(g.equals("left")),
                    Pattern.compile("Continue.*state (.+)\\."), Continue::new
            );

            String[] states = description.split("\n\n");
            MatchResult prefix = Pattern.compile("Begin in state (.+?)\\..*?(\\d+)", Pattern.DOTALL).matcher(states[0]).results().findFirst().get();
            checkAt = Integer.parseInt(prefix.group(2));
            String start = prefix.group(1);

            for (int i = 1; i < states.length; i++) {
                String s = states[i];
                String name = Pattern.compile("In state (.+?):").matcher(s).results().findFirst().get().group(1);
                Map<Integer, List<Action>> actions = new HashMap<>();

                List<MatchResult> conditions = Pattern.compile("If .*(\\d+):").matcher(s).results().toList();
                for (int j = 0; j < conditions.size(); j++) {
                    MatchResult condition = conditions.get(j);
                    List<Action> actionsList = new ArrayList<>();
                    String acts;
                    if (j == conditions.size() - 1) acts = s.substring(condition.end());
                    else acts = s.substring(condition.end(), conditions.get(j + 1).start());
                    acts = acts.trim();
                    for (String act : acts.split("\n")) {
                        actionMap.entrySet().stream().map(e -> {
                            Optional<MatchResult> first = e.getKey().matcher(act).results().findFirst();
                            return first.map(matchResult -> e.getValue().apply(matchResult.group(1))).orElse(null);
                        }).filter(Objects::nonNull).findFirst().ifPresent(actionsList::add);
                    }

                    actions.put(Integer.parseInt(condition.group(1)), actionsList);
                }

                State state = new State(actions);
                this.states.put(name, state);
            }

            currentState = this.states.get(start);
        }

        private record State(Map<Integer, List<Action>> actions) {

        }

        private abstract static class Action {
            @Override
            public abstract String toString();

            abstract void execute();
        }

        private class Write extends Action {
            private final int value;

            private Write(int value) {
                this.value = value;
            }

            @Override
            void execute() {
                tape.put(cursor, value);
            }

            @Override
            public String toString() {
                return "Write{" +
                        "value=" + value +
                        '}';
            }
        }

        private class Move extends Action {
            private final boolean left;
            private final int slots;

            private Move(boolean left) {
                this.left = left;
                this.slots = 1;
            }

            private Move(boolean left, int slots) {
                this.left = left;
                this.slots = slots;
            }

            @Override
            void execute() {
                cursor += (left ? -1 : 1) * slots;
            }

            @Override
            public String toString() {
                return "Move{" +
                        "left=" + left +
                        ", slots=" + slots +
                        '}';
            }
        }

        private class Continue extends Action {
            private final String newState;

            private Continue(String newState) {
                this.newState = newState;
            }

            @Override
            void execute() {
                currentState = states.get(newState);
            }

            @Override
            public String toString() {
                return "Continue{" +
                        "newState='" + newState + '\'' +
                        '}';
            }
        }
    }
}
