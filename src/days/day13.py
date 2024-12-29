from itertools import permutations

from src.setup.day import Day
from src.util.solution import solution

class Day13(Day):
    @property
    def day(self):
        return 13

    @solution("")
    def part1(self) -> object:
        people = set()
        happiness = {}

        for line in self.input:
            line = line.split()
            person = line[0]
            neighbour = line[-1][:-1]
            happiness[(person, neighbour)] = int(line[3]) if line[2] == "gain" else -int(line[3])
            people.add(person)

        max_happiness = 0
        for perm in permutations(people):
            perm = list(perm)
            perm.append(perm[0])
            total = 0
            for i in range(len(perm) - 1):
                total += happiness[(perm[i], perm[i + 1])] + happiness[(perm[i + 1], perm[i])]
            max_happiness = max(max_happiness, total)

        self.people = people
        self.happiness = happiness
        return max_happiness

    @solution("668")
    def part2(self) -> object:
        people, happiness = self.people, self.happiness
        for person in people:
            happiness[("Me", person)] = 0
            happiness[(person, "Me")] = 0
        people.add("Me")

        max_happiness = 0
        for perm in permutations(people):
            perm = list(perm)
            perm.append(perm[0])
            total = 0
            for i in range(len(perm) - 1):
                total += happiness[(perm[i], perm[i + 1])] + happiness[(perm[i + 1], perm[i])]
            max_happiness = max(max_happiness, total)
        return max_happiness

# Day13("test").run()
Day13().run()
