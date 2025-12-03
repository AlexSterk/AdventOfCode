from src.setup.day import Day
from src.util.solution import solution

def joltage(bank, N):
    on_batteries = bank[-N:] # get the last N batteries
    bank = bank[:-N] # get all batteries to the left of last N
    bank.reverse() # go in reverse order (i.e. rightmost first)
    for bat in bank:
        for i in range(N):
            if bat >= on_batteries[i]: # can this battery replace the current battery in slot i
                on_batteries[i], bat = bat, on_batteries[i] # swap them
            else: break
    joltage = int(''.join(map(str, on_batteries)))
    return joltage

class Day3(Day):
    @property
    def day(self):
        return 3

    @solution("17346")
    def part1(self) -> object:
        res = 0
        for line in self.read_input():
            res += joltage([int(x) for x in line], 2)
        return res

    @solution("172981362045136")
    def part2(self) -> object:
        c = 0
        for line in self.read_input():
            c += joltage([int(x) for x in line], 12)
        return c

# Day3("test").run()
Day3().run()