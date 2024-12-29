from src.setup.day import Day
from src.util.solution import solution

tape = '''children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1'''

class Day16(Day):
    @property
    def day(self):
        return 16

    @solution("373")
    def part1(self) -> object:
        aunts = {}

        for line in self.input:
            idx, props = line.split(": ", 1)
            print(idx, props)
            aunts[int(idx.split(" ")[1])] = {k: int(v) for k, v in [prop.split(": ") for prop in props.split(", ")]}

        for line in tape.split("\n"):
            prop, val = line.split(": ")
            aunts = {k: v for k, v in aunts.items() if v.get(prop, int(val)) == int(val)}

        return next(iter(aunts))

    @solution("260")
    def part2(self) -> object:
        aunts = {}

        for line in self.input:
            idx, props = line.split(": ", 1)
            print(idx, props)
            aunts[int(idx.split(" ")[1])] = {k: int(v) for k, v in [prop.split(": ") for prop in props.split(", ")]}

        for line in tape.split("\n"):
            prop, val = line.split(": ")
            if prop in ["cats", "trees"]:
                aunts = {k: v for k, v in aunts.items() if v.get(prop, int(val) + 1) > int(val)}
            elif prop in ["pomeranians", "goldfish"]:
                aunts = {k: v for k, v in aunts.items() if v.get(prop, int(val) - 1) < int(val)}
            else:
                aunts = {k: v for k, v in aunts.items() if v.get(prop, int(val)) == int(val)}

        return next(iter(aunts))

# Day16("test").run()
Day16().run()
