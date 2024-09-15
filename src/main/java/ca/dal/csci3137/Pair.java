package ca.dal.csci3137;

import java.util.Objects;

public final class Pair {
    final Object ar;
    final Object dr;

    Pair(Object ar, Object dr) {
        this.ar = ar;
        this.dr = dr;
    }

    private String _toString() {
        if (this == SchemeCore.nil) return "";
        if (!(dr instanceof Pair)) return ar.toString() + " . " + dr.toString();
        return ar.toString() + (dr != SchemeCore.nil ? " " + ((Pair) dr)._toString() : "");
    }

    public String toString() {
        return "(" + _toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(ar, pair.ar) && Objects.equals(dr, pair.dr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ar, dr);
    }
}
