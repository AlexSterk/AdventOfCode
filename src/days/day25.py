from distutils.command.register import register

from src.setup.day import Day
from src.util.solution import solution


def run_cpu(instructions, registers):
    pc = 0

    def val(label):
        try:
            return int(label)
        except ValueError:
            return registers.get(label, 0)

    while pc < len(instructions):
        ins, *args = instructions[pc]
        # print(pc, ins, args) # find the loop
        if ins == "cpy":
            registers[args[1]] = val(args[0])
        elif ins == "inc":
            registers[args[0]] += 1
        elif ins == "dec":
            registers[args[0]] -= 1
        elif ins == "out":
            yield val(args[0])
        elif ins == "mul":
            x, y, z = args
            registers[z] = val(x) * val(y)
        elif ins == "jnz":
            if val(args[0]) != 0:
                pc += val(args[1])
                continue
        elif ins == "tgl":
            offset = val(args[0])
            if 0 <= pc + offset < len(instructions):
                ins, *args = instructions[pc + offset]
                if len(args) == 1:
                    if ins == "inc":
                        ins = "dec"
                    else:
                        ins = "inc"
                else:
                    if ins == "jnz":
                        ins = "cpy"
                    else:
                        ins = "jnz"
                instructions[pc + offset] = [ins, *args]
        pc += 1
    return registers["a"]


class Day25(Day):
    @property
    def day(self):
        return 25

    @solution("")
    def part1(self) -> object:
        instructions = [line.split() for line in self.input]

        for i in range(10):
            registers = {"a": i}
            output = []
            states = set()
            state = (0, frozenset(registers.items()))
            while state not in states:
                states.add(state)
                output.append(next(run_cpu(instructions, registers)))
                state = (registers["a"], frozenset(registers.items()))
            print(i, output)

        return None

    @solution("")
    def part2(self) -> object:
        return None


# Day25("test").run()
Day25().run()
