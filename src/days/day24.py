import re

from src.setup.day import Day
from src.util.solution import solution

class Day24(Day):
    @property
    def day(self):
        return 24

    @solution("50411513338638")
    def part1(self) -> object:
        wires, gates = self.raw_input.split("\n\n")
        wires = {wire: int(value) for wire, value in map(lambda s: s.split(": "), wires.split("\n"))}
        pattern = r"(.+) (AND|XOR|OR) (.+) -> (.+)"
        gates = [re.match(pattern, gate).groups() for gate in gates.split("\n")]

        def determine_wire(wire):
            if wire in wires:
                return wires[wire]
            gate = next(gate for gate in gates if gate[-1] == wire)
            w1, op, w2, _ = gate
            w1 = determine_wire(w1)
            w2 = determine_wire(w2)
            if op == "AND":
                wires[wire] = w1 & w2
            elif op == "XOR":
                wires[wire] = w1 ^ w2
            elif op == "OR":
                wires[wire] = w1 | w2
            return wires[wire]

        z_wires = sorted(z for _,_,_,z in gates if z.startswith("z"))

        b = ""
        for z_wire in z_wires:
            b += str(determine_wire(z_wire))
        b = b[::-1]
        print(b)
        return int(b, 2)

    @solution("")
    def part2(self) -> object:
        return None

# Day24("test").run()
Day24().run()