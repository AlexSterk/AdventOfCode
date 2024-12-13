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
        if ins == "cpy":
            registers[args[1]] = val(args[0])
        elif ins == "inc":
            registers[args[0]] += 1
        elif ins == "dec":
            registers[args[0]] -= 1
        elif ins == "jnz":
            if val(args[0]) != 0:
                pc += val(args[1])
                continue
        pc += 1
    return registers["a"]


class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("318077")
    def part1(self) -> object:
        instructions = [l.split() for l in self.input]
        registers = {}
        return run_cpu(instructions, registers)

    @solution("9227731")
    def part2(self) -> object:
        registers = {"c": 1}
        instructions = [l.split() for l in self.input]
        return run_cpu(instructions, registers)


# Day12("test").run()
Day12().run()
