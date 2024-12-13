import time

class Day:
    input = None
    raw_input = None

    def __init__(self, input_file = "input"):
        self.input_file = input_file

    def part1(self) -> object:
        pass

    def part2(self) -> object:
        pass

    @property
    def day(self):
        raise NotImplementedError

    def read_input(self):
        f = f"data/day{self.day}/{self.input_file}.txt"
        with open(f, "r") as file:
            self.raw_input = file.read().strip()
            return self.raw_input.split("\n")

    def run(self):
        print(f"Running day {self.day} with {self.input_file}")
        self.input = self.read_input()
        s = time.time()
        print(f"Part 1: {self.part1()} ({time.time() - s:.6f}s)")
        s = time.time()
        print(f"Part 2: {self.part2()} ({time.time() - s:.6f}s)")
        print()
