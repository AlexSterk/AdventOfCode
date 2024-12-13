from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution


def is_open_space(x, y, n):
    return bin(x * x
               + 3 * x
               + 2 * x * y
               + y
               + y * y + n).count("1") % 2 == 0


class Day13(Day):
    @property
    def day(self):
        return 13

    @solution("92")
    def part1(self) -> object:
        n = int(self.raw_input.strip())
        goal = (31, 39) if not self.test else (7, 4)
        start = (1, 1)

        def ns(p):
            x, y = p
            _ns = [(x + dx, y + dy) for dx, dy in [(0, 1), (0, -1), (1, 0), (-1, 0)]]
            _ns = [(x,y) for (x,y) in _ns if x >= 0 and y >= 0 and is_open_space(x, y, n)]
            return _ns

        ds, _, c = shortest_path(start, lambda p: p == goal, ns, lambda _1, _2: 1)
        self.ds = ds

        return c

    @solution("124")
    def part2(self) -> object:
        return sum(1 for v in self.ds.values() if v <= 50)


# Day13("test").run()
Day13().run()
