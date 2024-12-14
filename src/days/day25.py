from src.setup.day import Day
from src.util.solution import solution

import multiprocessing

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


def loops_forever(instructions, i):
    i.value = 0
    while True:
        registers = {"a": i.value}
        cpu = run_cpu(instructions, registers)
        c = next(cpu)
        if c == 1:
            i.value += 1
            continue
        while True:
            n = next(cpu)
            if c == n:
                break
            c = n
        i.value += 1


class Day25(Day):
    @property
    def day(self):
        return 25

    @solution("")
    def part1(self) -> object:
        instructions = [line.split() for line in self.input]

        i = multiprocessing.Value('i', 0)
        p = multiprocessing.Process(target=loops_forever, args=(instructions,i))
        p.start()
        p.join(10)
        p.terminate()

        return i.value

    @solution("")
    def part2(self) -> object:
        return None

if __name__ == '__main__':
    Day25().run()
