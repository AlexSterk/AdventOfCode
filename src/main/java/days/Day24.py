import re

import z3

input_file = "data/day24/input.txt"
with open(input_file) as f:
    lines = f.readlines()
    lines = [line.strip() for line in lines]

hailstones = []
for line in lines:
    x, y, z, vx, vy, vz = [int(s) for s in re.findall(r"(-?\d+)", line)]
    hailstones.append((x, y, z, vx, vy, vz))

xr, yr, zr, vxr, vyr, vzr = z3.Reals('xr yr zr vxr vyr vzr')
solver = z3.Solver()
i = 0
for h in hailstones[:3]:
    xh, yh, zh, vxh, vyh, vzh = h

    ti = z3.Real(f"t{i}")
    i += 1
    solver.add(ti > 0)
    solver.add(xr + vxr * ti == xh + vxh * ti)
    solver.add(yr + vyr * ti == yh + vyh * ti)
    solver.add(zr + vzr * ti == zh + vzh * ti)
solver.check()
x, y, z = [solver.model()[v].as_long() for v in [xr, yr, zr]]
print(sum([x, y, z]))
