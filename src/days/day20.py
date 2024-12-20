from functools import cache

from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_path, reconstruct_path, Dijkstra, shortest_paths
from src.util.solution import solution


class Day20(Day):
    @property
    def day(self):
        return 20

    @solution("1518")
    def part1(self) -> object:
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
        print(no_cheats)

        total = 0

        def manhattan(a, b):
            x1, y1 = a
            x2, y2 = b
            return abs(x1 - x2) + abs(y1 - y2)

        for p, v in grid.items():
            x, y = p
            if v == "#":
                continue
            for (dx, dy) in directions.cardinal.values():
                for r in range(1,3):
                    q = (x + dx * r, y + dy * r)
                    if grid.get(q, "#") == "#":
                        continue
                    cheat = dist_from_start[p] + dist_from_end[q] + manhattan(p, q)
                    if no_cheats - cheat >= 100:
                        total += 1
        return total

    @solution("")
    def part2(self) -> object:
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
            x, y = p
            if v == "#":
                continue
            for (dx, dy) in directions.cardinal.values():
                for r in range(1,21):
                    q = (x + dx * r, y + dy * r)
                    if grid.get(q, "#") == "#":
                        continue
                    cheat = dist_from_start[p] + dist_from_end[q] + manhattan(p, q)
                    if no_cheats - cheat >= 100:
                        total += 1
        return total


# Day20("test").run()
Day20().run()
