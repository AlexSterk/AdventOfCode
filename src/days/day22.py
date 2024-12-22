from collections import defaultdict
from itertools import pairwise

from src.setup.day import Day
from src.util.solution import solution


def next_number(n):
    r = n
    a = r * 64
    r = (r ^ a) % 16777216
    b = r // 32
    r = (r ^ b) % 16777216
    c = r * 2048
    r = (r ^ c) % 16777216

    return r


def last_digit(n):
    return n % 10


def next_number_repeat(n, i):
    for i in range(i):
        n = next_number(n)
        yield n


class Day22(Day):
    @property
    def day(self):
        return 22

    @solution("16999668565")
    def part1(self) -> object:
        return sum(last for *_, last in map(lambda n: next_number_repeat(n, 2000), map(int, self.input)))

    @solution("1898")
    def part2(self) -> object:
        ans = defaultdict(int)

        for n in map(int, self.input):
            nums = list(next_number_repeat(n, 2000))
            diffs = [b % 10 - a % 10 for a, b in pairwise(nums)]
            seen = set()
            for i in range(len(nums) - 4):
                window = tuple(diffs[i:i + 4])
                if window not in seen:
                    seen.add(window)
                    ans[window] += nums[i + 4] % 10

        return max(ans.values())


if __name__ == '__main__':
    # Day22("test").run()
    Day22().run()
