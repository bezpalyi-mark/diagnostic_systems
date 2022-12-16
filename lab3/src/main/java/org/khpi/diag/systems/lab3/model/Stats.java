package org.khpi.diag.systems.lab3.model;

import org.khpi.diag.systems.lab1.model.Indication;

import java.util.List;
import java.util.Map;

public class Stats {
    private Map<Diagnosis, List<Individual>> individualsByDiagnosis;
    private Map<Diagnosis, Map<Indication, List<Individual>>> individualsIndications;
    private Map<Indication, List<Individual>> generalIndividualsIndications;
    private Map<Diagnosis, Double> diagnosisProbabilities;
    private Map<Indication, Double> generalIndicationsProbabilities;

    // SETTERS

    public void setIndividualsByDiagnosis(Map<Diagnosis, List<Individual>> individualsByDiagnosis) {
        this.individualsByDiagnosis = individualsByDiagnosis;
    }

    public void setIndividualsIndications(Map<Diagnosis, Map<Indication, List<Individual>>> individualsIndications) {
        this.individualsIndications = individualsIndications;
    }

    public void setGeneralIndividualsIndications(Map<Indication, List<Individual>> generalIndividualsIndications) {
        this.generalIndividualsIndications = generalIndividualsIndications;
    }

    public void setDiagnosisProbabilities(Map<Diagnosis, Double> diagnosisProbabilities) {
        this.diagnosisProbabilities = diagnosisProbabilities;
    }

    public void setGeneralIndicationsProbabilities(Map<Indication, Double> generalIndicationsProbabilities) {
        this.generalIndicationsProbabilities = generalIndicationsProbabilities;
    }

    // GETTERS

    public Map<Diagnosis, List<Individual>> getIndividualsByDiagnosis() {
        return individualsByDiagnosis;
    }

    public Map<Diagnosis, Map<Indication, List<Individual>>> getIndividualsIndications() {
        return individualsIndications;
    }

    public Map<Indication, List<Individual>> getGeneralIndividualsIndications() {
        return generalIndividualsIndications;
    }

    public Map<Diagnosis, Double> getDiagnosisProbabilities() {
        return diagnosisProbabilities;
    }

    public Map<Indication, Double> getGeneralIndicationsProbabilities() {
        return generalIndicationsProbabilities;
    }
}
