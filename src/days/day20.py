from functools import cache

from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_path, reconstruct_path, Dijkstra
from src.util.solution import solution

class Day20(Day):
    @property
    def day(self):
        return 20

    @solution("")
    def part1(self) -> object:
        grid = {(x,y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        start = next(k for k, v in grid.items() if v == "S")
        end = next(k for k, v in grid.items() if v == "E")

        def ns(n):
            x,y = n
            for dx, dy in directions.cardinal.values():
                if grid.get((x+dx, y+dy), '#') != "#":
                    yield x+dx, y+dy

        D = Dijkstra()

        D.shortest_path(start, lambda n: n == end, ns)
        no_cheats = D.d
        path = D.get_shortest_path()

        @cache
        def cached_shortest_path(n):
            return shortest_path(n, lambda n: n == end, ns)

        cheats = {}
        for i, (x,y) in enumerate(path):
            for (dx,dy) in directions.cardinal.values():
                ch_start = (x + dx, y + dy)
                ch_end = (x + dx * 2, y + dy * 2)
                if grid.get(ch_end, "#") != "#":
                    _,_,d = cached_shortest_path(ch_end)
                    if d:
                        # print(f"When cheating from path position {i} ({x},{y}), we cheat from {ch_start} to {ch_end} and from there it takes {d} steps to reach the end")
                        to_i = i
                        from_i_to_ch = 2
                        from_ch_to_end = d
                        length = to_i + from_i_to_ch + from_ch_to_end
                        saved = no_cheats - length
                        # print(f"We save {saved} steps")
                        assert (ch_start, ch_end) not in cheats
                        cheats[(ch_start, ch_end)] = saved
        # count all cheats that save more than 100 steps
        return sum(1 for v in cheats.values() if v > 100)

        # count values of saved_list
        # saved_list = [v for v in cheats.values()]
        # saved_list.sort()
        # for v in set(saved_list):
        #     print(f"Value {v} occurs {saved_list.count(v)} times")



    @solution("")
    def part2(self) -> object:
        return None

# Day20("test").run()
Day20().run()
