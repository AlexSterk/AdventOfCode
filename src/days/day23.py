from src.setup.day import Day
from src.util.solution import solution

''' to optimize:
3: cpy 0 a             a = 0                  a = 0             a = 0            a = d * b
4: cpy b c             c = b                  while d != 0:     while d != 0:    d = 0
5: inc a               a += 1                     c = b             a += b       c = 0
6: dec c               c -= 1                     a += c            d -= 1    
7: jnz c -2            if c != 0: goto 5          d -= 1        c = 0        
8: dec d               d -= 1                     c = 0
9: jnz d -5            if d != 0: goto 4

does the following:
a = d * c

cpy b c
inc a
dec c
jnz c -2
dec d
jnz d -5

translates to:
mul b d a
cpy 0 c
cpy 0 c
cpy 0 c
cpy 0 c
cpy 0 d
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
        # print(pc, ins, args) # find the loop
        if ins == "cpy":
            registers[args[1]] = val(args[0])
        elif ins == "inc":
            registers[args[0]] += 1
        elif ins == "dec":
            registers[args[0]] -= 1
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
