import functools

from src.setup.day import Day
from src.util.solution import solution

@functools.cache
def process_stone(stone):
    if stone == 0:
        return 1
    if len(str(stone)) % 2 == 0:
        s = str(stone)
        left = int(s[:len(s)//2])
        right = int(s[len(s)//2:])
        return left, right
    return stone * 2024


class Day11(Day):
    @property
    def day(self):
        return 11

    @solution("182081")
    def part1(self, n=25) -> object:
        _stones = [int(x) for x in self.raw_input.split()]
        stones = {stone: 0 for stone in _stones}
        for stone in _stones:
            stones[stone] += 1

        for i in range(n):
            stones = self.blink(stones)

        return sum(stones.values())

    def blink(self, stones):
        temp = {}
        for stone, count in list(stones.items()):
            p = process_stone(stone)
            if isinstance(p, tuple):
                l, r = p
                c_l = temp.get(l) or 0
                temp[l] = c_l + count
                c_r = temp.get(r) or 0
                temp[r] = c_r + count
            else:
                temp[p] = (temp.get(p) or 0) + count
        stones = temp
        return stones

    @solution("216318908621637")
    def part2(self, n=75) -> object:
        return self.part1(n, skip_test=True)


# Day11("test2").run()
Day11().run()