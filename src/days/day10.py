from src.setup.day import Day
from src.util.dijkstra import shortest_paths
from src.util.solution import solution

DIRS = [(0, 1), (0, -1), (1, 0), (-1, 0)]


class Day10(Day):
    @property
    def day(self):
        return 10

    @solution("430")
    def part1(self) -> object:
        grid = {(x, y): int(c) for y, row in enumerate(self.input) for x, c in enumerate(row)}
        def neighbours(node):
            X, Y, C, _ = node
            ns = [(X + dx, Y + dy) for dx, dy in DIRS]
            ns = [(x, y, grid.get((x, y))) for x, y in ns if (x, y) in grid]
            ns = [(x, y, c, node) for x, y, c in ns if c == C + 1]
            return ns

        starts = [(k[0], k[1], v, ()) for k, v in grid.items() if v == 0]
        ends = []
        for start in starts:
            dist, _, _ = shortest_paths(start, neighbours, lambda a, b: 1)
            ends += [(start, x,y,c) for x, y, c, end in dist if c == 9]
        self.ends = ends

        return len(set(ends))

    @solution("928")
    def part2(self) -> object:
        return len(self.ends)


# Day10("test").run()
Day10().run()
