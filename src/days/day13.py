import re
import numpy as np

from src.setup.day import Day
from src.util.solution import solution


class Day13(Day):
    @property
    def day(self):
        return 13

    @solution("29877")
    def part1(self) -> object:
        config = self.raw_input.split("\n\n")
        total = 0
        for c in config:
            ax, ay, bx, by, gx, gy = map(int, re.findall(r"\d+", c))

            A = np.array([[ax, bx], [ay, by]])
            G = np.array([gx, gy])
            X = np.round(np.linalg.solve(A, G))
            if (G == X @ A.T).all():
                total += X @ (3, 1)

        return round(total)

    @solution("99423413811305")
    def part2(self) -> object:
        config = self.raw_input.split("\n\n")
        total = 0
        for c in config:
            ax, ay, bx, by, gx, gy = map(int, re.findall(r"\d+", c))

            A = np.array([[ax, bx], [ay, by]])
            G = np.array([gx, gy]) + 10000000000000
            X = np.round(np.linalg.solve(A, G))
            if (G == X @ A.T).all():
                total += X @ (3, 1)

        return round(total)


# Day13("test").run()
Day13().run()
