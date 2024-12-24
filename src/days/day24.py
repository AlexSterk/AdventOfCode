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

        z_wires = sorted(z for _, _, _, z in gates if z.startswith("z"))

        b = ""
        for z_wire in z_wires:
            b += str(determine_wire(z_wire))
        b = b[::-1]
        print(b)
        return int(b, 2)

    @solution("gfv,hcm,kfs,tqm,vwr,z06,z11,z16")
    def part2(self) -> object:
        _, gates = self.raw_input.split("\n\n")
        pattern = r"(.+) (AND|XOR|OR) (.+) -> (.+)"
        gates = [re.match(pattern, gate).groups() for gate in gates.split("\n")]

        z_len = len([gate for gate in gates if gate[-1].startswith("z")])

        def eval(inp, wire):
            if wire in inp:
                return inp[wire]
            gate = next(gate for gate in gates if gate[-1] == wire)
            w1, op, w2, _ = gate
            w1 = eval(inp, w1)
            w2 = eval(inp, w2)
            if op == "AND":
                inp[wire] = w1 & w2
            elif op == "XOR":
                inp[wire] = w1 ^ w2
            elif op == "OR":
                inp[wire] = w1 | w2
            return inp[wire]

        def add(x, y):
            inp = {}
            out = {}
            for i in range(z_len):
                inp[f"x{str(i).zfill(2)}"] = (x >> i) & 1
                inp[f"y{str(i).zfill(2)}"] = (y >> i) & 1
            for i in range(z_len):
                out[f"z{str(i).zfill(2)}"] = eval(inp, f"z{str(i).zfill(2)}")
            z = 0
            for i in range(z_len):
                z |= out[f"z{str(i).zfill(2)}"] << i
            return z

        def eval_str(w, wires={}, l=0, max_l=4):
            if w.startswith("x") or w.startswith("y") or l == max_l:
                return w
            gate = next(gate for gate in gates if gate[-1] == w)
            w1, op, w2, o = gate
            w1 = eval_str(w1, wires, l + 1, max_l)
            w2 = eval_str(w2, wires, l + 1, max_l)
            o = "" if max_l == -1 else o
            if op == "AND":
                wires[w] = f"{o}({w1} AND {w2})"
            elif op == "XOR":
                wires[w] = f"{o}({w1} XOR {w2})"
            elif op == "OR":
                wires[w] = f"{o}({w1} OR {w2})"
            return wires[w]

        swaps = []
        def swap_output(a, b):
            swaps.append(a)
            swaps.append(b)
            a = next(gate for gate in gates if gate[-1] == a)
            b = next(gate for gate in gates if gate[-1] == b)

            a1, a2, a3, a4 = a
            b1, b2, b3, b4 = b

            gates.remove(a)
            gates.remove(b)

            gates.append((a1, a2, a3, b4))
            gates.append((b1, b2, b3, a4))

        swap_output("z06", "vwr")
        swap_output("z11", "tqm")
        swap_output("z16", "kfs")
        swap_output("hcm", "gfv")

        for i in range(z_len):
            print(f"z{str(i).zfill(2)} = {eval_str(f'z{str(i).zfill(2)}')}")
            n = 1 << i
            if add(n, n) != n + n:
                print(f"mismatch at bit {i + 1}")
                v = add(n, n)
                print(f"add({n},{n}) = {v}")
                break
        if i + 3 < z_len:
            for i in range(i + 1, i + 3):
                print(f"z{str(i).zfill(2)} = {eval_str(f'z{str(i).zfill(2)}')}")

        return ",".join(sorted(swaps))


# Day24("test").run()
Day24().run()
