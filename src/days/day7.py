from src.setup.day import Day
from src.util.solution import solution


class Day7(Day):
    @property
    def day(self):
        return 7

    @solution("1698")
    def part1(self) -> object:
        first_line, *lines = self.input

        count = 0
        beams = [c == 'S' for c in first_line]

        for line in lines:
            for i,c in enumerate(line):
                if c == '^':
                    count += 1 if beams[i] else 0
                    beams[i-1] += beams[i]
                    beams[i+1] += beams[i]
                    beams[i] = 0


        return count

    @solution("95408386769474")
    def part2(self) -> object:
        first_line, *lines = self.input

        beams = [c == 'S' for c in first_line]

        for line in lines:
            for i,c in enumerate(line):
                if c == '^':
                    beams[i-1] += beams[i]
                    beams[i+1] += beams[i]
                    beams[i] = 0


        return sum(beams)


# Day7("test").run()
Day7().run()
