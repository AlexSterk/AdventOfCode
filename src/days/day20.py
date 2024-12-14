from src.setup.day import Day
from src.util.solution import solution

class Day20(Day):
    @property
    def day(self):
        return 20

    @solution("14975795")
    def part1(self) -> object:
        blocked = []
        for line in self.input:
            a, b = map(int, line.split("-"))
            blocked.append((a, b))

        blocked.sort()
        mn, mx = blocked[0]
        for a, b in blocked:
            if a > mx + 1:
                return mx + 1
            else:
                mx = max(mx, b)

    @solution("101")
    def part2(self) -> object:
        blocked = []
        for line in self.input:
            a, b = map(int, line.split("-"))
            blocked.append((a, b))

        blocked.sort()
        total_blocked = 0
        mn, mx = blocked[0]
        for a, b in blocked:
            if a > mx + 1:
                total_blocked += mx + 1 - mn
                mn, mx = a, b
            else:
                mx = max(mx, b)
        return 2**32 - total_blocked - (mx-mn+1)

# Day20("test").run()
Day20().run()
