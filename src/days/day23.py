from src.setup.day import Day
from src.util.solution import solution

''' to optimize:
cpy 0 a
cpy b c
inc a
dec c
jnz c -2
dec d
jnz d -5

does the following:
a = d * c
'''

def run_cpu(instructions, registers):
    pc = 0

    def val(label):
        try:
            return int(label)
        except ValueError:
            return registers.get(label, 0)

    while pc < len(instructions):
        ins, *args = instructions[pc]
        print(pc, ins, args)
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


class Day23(Day):
    @property
    def day(self):
        return 23

    @solution("11478")
    def part1(self) -> object:
        registers = {"a": 7}
        instructions = [l.split() for l in self.input]
        return run_cpu(instructions, registers)

    @solution("")
    def part2(self) -> object:
        registers = {"a": 12}
        instructions = [l.split() for l in self.input]
        return run_cpu(instructions, registers)


# Day23("test").run()
Day23().run()
