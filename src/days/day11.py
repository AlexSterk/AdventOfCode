import functools

from src.setup.day import Day
from src.util.solution import solution

N = 7

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

def blink(stones):
    cont_next = False
    for i, stone in enumerate(stones):
        if cont_next:
            cont_next = False
            continue
        output = process_stone(stone)
        if isinstance(output, tuple):
            stones[i] = output[0]
            stones.insert(i+1, output[1])
            cont_next = True
        else:
            stones[i] = output
    return stones


class Day11(Day):
    @property
    def day(self):
        return 11

    @solution("182081")
    def part1(self, n=25) -> object:
        stones = [int(x) for x in self.raw_input.split()]

        for i in range(n):
            stones = blink(stones)

        return len(stones)

    @solution("216318908621637")
    def part2(self, n=75) -> object:
        _stones = [int(x) for x in self.raw_input.split()]
        stones = {stone: 0 for stone in _stones}
        for stone in _stones:
            stones[stone] += 1

        for i in range(n):
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

        return sum(stones.values())


# Day11("test2").run()
Day11().run()