import itertools

from src.setup.day import Day
from src.util.solution import solution

class Day15(Day):
    @property
    def day(self):
        return 15

    @solution("21367368")
    def part1(self) -> object:
        ingredients = {}

        for line in self.input:
            name, rest = line.split(": ")
            capacity, durability, flavor, texture, calories = [int(x.split(" ")[1]) for x in rest.split(", ")]
            ingredients[name] = (capacity, durability, flavor, texture, calories)

        best = 0
        for comb in itertools.combinations_with_replacement(ingredients.keys(), 100):
            comb = list(comb)
            counts = {x: comb.count(x) for x in comb}

            capacity = max(0, sum([counts[x] * ingredients[x][0] for x in counts]))
            durability = max(0, sum([counts[x] * ingredients[x][1] for x in counts]))
            flavor = max(0, sum([counts[x] * ingredients[x][2] for x in counts]))
            texture = max(0, sum([counts[x] * ingredients[x][3] for x in counts]))

            if capacity < 0 or durability < 0 or flavor < 0 or texture < 0:
                continue
            best = max(best, capacity * durability * flavor * texture)

        return best

    @solution("1766400")
    def part2(self) -> object:
        ingredients = {}

        for line in self.input:
            name, rest = line.split(": ")
            capacity, durability, flavor, texture, calories = [int(x.split(" ")[1]) for x in rest.split(", ")]
            ingredients[name] = (capacity, durability, flavor, texture, calories)

        best = 0
        for comb in itertools.combinations_with_replacement(ingredients.keys(), 100):
            comb = list(comb)
            counts = {x: comb.count(x) for x in comb}

            capacity = max(0, sum([counts[x] * ingredients[x][0] for x in counts]))
            durability = max(0, sum([counts[x] * ingredients[x][1] for x in counts]))
            flavor = max(0, sum([counts[x] * ingredients[x][2] for x in counts]))
            texture = max(0, sum([counts[x] * ingredients[x][3] for x in counts]))
            calories = max(0, sum([counts[x] * ingredients[x][4] for x in counts]))

            if capacity < 0 or durability < 0 or flavor < 0 or texture < 0 or calories != 500:
                continue
            best = max(best, capacity * durability * flavor * texture)

        return best

# Day15("test").run()
Day15().run()
