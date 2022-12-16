package org.khpi.diag.systems.lab3.model;

public class Diagnosis {
    private final int number;

    public Diagnosis(int number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Diagnosis diagnosis = (Diagnosis) o;

        return number == diagnosis.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
