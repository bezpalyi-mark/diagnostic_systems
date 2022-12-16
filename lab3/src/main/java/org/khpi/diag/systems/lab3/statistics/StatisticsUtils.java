package org.khpi.diag.systems.lab3.statistics;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Diagnosis;
import org.khpi.diag.systems.lab3.model.Individual;
import org.khpi.diag.systems.lab3.model.Stats;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsUtils {

    private StatisticsUtils() {
    }

    public static Map<Diagnosis, Map<Indication, Double>> computeMedicalMemoryTable(Stats stats) {
        Map<Diagnosis, List<Individual>> individualsByDiagnosis = stats.getIndividualsByDiagnosis();

        // Probabilities for Indications in each Diagnosis or Medical Memory Table
        Map<Diagnosis, Map<Indication, Double>> medicalMemoryTable = new HashMap<>();
        individualsByDiagnosis.keySet().forEach(diagnosis -> medicalMemoryTable.put(diagnosis, new EnumMap<>(Indication.class)));

        for (Map.Entry<Diagnosis, List<Individual>> entry : individualsByDiagnosis.entrySet()) {

            Diagnosis diagnosis = entry.getKey();
            List<Individual> individualsWithDiagnosis = entry.getValue();

            Arrays.stream(Indication.values()).forEach(indication -> {
                List<Individual> individualsWithPositiveDichotomies = individualsWithDiagnosis.stream()
                        .filter(individual -> individual.isPositiveDichotomy(indication))
                        .collect(Collectors.toList());

                // Filling indication probabilities by diagnosis
                double probability = individualsWithPositiveDichotomies.isEmpty()
                        ? 0
                        : (double) individualsWithPositiveDichotomies.size() / individualsWithDiagnosis.size();

                medicalMemoryTable.get(diagnosis).put(indication, probability);
            });
        }

        return medicalMemoryTable;
    }

    public static Stats collectStats(List<Individual> individuals) {
        Stats stats = new Stats();

        // N1
        Map<Diagnosis, List<Individual>> individualsByDiagnosis = individuals.stream()
                .collect(Collectors.groupingBy(Individual::getDiagnosis));

        stats.setIndividualsByDiagnosis(individualsByDiagnosis);

        // N2
        stats.setIndividualsIndications(DiagnosesStatistics.individualsIndications(
                individualsByDiagnosis,
                IndicationStatistics.individualsWithIndications(individuals)
        ));

        // N3
        stats.setGeneralIndividualsIndications(
                IndicationStatistics.individualsWithIndications(individuals)
        );

        // Diagnoses probabilities
        stats.setDiagnosisProbabilities(
                DiagnosesStatistics.priorProbabilities(individualsByDiagnosis)
        );

        stats.setGeneralIndicationsProbabilities(
                IndicationStatistics.generalProbabilities(individuals)
        );

        return stats;
    }
}
