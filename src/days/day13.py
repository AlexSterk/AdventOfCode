import re
import numpy as np

from src.setup.day import Day
from src.util.solution import solution

def is_integer(n):
    if abs(n - round(n)) < 1e-9:
        return True


class Day13(Day):
    @property
    def day(self):
        return 13

    @solution("29877")
    def part1(self) -> object:
        config = self.raw_input.split("\n\n")
        total = 0
        for c in config:
            p = r"\d+"
            matches = re.findall(p, c)
            a_dx = int(matches[0])
            a_dy = int(matches[1])
            b_dx = int(matches[2])
            b_dy = int(matches[3])
            g_x = int(matches[4])
            g_y = int(matches[5])

            # a * a_dx + b * b_dx = g_x
            # a * a_dy + b * b_dy = g_y
            # | a_dx b_dx | | a | = | g_x |
            # | a_dy b_dy | | b | = | g_y |
            # Ax = G
            # x = A^-1 * G

            A = np.array([[a_dx, b_dx], [a_dy, b_dy]])
            G = np.array([g_x, g_y])
            x = np.linalg.solve(A, G)
            a, b = x
            if a > 100 or b > 100 or not is_integer(a) or not is_integer(b):
                continue
            a = round(a)
            b = round(b)
            total += a * 3 + b
        return total

    @solution("")
    def part2(self) -> object:
        return None


# Day13("test").run()
Day13().run()
