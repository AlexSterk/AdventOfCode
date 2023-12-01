package util;

import java.util.Comparator;
import java.util.Objects;

public class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A extends Comparable<? super A>, B> Comparator<Pair<A, B>> comparingByA() {
        return Comparator.comparing(pair -> pair.a);
    }

    public static <A, B extends Comparable<? super B>> Comparator<Pair<A, B>> comparingByB() {
        return Comparator.comparing(pair -> pair.b);
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pair) obj;
        return Objects.equals(this.a, that.a) &&
                Objects.equals(this.b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "Pair[" +
                "a=" + a + ", " +
                "b=" + b + ']';
    }
}


