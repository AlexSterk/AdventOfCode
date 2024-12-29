from src.setup.day import Day
from src.util.solution import solution

items = {
    "Weapons": [
        {"name": "Dagger", "cost": 8, "damage": 4, "armor": 0},
        {"name": "Shortsword", "cost": 10, "damage": 5, "armor": 0},
        {"name": "Warhammer", "cost": 25, "damage": 6, "armor": 0},
        {"name": "Longsword", "cost": 40, "damage": 7, "armor": 0},
        {"name": "Greataxe", "cost": 74, "damage": 8, "armor": 0},
    ],
    "Armor": [
        {"name": "Leather", "cost": 13, "damage": 0, "armor": 1},
        {"name": "Chainmail", "cost": 31, "damage": 0, "armor": 2},
        {"name": "Splintmail", "cost": 53, "damage": 0, "armor": 3},
        {"name": "Bandedmail", "cost": 75, "damage": 0, "armor": 4},
        {"name": "Platemail", "cost": 102, "damage": 0, "armor": 5},
    ],
    "Rings": [
        {"name": "Damage +1", "cost": 25, "damage": 1, "armor": 0},
        {"name": "Damage +2", "cost": 50, "damage": 2, "armor": 0},
        {"name": "Damage +3", "cost": 100, "damage": 3, "armor": 0},
        {"name": "Defense +1", "cost": 20, "damage": 0, "armor": 1},
        {"name": "Defense +2", "cost": 40, "damage": 0, "armor": 2},
        {"name": "Defense +3", "cost": 80, "damage": 0, "armor": 3},
    ],
}


def fight(player_hp, player_dmg, player_armor, boss_hp, boss_dmg, boss_armor):
    while True:
        boss_hp -= max(1, player_dmg - boss_armor)
        if boss_hp <= 0:
            return True
        player_hp -= max(1, boss_dmg - player_armor)
        if player_hp <= 0:
            return False


class Day21(Day):
    @property
    def day(self):
        return 21

    @solution("111")
    def part1(self) -> object:
        boss_hp, boss_dmg, boss_armor = 109, 8, 2  # too lazy to parse input
        player_hp, player_dmg, player_armor = 100, 0, 0

        min_cost = float("inf")

        for weapon in items["Weapons"]:
            for armor in items["Armor"] + [{}]:
                for ring1 in items["Rings"] + [{}]:
                    for ring2 in items["Rings"] + [{}]:
                        if ring1 == ring2:
                            continue
                        cost = weapon["cost"] + armor.get("cost", 0) + ring1.get("cost", 0) + ring2.get("cost", 0)
                        if cost >= min_cost:
                            continue
                        player_dmg = weapon["damage"] + ring1.get("damage", 0) + ring2.get("damage", 0)
                        player_armor = armor.get("armor", 0) + ring1.get("armor", 0) + ring2.get("armor", 0)
                        if fight(player_hp, player_dmg, player_armor, boss_hp, boss_dmg, boss_armor):
                            min_cost = cost

        return min_cost

    @solution("188")
    def part2(self) -> object:
        boss_hp, boss_dmg, boss_armor = 109, 8, 2  # too lazy to parse input
        player_hp, player_dmg, player_armor = 100, 0, 0

        max_cost = 0

        for weapon in items["Weapons"]:
            for armor in items["Armor"] + [{}]:
                for ring1 in items["Rings"] + [{}]:
                    for ring2 in items["Rings"] + [{}]:
                        if ring1 == ring2:
                            continue
                        cost = weapon["cost"] + armor.get("cost", 0) + ring1.get("cost", 0) + ring2.get("cost", 0)
                        if cost <= max_cost:
                            continue
                        player_dmg = weapon["damage"] + ring1.get("damage", 0) + ring2.get("damage", 0)
                        player_armor = armor.get("armor", 0) + ring1.get("armor", 0) + ring2.get("armor", 0)
                        if not fight(player_hp, player_dmg, player_armor, boss_hp, boss_dmg, boss_armor):
                            max_cost = cost

        return max_cost


# Day21("test").run()
Day21().run()
