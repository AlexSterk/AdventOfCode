from collections import deque

from src.setup.day import Day
from src.util.solution import solution

class Day19(Day):
    @property
    def day(self):
        return 19

    @solution("535")
    def part1(self) -> object:
        reactions, molecule = self.raw_input.split("\n\n")
        reactions = [tuple(r.split(" => ")) for r in reactions.split("\n")]

        def get_replacements(molecule):
            for a, b in reactions:
                for i in range(len(molecule)):
                    if molecule[i:i+len(a)] == a:
                        yield molecule[:i] + b + molecule[i+len(a):]

        self.reactions, self.molecule = reactions, molecule
        self.get_replacements = get_replacements

        return len(set(m for m in get_replacements(molecule)))

    @solution("")
    def part2(self) -> object:
        reactions, molecule = self.reactions, self.molecule

        target = molecule
        steps = 0
        while target != "e":
            for r in reactions:
                if r[1] in target:
                    print(target, r)
                    target = target.replace(r[1], r[0], 1)
                    steps += 1
                    break
            else:
                target = molecule
                steps = 0
                continue

        return steps



# Day19("test").run()
Day19().run()
