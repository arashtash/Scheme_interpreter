package ca.dal.csci3137;

import java.util.Objects;

public final class Symbol {
    private final String name;

    Symbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(name, symbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
