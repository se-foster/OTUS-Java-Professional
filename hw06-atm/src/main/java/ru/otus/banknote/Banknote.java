package ru.otus.banknote;

public abstract class Banknote implements Comparable<Banknote> {

    @Override
    public int compareTo(Banknote banknote) {
        return Integer.compare(banknote.nominal, this.nominal);
    }

    private final int nominal;
    public Banknote(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Banknote banknote = (Banknote) o;

        return nominal == banknote.nominal;
    }

    @Override
    public int hashCode() {
        return nominal;
    }
}
