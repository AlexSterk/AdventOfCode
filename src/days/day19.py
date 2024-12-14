from src.setup.day import Day
from src.util.solution import solution

class Day19(Day):
    @property
    def day(self):
        return 19

    @solution("")
    def part1(self) -> object:
        max_elves = int(self.raw_input) + 1
        elves = {i: 1 for i in range(1, max_elves)}
        while len(elves) > 1:
            for i in sorted(elves.keys()):
                if i not in elves:
                    continue
                if len(elves) == 1:
                    break
                next_elf = (i + 1) % max_elves
                while next_elf not in elves:
                    next_elf = (next_elf + 1) % max_elves
                elves[i] += elves[next_elf]
                del elves[next_elf]
        return list(elves.keys())[0]


    @solution("1410630")
    def part2(self) -> object:
        max_elves = int(self.raw_input)

        i = 1
        while i * 3 < max_elves:
            i *= 3
        return max_elves - i

# Day19("test").run()
Day19().run()
