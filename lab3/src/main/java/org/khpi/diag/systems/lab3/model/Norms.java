package org.khpi.diag.systems.lab3.model;

public interface Norms<T> {
    boolean test(T type, float value);
}
