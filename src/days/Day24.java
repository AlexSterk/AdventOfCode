package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day24 extends Day {
    private List<Group> immuneSystem, infection;

    @Override
    public void processInput() {
        String[] armies = input.split("\n\n");
        for (String army : armies) {
            String[] lines = army.split("\n");
            List<Group> groups = Arrays.stream(lines).skip(1).map(Group::Group).toList();
            if (army.startsWith("Immune System")) immuneSystem = groups;
            else infection = groups;
        }
        immuneSystem.forEach(g -> g.army = Army.IMMUNE_SYSTEM);
        infection.forEach(g -> g.army = Army.INFECTION);
    }

    @Override
    public Object part1() {
        while (immuneSystem.stream().anyMatch(Group::isAlive) && infection.stream().anyMatch(Group::isAlive)) {
            Set<Group> groups = new TreeSet<>(Group.POWER_COMPARATOR.thenComparing(Group.INIT_COMPARATOR));
            groups.addAll(immuneSystem);
            groups.addAll(infection);
            groups.forEach(group -> group.chooseTarget(group.army == Army.IMMUNE_SYSTEM ? infection : immuneSystem));

            groups = new TreeSet<>(Group.INIT_COMPARATOR);
            groups.addAll(immuneSystem);
            groups.addAll(infection);

            groups.forEach(Group::attack);
        }

        return Stream.concat(immuneSystem.stream(), infection.stream())
                .filter(Group::isAlive)
                .mapToLong(g -> g.units)
                .sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum Army {
        IMMUNE_SYSTEM, INFECTION
    }

    private static class Group {
        private static final Comparator<Group> POWER_COMPARATOR = Comparator.comparing(Group::effectivePower).reversed();
        private static final Comparator<Group> INIT_COMPARATOR = Comparator.comparing((Group g) -> g.initiative).reversed();
        private static final Pattern PATTERN = Pattern.compile("(?<units>\\d+) units each with (?<hp>\\d+) hit points(?: \\((?<immuneAndWeakness>.*)\\))? with an attack that does (?<power>\\d+) (?<type>\\w+) damage at initiative (?<init>\\d+)");

        private final int hp;
        private final int power;
        private final int initiative;
        private final String attack;
        private final Set<String> weaknesses;
        private final Set<String> immunities;
        private int units;
        private Group targeting;
        private Group targetedBy;
        private Army army;

        private Group(int units, int hp, int power, int initiative, String attack, Collection<String> weaknesses, Collection<String> immunities) {
            this.units = units;
            this.hp = hp;
            this.power = power;
            this.initiative = initiative;
            this.attack = attack;
            this.weaknesses = Set.copyOf(weaknesses);
            this.immunities = Set.copyOf(immunities);
        }

        private static Group Group(String description) {
            Matcher matcher = PATTERN.matcher(description);
            matcher.matches();

            String immuneAndWeakness = matcher.group("immuneAndWeakness");
            Set<String> w = new HashSet<>();
            Set<String> i = new HashSet<>();

            if (immuneAndWeakness != null) {
                for (String s : immuneAndWeakness.split("; ")) {
                    var ss = s.replaceFirst(".* to ", "");
                    String[] types = ss.split(", ");
                    Set<String> set = Set.of(types);
                    if (s.startsWith("weak")) w.addAll(set);
                    else i.addAll(set);
                }
            }

            return new Group(
                    Integer.parseInt(matcher.group("units")),
                    Integer.parseInt(matcher.group("hp")),
                    Integer.parseInt(matcher.group("power")),
                    Integer.parseInt(matcher.group("init")),
                    matcher.group("type"),
                    w,
                    i
            );
        }

        private boolean isAlive() {
            return units > 0;
        }

        private int effectivePower() {
            return units * power;
        }

        private void chooseTarget(Collection<Group> possibleTargets) {
            if (!isAlive()) return;
            Optional<Group> target = possibleTargets.stream()
                    .filter(Group::isAlive)
                    .filter(t -> t.targetedBy == null)
                    .min(Comparator.comparing((Group t) -> t.wouldTakeDamage(effectivePower(), attack))
                            .reversed()
                            .thenComparing(POWER_COMPARATOR)
                            .thenComparing(INIT_COMPARATOR));
            target.ifPresent(this::target);
        }

        private void target(Group target) {
            if (!isAlive()) return;
            this.targeting = target;
            target.targetedBy = this;
        }

        private void attack() {
            if (!isAlive() || targeting == null) return;
            targeting.takeDamage(effectivePower(), attack);
            targeting.targetedBy = null;
            targeting = null;
        }

        private void takeDamage(int power, String type) {
            if (immunities.contains(type)) return;
            if (weaknesses.contains(type)) power *= 2;
            int taking = power / hp;
//            System.out.println(taking);
            this.units -= taking;
        }

        private int wouldTakeDamage(int power, String type) {
            if (immunities.contains(type)) return 0;
            if (weaknesses.contains(type)) return power * 2;
            return power;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "units=" + units +
                    ", hp=" + hp +
                    ", power=" + power +
                    ", attack='" + attack + '\'' +
                    ", initiative=" + initiative +
                    ", weaknesses=" + weaknesses +
                    ", immunities=" + immunities +
                    '}';
        }
    }
}
