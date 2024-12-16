from src.setup.day import Day
from src.util.dijkstra import shortest_path, shortest_paths
from src.util.directions import cardinal
from src.util.solution import solution


class Day16(Day):
    @property
    def day(self):
        return 16

    @solution("105508")
    def part1(self) -> object:
        grid = {(x, y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        start = next(k for k, v in grid.items() if v == 'S')
        end = next(k for k, v in grid.items() if v == 'E')

        start_state = (start, (cardinal['E']))

        def is_end(state):
            return state[0] == end

        def ns(state):
            (x, y), (dx, dy) = p, d = state
            if (n := (x + dx, y + dy)) in grid and grid[n] != '#':
                yield n, d
            for d in cardinal.values():
                if d == (dx, dy) or d == (-dx, -dy):
                    continue
                n = (x + d[0], y + d[1])
                if n in grid and grid[n] != '#':
                    yield n, d

        def cost(state, n_state):
            return 1 if state[1] == n_state[1] else 1001

        dist, _, _, prev = shortest_paths(start_state, ns, cost)
        self.prev = prev
        self.start = start_state
        self.end = end
        return min(v for k, v in dist.items() if is_end(k))

    @solution("")
    def part2(self) -> object:
        start = self.start
        end = self.end
        prev = self.prev

        spots = set()

        spots.add(end)
        spots.add(start[0])

        for k in prev:
            if k[0] == end:
                c = k
                while c != start:
                    spots.add(c[0])
                    c = prev[c]

        print(spots)
        return len(spots)


Day16("test").run()
# Day16().run()
