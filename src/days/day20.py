from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_paths
from src.util.solution import solution

def circle(p, r):
    x, y = p
    for k in range(r):
        yield x + r - k, y + k
        yield x - k, y + r - k
        yield x + k - r, y - k
        yield x + k, y + k - r



def disk(p, radius):
    for r in range(radius + 1):
        yield from circle(p, r)

class Day20(Day):
    @property
    def day(self):
        return 20

    @solution("1518")
    def part1(self) -> object:
        return self.count_cheats(2)

    def count_cheats(self, l):
        grid = {(x, y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        start = next(k for k, v in grid.items() if v == "S")
        end = next(k for k, v in grid.items() if v == "E")

        def ns(n):
            x, y = n
            for dx, dy in directions.cardinal.values():
                if grid.get((x + dx, y + dy), '#') != "#":
                    yield x + dx, y + dy

        dist_from_start, _, _ = shortest_paths(start, ns)
        dist_from_end, _, _ = shortest_paths(end, ns)
        no_cheats = dist_from_start[end]
        total = 0

        def manhattan(a, b):
            x1, y1 = a
            x2, y2 = b
            return abs(x1 - x2) + abs(y1 - y2)

        for p, v in grid.items():
            if v == "#":
                continue
            for q in disk(p, l):
                if grid.get(q, "#") == "#":
                    continue
                cheat = dist_from_start[p] + dist_from_end[q] + manhattan(p, q)
                if no_cheats - cheat >= 100:
                    total += 1
        return total

    @solution("1032257")
    def part2(self) -> object:
        return self.count_cheats(20)


# Day20("test").run()
Day20().run()
