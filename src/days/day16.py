from turtledemo.penrose import start

from src.setup.day import Day
from src.util.dijkstra import shortest_path, shortest_paths, all_paths
from src.util.directions import cardinal
from src.util.solution import solution


class Day16(Day):
    @property
    def day(self):
        return 16

    @solution("105508")
    def part1(self) -> object:
        self.grid = grid = {(x, y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
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
                yield p, d

        def cost(state, n_state):
            return 1 if state[1] == n_state[1] else 1000

        _, e, d = self.shortest_path = shortest_path(start_state, is_end, ns, cost)
        self.start = start_state
        self.end = e

        return d

    @solution("548")
    def part2(self) -> object:
        grid = self.grid
        dist, _, _ = self.shortest_path
        start = self.start
        end = self.end

        def cost(state, n_state):
            return 1 if state[1] == n_state[1] else 1000

        def backwards_ns(state):
            (x, y), (dx, dy) = p, d = state
            if (n := (x - dx, y - dy)) in grid and grid[n] != '#':
                yield n, d
            for d in cardinal.values():
                if d == (dx, dy) or d == (-dx, -dy):
                    continue
                yield p, d

        q = [end]
        visited = {end}

        while q:
            c = q.pop()
            if c == start:
                continue
            for n in backwards_ns(c):
                # Is this neighbor accessible from start,
                # with a cost equal to the cost to current MINUS the cost between current and neighbour?
                co = cost(c, n)
                if n in dist and dist[n] == dist[c] - co:
                    if n not in visited:
                        q.append(n)
                        visited.add(n)

        return len(set(s[0] for s in visited))


# Day16("test").run()
Day16().run()
