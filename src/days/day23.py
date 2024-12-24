from itertools import combinations

from src.setup.day import Day
from src.util.solution import solution


class Day23(Day):
    @property
    def day(self):
        return 23

    @solution("1269")
    def part1(self) -> object:
        computers = set()
        connected = set()
        for a, b in map(lambda x: x.split("-"), self.input):
            connected.add((a, b))
            connected.add((b, a))
            computers.add(a)
            computers.add(b)

        def are_connected(a, b):
            return (a, b) in connected or (b, a) in connected

        total = 0
        for a, b, c in combinations(computers, 3):
            if are_connected(a, b) and are_connected(b, c) and are_connected(c, a):
                if any(x.startswith("t") for x in [a, b, c]):
                    total += 1
        self.computers = computers
        self.connected = connected
        return total

    @solution("ad,jw,kt,kz,mt,nc,nr,sb,so,tg,vs,wh,yh")
    def part2(self) -> object:
        computers, connected = self.computers, self.connected
        networks = [{c} for c in computers]

        def are_connected(a, b):
            return (a, b) in connected or (b, a) in connected

        for n in networks:
            for c in computers:
                if all(are_connected(c, x) for x in n):
                    n.add(c)

        largest = max(networks, key=len)
        largest = sorted(largest)

        return ",".join(largest)


# Day23("test").run()
Day23().run()
