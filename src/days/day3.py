from src.setup.day import Day
from src.util.solution import solution

class Day3(Day):
    @property
    def day(self):
        return 3

    @solution("17346")
    def part1(self) -> object:
        res = 0
        for line in self.read_input():
            m = 0
            for i, x in enumerate(line[:-1]):
                for y in line[i + 1:]:
                    m = max(m, int(x + y))
            # print(m)
            res += m
        return res

    @solution("")
    def part2(self) -> object:
        return None

# Day3("test").run()
Day3().run()