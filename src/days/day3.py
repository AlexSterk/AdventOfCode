import re

from src.setup.day import Day
from src.util.solution import solution

class Day3(Day):
    @property
    def day(self):
        return 3

    @solution("182780583")
    def part1(self) -> object:
        pattern = r"mul\((\d+),(\d+)\)"
        memory = "".join(self.input)

        # find all matches
        matches = re.findall(pattern, memory)

        self.memory = memory
        return sum([int(x) * int(y) for x, y in matches])

    @solution("90772405")
    def part2(self) -> object:
        memory = self.memory
        pattern = f"(mul\((\d+),(\d+)\))|(do\(\))|(don't\(\))"

        total = 0
        on = True
        for e in re.finditer(pattern, memory):
            if e.group(5):
                on = False
                continue
            if e.group(4):
                on = True
                continue
            if on and e.group(1):
                total += int(e.group(2)) * int(e.group(3))

        return total

# Day3("test2").run()
Day3().run()

