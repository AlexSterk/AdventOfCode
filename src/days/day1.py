from src.setup.day import Day
from src.util.solution import solution

class Day1(Day):
    @property
    def day(self):
        return 1

    @solution("1102")
    def part1(self) -> object:
        dial = 50
        N = 100
        count = 0

        for line in self.read_input():
            # line is a string
            dir, num = line[0], int(line[1:])
            dir = 1 if dir == "R" else -1
            dial = (dial + dir * num) % N
            if dial == 0:
                count+=1
        return count

    @solution("")
    def part2(self) -> object:
        dial = 50
        N = 100
        count = 0

        for line in self.read_input():
            # line is a string
            dir, num = line[0], int(line[1:])
            dir = 1 if dir == "R" else -1
            count += abs(num // N)
            num = num % N
            temp_dial = dial + dir * num
            dial = temp_dial % N
            count += abs(temp_dial // N)

        return count

Day1("test2").run()
Day1().run()