package days;

import setup.Day;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 extends Day {

    private static final Pattern BEFORE_AFTER_LIST = Pattern.compile("\\[(.+)]");
    private List<Sample> samples;
    private List<List<Integer>> program;

    @Override
    public void processInput() {
        String[] parts = input.split("\n\n\n\n");

        String[] samples = parts[0].split("\n\n");
        this.samples = new ArrayList<>();
        for (String sample : samples) {
            String[] lines = sample.split("\n");
            Matcher m1 = BEFORE_AFTER_LIST.matcher(lines[0]);
            Matcher m2 = BEFORE_AFTER_LIST.matcher(lines[2]);
            m1.find();
            m2.find();
            this.samples.add(new Sample(
                    Arrays.stream(m1.group(1).split(", ")).map(Integer::parseInt).toList(),
                    Arrays.stream(m2.group(1).split(", ")).map(Integer::parseInt).toList(),
                    Arrays.stream(lines[1].split(" ")).map(Integer::parseInt).toList()
            ));
        }
        program = Arrays.stream(parts[1].split("\n"))
                .map(s ->
                        Arrays.stream(s.split(" ")).map(Integer::parseInt).toList()
                ).toList();
    }

    @Override
    public void part1() {
        System.out.println(samples.stream()
                .map(Sample::tryAllInstructions)
                .mapToInt(List::size)
                .filter(i -> i >= 3)
                .count()
        );
    }

    @Override
    public void part2() {
        Map<Integer, Set<Class<? extends Instruction>>> potentialOpcodes = samples.stream()
                .collect(Collectors.toMap(
                                s -> s.op.get(0),
                                s -> s.tryAllInstructions().stream().map(Instruction::getClass).collect(Collectors.toSet()),
                                (l1, l2) -> {
                                    l1.retainAll(l2);
                                    return l1;
                                }
                        )
                );

        Map<Integer, Class<? extends Instruction>> opcodes = new HashMap<>();
        while (!potentialOpcodes.isEmpty()) {
            var sureOpcode = potentialOpcodes.entrySet().stream().filter(e -> e.getValue().size() == 1).findFirst().get();
            Class<? extends Instruction> ins = sureOpcode.getValue().iterator().next();
            Integer opcode = sureOpcode.getKey();
            opcodes.put(opcode, ins);
            potentialOpcodes.forEach((o, s) -> s.remove(ins));
            potentialOpcodes.remove(opcode);
        }

        List<Instruction> program = new ArrayList<>();
        for (List<Integer> line : this.program) {
            Instruction newInstance = null;
            try {
                newInstance = opcodes.get(line.get(0))
                        .getDeclaredConstructor(int.class, int.class, int.class)
                        .newInstance(line.get(1), line.get(2), line.get(3));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            program.add(newInstance);
        }
        final int[] registers = new int[4];
        program.forEach(i -> Instruction.execute(i, registers));
        System.out.println(registers[0]);
    }

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private sealed interface Instruction {
        static List<Instruction> getAllInstructions(int a, int b, int c) {
            List<Instruction> res = new ArrayList<>();

            res.add(new Addr(a, b, c));
            res.add(new Addi(a, b, c));
            res.add(new Mulr(a, b, c));
            res.add(new Muli(a, b, c));
            res.add(new Banr(a, b, c));
            res.add(new Bani(a, b, c));
            res.add(new Borr(a, b, c));
            res.add(new Bori(a, b, c));
            res.add(new Setr(a, b, c));
            res.add(new Seti(a, b, c));
            res.add(new Gtir(a, b, c));
            res.add(new Gtri(a, b, c));
            res.add(new Gtrr(a, b, c));
            res.add(new Eqir(a, b, c));
            res.add(new Eqri(a, b, c));
            res.add(new Eqrr(a, b, c));

            return res;
        }

        static void execute(Instruction instruction, int[] registers) {
            switch (instruction) {
                case Addr o -> registers[o.C] = registers[o.A] + registers[o.B];
                case Addi o -> registers[o.C] = registers[o.A] + o.B;
                case Mulr o -> registers[o.C] = registers[o.A] * registers[o.B];
                case Muli o -> registers[o.C] = registers[o.A] * o.B;
                case Banr o -> registers[o.C] = registers[o.A] & registers[o.B];
                case Bani o -> registers[o.C] = registers[o.A] & o.B;
                case Borr o -> registers[o.C] = registers[o.A] | registers[o.B];
                case Bori o -> registers[o.C] = registers[o.A] | o.B;
                case Setr o -> registers[o.C] = registers[o.A];
                case Seti o -> registers[o.C] = o.A;
                case Gtir o -> registers[o.C] = o.A > registers[o.B] ? 1 : 0;
                case Gtri o -> registers[o.C] = registers[o.A] > o.B ? 1 : 0;
                case Gtrr o -> registers[o.C] = registers[o.A] > registers[o.B] ? 1 : 0;
                case Eqir o -> registers[o.C] = o.A == registers[o.B] ? 1 : 0;
                case Eqri o -> registers[o.C] = registers[o.A] == o.B ? 1 : 0;
                case Eqrr o -> registers[o.C] = registers[o.A] == registers[o.B] ? 1 : 0;
            }
        }

        int A();

        int B();

        int C();
    }

    private record Sample(List<Integer> before, List<Integer> after, List<Integer> op) {
        public List<Instruction> tryAllInstructions() {
            List<Instruction> valid = new ArrayList<>();
            List<Instruction> allInstructions = Instruction.getAllInstructions(op.get(1), op.get(2), op.get(3));
            for (Instruction instruction : allInstructions) {
                int[] registers = before.stream().mapToInt(x -> x).toArray();
                Instruction.execute(instruction, registers);
                List<Integer> output = Arrays.stream(registers).boxed().toList();
                if (output.equals(after)) valid.add(instruction);
            }
            return valid;
        }
    }

    private record Addr(int A, int B, int C) implements Instruction {
    }

    private record Addi(int A, int B, int C) implements Instruction {
    }

    private record Mulr(int A, int B, int C) implements Instruction {
    }

    private record Muli(int A, int B, int C) implements Instruction {
    }

    private record Banr(int A, int B, int C) implements Instruction {
    }

    private record Bani(int A, int B, int C) implements Instruction {
    }

    private record Borr(int A, int B, int C) implements Instruction {
    }

    private record Bori(int A, int B, int C) implements Instruction {
    }

    private record Setr(int A, int B, int C) implements Instruction {
    }

    private record Seti(int A, int B, int C) implements Instruction {
    }

    private record Gtir(int A, int B, int C) implements Instruction {
    }

    private record Gtri(int A, int B, int C) implements Instruction {
    }

    private record Gtrr(int A, int B, int C) implements Instruction {
    }

    private record Eqir(int A, int B, int C) implements Instruction {
    }

    private record Eqri(int A, int B, int C) implements Instruction {
    }

    private record Eqrr(int A, int B, int C) implements Instruction {
    }
}
