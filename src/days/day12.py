from src.setup.day import Day
from src.util.solution import solution

class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("318077")
    def part1(self) -> object:
        registers = {}
        instructions = [l.split() for l in self.input]
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

    @solution("9227731")
    def part2(self) -> object:
        registers = {"c": 1}
        instructions = [l.split() for l in self.input]
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

# Day12("test").run()
Day12().run()
