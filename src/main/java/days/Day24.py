import random


class ALU:

    def run(self, A=1, B=1, C=1, D=1, E=1, F=1, G=1, H=1, I=1, J=1, K=1, L=1, M=1, N=1) -> int:
        if C + 8 != D:
            if D + 1 != E:
                if H + 2 != I:
                    if I + 2 != J:
                        z = (((((A + 1) * 26 + B + 1) * 26 + E + 9) * 26 + F + 3) * 26 + G + 2) * 26 + J + 11
                    else:
                        z = (((((A + 1) * 26 + B + 1) * 26 + E + 9) * 26 + F + 3) * 26 + G + 2)
                else:
                    if G - 1 != J:
                        z = ((((A + 1) * 26 + B + 1) * 26 + E + 9) * 26 + F + 3) * 26 + J + 11
                    else:
                        z = ((((A + 1) * 26 + B + 1) * 26 + E + 9) * 26 + F + 3)
            else:
                if H + 2 != I:
                    z = ((((A + 1) * 26 + B + 1) * 26 + F + 3) * 26 + G + 2)
                    x = I + 2 != J
                    z = z * (25 * x + 1) + (J + 11) * x
                else:
                    z = (((A + 1) * 26 + B + 1) * 26 + F + 3)
                    x = G - 1 != J
                    z = z * (25 * x + 1) + (J + 11) * x

        else:
            if B - 3 != E:
                if H + 2 != I:
                    z = ((((A + 1) * 26 + E + 9) * 26 + F + 3) * 26 + G + 2)
                    x = I + 2 != J
                    z = z * (25 * x + 1) + (J + 11) * x

                else:
                    z = (((A + 1) * 26 + E + 9) * 26 + F + 3)
                    x = G - 1 != J
                    z = z * (25 * x + 1) + (J + 11) * x

            else:
                if H + 2 != I:
                    z = (((A + 1) * 26 + F + 3) * 26 + G + 2)
                    x = I + 2 != J
                    z = z * (25 * x + 1) + (J + 11) * x

                else:
                    z = ((A + 1) * 26 + F + 3)
                    x = G - 1 != J
                    z = z * (25 * x + 1) + (J + 11) * x



        w = K
        x = 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -7
        x = int(x == w)
        x = int(x == 0)
        y = (J + 11) * x * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 7
        y = y * x
        z = z + y

        w = L
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 10
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 1
        y = y * x
        z = z + y

        w = M
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -6
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 10
        y = y * x
        z = z + y

        w = N
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -8
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 3
        y = y * x
        z = z + y

        return z

    def run_no_change(self, A, B, C, D, E, F, G, H, I, J, K, L, M, N):
        w = 0
        x = 0
        y = 0
        z = 0

        w = A
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 12
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 1
        y = y * x
        z = z + y

        w = B
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 12
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 1
        y = y * x
        z = z + y

        w = C
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 15
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 16
        y = y * x
        z = z + y

        w = D
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -8
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 5
        y = y * x
        z = z + y

        w = E
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -4
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 9
        y = y * x
        z = z + y

        w = F
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 15
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 3
        y = y * x
        z = z + y

        w = G
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 14
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 2
        y = y * x
        z = z + y

        w = H
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 14
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 15
        y = y * x
        z = z + y

        w = I
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -13
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 5
        y = y * x
        z = z + y

        w = J
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -3
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 11
        y = y * x
        z = z + y

        w = K
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -7
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 7
        y = y * x
        z = z + y

        w = L
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 1
        x = x + 10
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 1
        y = y * x
        z = z + y

        w = M
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -6
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 10
        y = y * x
        z = z + y

        w = N
        x = x * 0
        x = x + z
        x = x % 26
        z = z // 26
        x = x + -8
        x = int(x == w)
        x = int(x == 0)
        y = y * 0
        y = y + 25
        y = y * x
        y = y + 1
        z = z * y
        y = y * 0
        y = y + w
        y = y + 3
        y = y * x
        z = z + y

        return z


for _ in range(20000):
    arr = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    alu = ALU()
    In = [random.choice(arr) for _ in range(14)]
    assert alu.run(*In) == alu.run_no_change(*In)

print(ALU().run())
