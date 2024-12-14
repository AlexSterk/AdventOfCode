from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution


class Day24(Day):
    @property
    def day(self):
        return 24

    @solution("498")
    def part1(self) -> object:
        grid = {(x, y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        to_visit = set(p for p, c in grid.items() if c.isdigit() and c != "0")
        start = next(p for p, c in grid.items() if c == "0")

        grid = {p: "." for p, c in grid.items() if c != "#"}

        def ns(p):
            (x, y), visited = p
            for dx, dy in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
                if grid.get((x + dx, y + dy)) == ".":
                    yield (x + dx, y + dy), frozenset.union(*[visited, [(x + dx, y + dy)]]) if (x + dx,
                                                                                                y + dy) in to_visit else visited

        s = (start, frozenset())

        def goal(s):
            p, visited = s
            return visited == to_visit

        return shortest_path(s, goal, ns)[2]

    @solution("804")
    def part2(self) -> object:
        grid = {(x, y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        to_visit = set(p for p, c in grid.items() if c.isdigit() and c != "0")
        start = next(p for p, c in grid.items() if c == "0")

        grid = {p: "." for p, c in grid.items() if c != "#"}

        def ns(p):
            (x, y), visited = p
            for dx, dy in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
                if grid.get((x + dx, y + dy)) == ".":
                    yield (x + dx, y + dy), frozenset.union(*[visited, [(x + dx, y + dy)]]) if (x + dx,
                                                                                                y + dy) in to_visit else visited

        s = (start, frozenset())

        def goal(s):
            p, visited = s
            return visited == to_visit and p == start

        return shortest_path(s, goal, ns)[2]


# Day24("test").run()
Day24().run()
