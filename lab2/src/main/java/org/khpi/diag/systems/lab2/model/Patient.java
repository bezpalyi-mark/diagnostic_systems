package org.khpi.diag.systems.lab2.model;

import org.khpi.diag.systems.lab1.model.Indication;

import java.util.Map;

public class Patient {

    private final int number;
    private final Map<Factor, Double> factors;
    private final Map.Entry<Indication, Double> response;
    private final int diagnosis;

    public Patient(int number, Map<Factor, Double> factors, Map.Entry<Indication, Double> response, int diagnosis) {
        this.number = number;
        this.factors = factors;
        this.response = response;
        this.diagnosis = diagnosis;
    }

    public int getNumber() {
        return number;
    }

    public Map<Factor, Double> getFactors() {
        return factors;
    }

    public Map.Entry<Indication, Double> getResponse() {
        return response;
    }

    public int getDiagnosis() {
        return diagnosis;
    }
}

