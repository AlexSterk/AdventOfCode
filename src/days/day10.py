from typing import Iterable

import numpy as np
from scipy.optimize import milp, LinearConstraint, Bounds

from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution

def button_to_bitstring(button: str, size: int):
    button = button.strip("()").split(",")
    button = [int(x) for x in button]
    result = [0] * size
    for b in button:
        result[b] = 1
    return result


def bitstring_to_int(bitstring: Iterable[int]) -> int:
    return int("".join(map(str, bitstring)), 2)


class Day10(Day):
    @property
    def day(self):
        return 10

    @solution("436")
    def part1(self) -> object:
        total = 0
        for line in self.input:
            end, *buttons, _ = line.split(" ")
            end = tuple([1 if c == "#" else 0 for c in end.strip("[]")])
            buttons = [button_to_bitstring(b, len(end)) for b in buttons]

            end = bitstring_to_int(end)
            buttons = [bitstring_to_int(b) for b in buttons]
            start = 0

            def is_end(cur):
                return cur == end

            def neighbours(cur):
                for b in buttons:
                    yield cur ^ b

            total += shortest_path(start, is_end, neighbours)[2]
        return total

    @solution("14999")
    def part2(self) -> object:
        total = 0
        for line in self.input:
            _, *buttons, joltage = line.split(" ")
            joltage = [int(x) for x in joltage.strip("{}").split(",")]
            buttons = [np.array(button_to_bitstring(b, len(joltage))) for b in buttons]
            cost = np.ones(len(buttons))
            buttons = np.vstack(buttons).T

            joltage = np.array(joltage)

            result = milp(c=cost,
                 constraints=[LinearConstraint(A=buttons, lb=joltage, ub=joltage)],
                 bounds=Bounds(lb=0, ub=np.inf),
                 integrality=1
                )
            total += np.sum(result.x)

        return int(total)


# Day10("test").run()
Day10().run()
