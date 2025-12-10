import numpy as np
from scipy.optimize import linprog

from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution

ld = {".": 0, "#": 1}


def button_to_bitstring(button: str, size: int):
    button = button.strip("()").split(",")
    button = [int(x) for x in button]
    result = [0] * size
    for b in button:
        result[b] = 1
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
            end = tuple([1 if c == "#" else 0 for c in end.strip("[]")])
            buttons = [button_to_bitstring(b, len(end)) for b in buttons]
            # print(end, buttons)

            start = [0] * len(end)

            def is_end(cur):
                return cur == end

            def neighbours(cur):
                for b in buttons:
                    yield tuple((np.array(cur) ^ np.array(b)).tolist())

            total += shortest_path(tuple(start), is_end, neighbours)[2]
        return total

    @solution("")
    def part2(self) -> object:
        total = 0
        for line in self.input:
            _, *buttons, joltage = line.split(" ")
            joltage = [int(x) for x in joltage.strip("{}").split(",")]
            buttons = [np.array(button_to_bitstring(b, len(joltage))) for b in buttons]
            cost = np.ones(len(buttons))
            buttons = np.vstack(buttons).T

            joltage = np.array(joltage)
            # print(joltage, buttons, cost)

            result = linprog(c=cost, A_eq=buttons, b_eq=joltage, method='highs')
            result = result.x
            print(result)
            total += np.sum(result)

        return total, int(total)


Day10("test").run()
# Day10().run()
