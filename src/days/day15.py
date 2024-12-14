import re
import z3

from src.setup.day import Day
from src.util.solution import solution


def solve_discs(discs):
    # find t such that (p + t + i) % m == 0 for all discs
    t = z3.Int("t")
    s = z3.Solver()
    s.add(t >= 0)
    for i, disc in enumerate(discs):
        s.add((disc["p"] + t + i + 1) % disc["m"] == 0)
    s.check()
    return s, t


class Day15(Day):
    @property
    def day(self):
        return 15

    @solution("121834")
    def part1(self) -> object:
        discs = []
        for line in self.input:
            m = re.match(r"Disc #.+ has (\d+) positions; at time=0, it is at position (\d+)\.", line)
            discs.append({"m": int(m.group(1)), "p": int(m.group(2))})

        s, t = solve_discs(discs)
        self.discs = discs
        return s.model()[t].as_long()

    @solution("3208099")
    def part2(self) -> object:
        discs = self.discs
        discs.append({"m": 11, "p": 0})

        s, t = solve_discs(discs)
        return s.model()[t].as_long()

# Day15("test").run()
Day15().run()
