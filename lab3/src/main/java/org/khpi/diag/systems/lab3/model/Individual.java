package org.khpi.diag.systems.lab3.model;

import org.khpi.diag.systems.lab1.model.Indication;

import java.util.EnumMap;
import java.util.Map;

public class Individual {

    private final int number;
    private final Map<Indication, Boolean> indicationsDichotomy = new EnumMap<>(Indication.class);
    private final Map<Indication, Double> indicationsValues = new EnumMap<>(Indication.class);
    private final Diagnosis diagnosis;
    private final Norms<Indication> indicationsNorms;

    public Individual(int number, Diagnosis diagnosis, Norms<Indication> indicationsNorms) {
        this.diagnosis = diagnosis;
        this.indicationsNorms = indicationsNorms;
        this.number = number;
    }

    public void addIndication(Indication indication, Double indicationValue) {
        boolean notInNormRange = !indicationsNorms.test(indication, indicationValue.floatValue());

        indicationsDichotomy.put(indication, notInNormRange);
        indicationsValues.put(indication, indicationValue);
    }

    public boolean isPositiveDichotomy(Indication indication) {
        return indicationsDichotomy.get(indication);
    }

    public Map<Indication, Boolean> getIndicationsDichotomy() {
        return indicationsDichotomy;
    }

    public Map<Indication, Double> getIndicationsValues() {
        return indicationsValues;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public int getNumber() {
        return number;
    }

    public Norms<Indication> getIndicationsNorms() {
        return indicationsNorms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Individual that = (Individual) o;

        if (number != that.number) return false;
        return diagnosis.equals(that.diagnosis);
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + diagnosis.hashCode();
        return result;
    }
}
