package days;

import setup.Day;

import java.util.*;

public class Day24 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        ALU alu = new ALU(input);
        alu.input.addAll(Collections.nCopies(14, 1L));
        alu.run();
        return null;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class ALU {
        private final Map<String, Long> variables = new HashMap<>() {
            @Override
            public Long get(Object key) {
                return super.getOrDefault(key, 0L);
            }
        };
        public final Queue<Long> input = new ArrayDeque<>();
        private final List<Instruction> instructions;

        private ALU(String program) {
            instructions = Arrays.stream(program.split("\n")).map(this::parse).toList();
        }

        private Instruction parse(String s) {
            String[] split = s.split(" ");
            return switch (split[0]) {
                case "inp" -> new Input(split[1]);
                case "add" -> new Add(split[1], split[2]);
                case "mul" -> new Mul(split[1], split[2]);
                case "div" -> new Div(split[1], split[2]);
                case "mod" -> new Mod(split[1], split[2]);
                case "eql" -> new Eql(split[1], split[2]);
                default -> throw new IllegalStateException("Unexpected value: " + split[0]);
            };
        }

        private void run() {
            for (Instruction instruction : instructions) {
                instruction.execute();
            }
        }

        private void reset() {
            variables.clear();
            input.clear();
        }

        private abstract class Instruction {
            Long resolve(String exp) {
                if (exp.startsWith("-") || exp.chars().allMatch(Character::isDigit)) return Long.parseLong(exp);
                else return variables.get(exp);
            }

            @Override
            public abstract String toString();

            abstract void execute();
        }

        private class Input extends Instruction {
            private final String var;

            private Input(String var) {
                this.var = var;
            }

            @Override
            void execute() {
                variables.put(var, input.poll());
            }

            @Override
            public String toString() {
                return "Input{" +
                        "var='" + var + '\'' +
                        '}';
            }
        }

        private class Add extends Instruction {
            private final String a, b;

            private Add(String a, String b) {
                this.a = a;
                this.b = b;
            }

            @Override
            void execute() {
                variables.put(a, variables.get(a) + resolve(b));
            }

            @Override
            public String toString() {
                return "Add{" +
                        "a='" + a + '\'' +
                        ", b='" + b + '\'' +
                        '}';
            }
        }

        private class Mul extends Instruction {
            private final String a, b;

            private Mul(String a, String b) {
                this.a = a;
                this.b = b;
            }

            @Override
            public String toString() {
                return "Mul{" +
                        "a='" + a + '\'' +
                        ", b='" + b + '\'' +
                        '}';
            }

            @Override
            void execute() {
                variables.put(a, variables.get(a) * resolve(b));
            }
        }

        private class Div extends Instruction {
            private final String a, b;

            @Override
            public String toString() {
                return "Div{" +
                        "a='" + a + '\'' +
                        ", b='" + b + '\'' +
                        '}';
            }

            private Div(String a, String b) {
                this.a = a;
                this.b = b;
            }

            @Override
            void execute() {
                variables.put(a, variables.get(a) / resolve(b));
            }
        }

        private class Mod extends Instruction {
            private final String a, b;

            @Override
            public String toString() {
                return "Mod{" +
                        "a='" + a + '\'' +
                        ", b='" + b + '\'' +
                        '}';
            }

            private Mod(String a, String b) {
                this.a = a;
                this.b = b;
            }

            @Override
            void execute() {
                variables.put(a, variables.get(a) % resolve(b));
            }
        }

        private class Eql extends Instruction {
            private final String a, b;

            @Override
            public String toString() {
                return "Eql{" +
                        "a='" + a + '\'' +
                        ", b='" + b + '\'' +
                        '}';
            }

            private Eql(String a, String b) {
                this.a = a;
                this.b = b;
            }

            @Override
            void execute() {
                variables.put(a, Objects.equals(variables.get(a), resolve(b)) ? 1L : 0L);
            }
        }
    }
}
