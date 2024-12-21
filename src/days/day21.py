import re
from collections import deque

from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_path
from src.util.solution import solution

KEYPAD = {(x, y): c for y, row in enumerate("789\n546\n123\n 0A".splitlines()) for x, c in enumerate(row)}
DIRPAD = {(x, y): c for y, row in enumerate(" ^A\n<v>".splitlines()) for x, c in enumerate(row)}

# remove " "
KEYPAD = {k: v for k, v in KEYPAD.items() if v != " "}
DIRPAD = {k: v for k, v in DIRPAD.items() if v != " "}

actions = {
    "<": (-1, 0),
    ">": (1, 0),
    "^": (0, -1),
    "v": (0, 1),
    "A": (0, 0)
}

opposite = {
    "<": ">",
    ">": "<",
    "^": "v",
    "v": "^",
}

def get_complexity(code: str, depth: int = 2) -> int:
    num = int(code.replace('A', ''))
    return num * find_best_length(code, depth)

def find_paths(start: str, end: str):
    if start == end:
        return []

    result = []
    keypad = (start.isdigit() or end.isdigit())
    pad = KEYPAD if keypad else DIRPAD

    position = next(k for k, v in pad.items() if v == start)
    target = (next(k for k, v in pad.items() if v == end))
    q = deque()
    q.append((position,))
    distance = abs(position[0] - target[0]) + abs(position[1] - target[1])

    def adjacent(p, path):
        x, y = p
        for a, (dx, dy) in actions.items():
            n = (x + dx, y + dy)
            if n in pad and n not in path:
                yield n

    while q:
        path = q.popleft()
        p = path[-1]
        if p == target:
            result.append(path)
        if len(path) > distance:
            continue
        for n in adjacent(p, path):
            q.append(path + (n,))
    return result


def path_to_dpad(path):
    result = []
    for i in range(1, len(path)):
        ax, ay = path[i - 1]
        bx, by = path[i]
        d = (
            '>' if bx > ax else
            '<' if bx < ax else
            '^' if by < ay else
            'v')
        result.append(d)
    result.append('A')
    return ''.join(result)


def find_best_length(sequence, level):
    result = 0
    for i in range(len(sequence)):
        start = 'A' if i == 0 else sequence[i - 1]
        end = sequence[i]

        paths = find_paths(start, end)

        if level == 0:
            length = min(len(x) for x in paths) if paths else 1
            result += length
            continue

        if not paths:
            result += 1
            continue

        lengths = set()
        for path in paths:
            dpad = path_to_dpad(path)
            lengths.add(find_best_length(dpad, level - 1))
        result += min(lengths)
    return result


class Day21(Day):
    @property
    def day(self):
        return 21

    @solution("")
    def part1(self) -> object:
        return sum(get_complexity(line) for line in self.input)

    @solution("")
    def part2(self) -> object:
        return None

# Day21("test").run()
Day21().run()

from functools import lru_cache
from itertools import permutations

pad = ["789", "456", "123", " 0A"]
pad2 = [" ^A", "<v>"]
drdc = [(-1, 0), (1, 0), (0, -1), (0, 1)]
drDcToDir = dict(zip(drdc, "^v<>"))


def getAllEncodings(code, pad, sr, sc):
    R = len(pad)
    C = len(pad[0])
    for perm in permutations(drdc):
        ret = []
        r, c = sr, sc
        for x in code:
            for rr in range(R):
                for cc in range(C):
                    if pad[rr][cc] == x:
                        while True:
                            for dr, dc in perm:
                                if (
                                        0 <= r + dr < R
                                        and 0 <= c + dc < C
                                        and pad[r + dr][c + dc] != " "
                                        and abs(rr - r) + abs(cc - c)
                                        > abs(rr - (r + dr)) + abs(cc - (c + dc))
                                ):
                                    ret.append(drDcToDir[dr, dc])
                                    r += dr
                                    c += dc
                                    break
                            else:
                                ret.append("A")
                                break
        yield "".join(ret)


lines = open("data/day21/input.txt").read().strip().split("\n")
for part, DEPTH in enumerate([2, 25], start=1):

    @lru_cache(maxsize=None)
    def dp(code, depth=0):
        if depth == DEPTH:
            return len(code)
        return sum(
            min(
                dp(code2, depth + 1)
                for code2 in getAllEncodings(chunk + "A", pad2, 0, 2)
            )
            for chunk in code[:-1].split("A")
        )

    print(
        f"Part{part}",
        sum(
            min(dp(code) for code in getAllEncodings(line, pad, 3, 2))
            * int("".join(x for x in line if x.isdigit()))
            for line in lines
        ),
    )


