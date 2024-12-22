from math import floor

from src.setup.day import Day
from src.util.solution import solution

import multiprocessing

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

def sublist_index(arr, sub):
    for i in range(len(arr) - len(sub)):
        if arr[i:i + len(sub)] == sub:
            return i
    return -1

def next_number_repeat(n, i):
    for i in range(i):
        n = next_number(n)
    return n

class Day22(Day):
    @property
    def day(self):
        return 22

    @solution("16999668565")
    def part1(self) -> object:
        return sum(next_number_repeat(i, 2000) for i in map(int, self.input))

    @solution("")
    def part2(self) -> object:
        cache = {}
        for i, n in enumerate(map(int, self.input)):
            seq = [last_digit(n)]
            for _ in range(2000):
                n = next_number(n)
                seq.append(last_digit(n))
            deltas = [seq[0]]
            for j in range(1, len(seq)):
                deltas.append(seq[j] - seq[j - 1])
            cache[i] = seq, deltas

        window_size = 4
        max_window = 0, []


        # try different windows using multiprocessing
        windows = set()
        def worker(i):
            window = cache[0][1][i:i + window_size]
            s = 0
            for j in range(0, len(cache)):
                index = sublist_index(cache[j][1], window)
                if index == -1:
                    continue
                n = cache[j][0][index + window_size - 1]
                s += n
            return s

        for i in range(0,len(cache[0][1]) - window_size + 1):
            window = cache[0][1][i:i + window_size]
            s = 0
            for j in range(0, len(cache)):
                index = sublist_index(cache[j][1], window)
                if index == -1:
                    continue
                n = cache[j][0][index + window_size - 1]
                s += n

            # if s > max_window[0]:
            #     max_window = s, window

        return max_window[0]

# Day22("test").run()
Day22().run()