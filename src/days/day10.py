from collections import deque

import numpy as np

from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution

ld = {".": 0, "#": 1}

def button_to_bitstring(button: str, size: int):
    button = button.strip("()").split(",")
    button = [int(x) for x in button]
    result = [False] * size
    for b in button:
        result[b] = True
    return result


class Day10(Day):
    @property
    def day(self):
        return 10

    @solution("436")
    def part1(self) -> object:
        total = 0
        for line in self.input:
            end, *buttons, _ = line.split(" ")
            end = tuple([True if c == "#" else False for c in end.strip("[]")])
            buttons = [button_to_bitstring(b, len(end)) for b in buttons]
            # print(end, buttons)

            start = [False] * len(end)
            def is_end(cur):
                return cur == end
            def neighbours(cur):
                for b in buttons:
                    yield tuple((np.array(cur) ^ np.array(b)).tolist())
            total += shortest_path(tuple(start), is_end, neighbours)[2]
        return total




    @solution("")
    def part2(self) -> object:
        return None


# Day10("test").run()
Day10().run()
