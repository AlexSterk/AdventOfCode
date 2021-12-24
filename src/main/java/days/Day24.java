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

//        try {
//            Files.write(Path.of("data/day24/program.txt"), alu.instructions.stream().map(ALU.Instruction::toJava).toList());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return alu.variables.get("z");
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

            abstract String toJava();
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
            String toJava() {
                return "%s = input();".formatted(var);
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
            String toJava() {
                return "%s += %s;".formatted(a, b);
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
            String toJava() {
                return "%s *= %s;".formatted(a, b);
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

            @Override
            String toJava() {
                return "%s /= %s;".formatted(a, b);
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

            @Override
            String toJava() {
                return "%s %%= %s;".formatted(a, b);
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

            @Override
            String toJava() {
                return "%s = %s == %s ? 1L : 0L;".formatted(a, a, b);
            }
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static class ALU2 {
        void run() {
            run(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        }


        void run(long A, long B, long C, long D, long E, long F, long G, long H, long I, long J, long K, long L, long M, long N) {
            long z;

            long z1 = (A * 676 + (B * 26) + C + 718) / 26;
            z = z1 * 25 * (z1 % 26 - 8 != D ? 1L : 0L) + z1;

            z = (z + (D + 5) * (z % 26 - 8 != D ? 1L : 0L)) / 26;
            z = z * 25 * (z % 26 - 4 != E ? 1L : 0L) + z;
            z = z + (E + 9) * (z % 26 - 4 != E ? 1L : 0L);

            z = z * 25 + z + F + 3;

            z = z * 25 + z + G + 2;

            z = z * 25 + z + H + 15;

            z /= 26;
            z = z * 25 * ((z % 26 - 13 == I ? 1L : 0L) == 0 ? 1L : 0L) + z;
            z = z + (I + 5) * ((z % 26 - 13 == I ? 1L : 0L) == 0 ? 1L : 0L);

            z /= 26;
            z = z * 25 * ((z % 26 - 3 == J ? 1L : 0L) == 0 ? 1L : 0L) + z;
            z = z + (J + 11) * ((z % 26 - 3 == J ? 1L : 0L) == 0 ? 1L : 0L);

            z /= 26;
            z = z * 25 * (((z) % 26 - 7 == K ? 1L : 0L) == 0 ? 1L : 0L) + z;
            z = z + (K + 7) * (((z) % 26 - 7 == K ? 1L : 0L) == 0 ? 1L : 0L);

            z = z * 26;
            z = z + L + 1;

            z /= 26;
            z = z * 25 * ((z % 26 - 6 == M ? 1L : 0L) == 0 ? 1L : 0L) + z;
            z = z + (M + 10) * ((z % 26 - 6 == M ? 1L : 0L) == 0 ? 1L : 0L);

            z /= 26;
            z = z * 25 * ((z % 26 - 8 == N ? 1L : 0L) == 0 ? 1L : 0L) + z;
            z = z + (N + 3) * ((z % 26 - 8 == N ? 1L : 0L) == 0 ? 1L : 0L);

            // For valid input z == 0;

            System.out.println(z);
        }

        public static void main(String[] args) {
            new ALU2().run();
        }
    }
}
