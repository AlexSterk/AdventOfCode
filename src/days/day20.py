from functools import cache

from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_path
from src.util.solution import solution

class Day20(Day):
    @property
    def day(self):
        return 20

    @solution("")
    def part1(self) -> object:
        grid = {(x,y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        start = next(k for k, v in grid.items() if v == "S")
        ee = end = next(k for k, v in grid.items() if v == "E")

        def ns(n):
            x,y = n
            for dx, dy in directions.cardinal.values():
                if grid.get((x+dx, y+dy), '#') != "#":
                    yield x+dx, y+dy

        _,_,without_cheats = shortest_path(start, lambda n: n == end, ns)
        print(without_cheats)

        # start = start,2
        #
        # def ns(n):
        #     (x,y),c = n
        #     for dx, dy in directions.cardinal.values():
        #         v = grid.get((x+dx, y+dy), '#')
        #         if v == '#' and c > 0:
        #             yield (x+dx, y+dy), c-1
        #         elif v != "#":
        #             yield (x+dx, y+dy), c-1 if c == 1 else c
        #
        # _,_,with_cheats = shortest_path(start, lambda n: n[0] == end, ns)
        # print(with_cheats)
        # print(without_cheats - with_cheats)

        start = start,None,None

        @cache
        def ns(n):
            (x,y),c1,c2 = n
            for dx, dy in directions.cardinal.values():
                N = (x+dx, y+dy)
                v = grid.get(N, '#')
                if v == "#":
                    if c1 is None:
                        yield N, N, None
                    elif c2 is None:
                        yield N, c1, N
                else:
                    if c1 is not None and c2 is None:
                        yield N, c1, N
                    else:
                        yield N, c1, c2

        # dfs to find all paths
        cheats = {}

        q = [(start, [start[0]])]

        # def dfs(n, path):
        #     if n[0] == end:
        #         return path
        #     for nn in ns(n):
        #         if nn[0] not in path:
        #             p = dfs(nn, path + [nn[0]])
        #             if p:
        #                 ch = nn[1], nn[2]
        #                 d = len(p) - 1
        #                 cheats[d] = cheats.get(d, []) + [ch]

        while q:
            n, path = q.pop()
            if n[0] == end:
                d = len(path) - 1
                ch = n[1], n[2]
                cheats[d] = cheats.get(d, []) + [ch]
                continue
            for nn in ns(n):
                if nn[0] not in path:
                    q.append((nn, path + [nn[0]]))

        # dfs(start, [start[0]])
        cheats = {k: set(v) for k,v in cheats.items()}

        return sum(len(v) for k, v in cheats.items() if without_cheats - k >= 100)

    @solution("")
    def part2(self) -> object:
        return None

# Day20("test").run()
Day20().run()
