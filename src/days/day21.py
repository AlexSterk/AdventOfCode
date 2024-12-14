import re

from src.setup.day import Day
from src.util.solution import solution

class Day21(Day):
    @property
    def day(self):
        return 21

    @solution("gcedfahb")
    def part1(self) -> object:
        to_scramble = list("abcdefgh") if not self.test else list("abcde")

        for instruction in self.input:
            if instruction.startswith("swap position"):
                x, y = map(int, re.findall(r"\d+", instruction))
                to_scramble[x], to_scramble[y] = to_scramble[y], to_scramble[x]
            elif instruction.startswith("swap letter"):
                p = r"swap letter (\w) with letter (\w)"
                x, y = re.match(p, instruction).groups()
                x, y = to_scramble.index(x), to_scramble.index(y)
                to_scramble[x], to_scramble[y] = to_scramble[y], to_scramble[x]
            elif instruction.startswith("rotate left"):
                n = int(re.search(r"\d+", instruction).group())
                to_scramble = to_scramble[n:] + to_scramble[:n]
            elif instruction.startswith("rotate right"):
                n = int(re.search(r"\d+", instruction).group())
                to_scramble = to_scramble[-n:] + to_scramble[:-n]
            elif instruction.startswith("rotate based"):
                p = r"rotate based on position of letter (\w)"
                x = re.search(p, instruction).group(1)
                x = to_scramble.index(x)
                to_scramble = to_scramble[-1:] + to_scramble[:-1]
                to_scramble = to_scramble[-x:] + to_scramble[:-x]
                if x >= 4:
                    to_scramble = to_scramble[-1:] + to_scramble[:-1]
                # n = 1 + x + (1 if x >= 4 else 0)
            elif instruction.startswith("reverse positions"):
                x, y = map(int, re.findall(r"\d+", instruction))
                to_scramble[x:y+1] = to_scramble[x:y+1][::-1]
            elif instruction.startswith("move position"):
                x, y = map(int, re.findall(r"\d+", instruction))
                to_scramble.insert(y, to_scramble.pop(x))
            print(instruction, "".join(to_scramble))

        return "".join(to_scramble)

    @solution("")
    def part2(self) -> object:
        return None

# Day21("test").run()
Day21().run()
